package com.aide.backend.service.impl;

import com.aide.backend.domain.dto.common.PageResponse;
import com.aide.backend.domain.dto.exam.*;
import com.aide.backend.domain.entity.exams.ChatMessage;
import com.aide.backend.domain.entity.exams.ExamSession;
import com.aide.backend.domain.entity.patients.Patient;
import com.aide.backend.domain.enums.SenderType;
import com.aide.backend.exception.ResourceNotFoundException;
import com.aide.backend.repository.ChatLogRepository;
import com.aide.backend.repository.ExamSessionRepository;
import com.aide.backend.repository.PatientRepository;
import com.aide.backend.repository.UserRepository;
import com.aide.backend.service.ExamService;
import com.openai.client.OpenAIClient;
import com.openai.core.JsonValue;
import com.openai.models.ChatModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.ResponseOutputText;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.aide.backend.config.Constants.*;
import static com.aide.backend.config.OpenAIClient.PATIENT_ASSISTANT_PROMPT_ID;
import static com.aide.backend.config.OpenAIClient.PATIENT_PROMPT_VERSION;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

    private final OpenAIClient openAIClient;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final ExamSessionRepository examSessRepository;
    private final ChatLogRepository chatLogRepository;

    @Override
    public PageResponse<TestCaseDTO> listTestCases(Pageable pageable) {
        Page<Patient> page = patientRepository.findAll(pageable);
        return PageResponse.of(page.map(this::mapToTestCaseDTO));
    }

    @Override
    public CreateExamResponse createNewExam(CreateExamRequest req) {
        var user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + req.getUsername()));

        var patient = patientRepository
                .findById(req.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not fount: " + req.getPatientId()));

        // Check if there is an existing exam session for the patient
        var examSess = examSessRepository.findTopExamSessionByPatientId(patient.getId(), user.getId());
        if (examSess.isPresent()) {
            var examSession = examSess.get();
            return CreateExamResponse.builder()
                    .sessId(examSession.getId())
                    .mess(null)
                    .timeLeft(LocalDateTime.now().until(examSession.getExpiresAt(), java.time.temporal.ChronoUnit.MINUTES))
                    .build();
        }

        var newSess = ExamSession.builder()
                .patient(patient)
                .user(user)
                .startedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(EXAM_SESSION_DURATION_MINUTES))
                .build();

        JsonValue promptField = buildPromptField(patient);
        ResponseCreateParams params = ResponseCreateParams.builder()
                .putAdditionalBodyProperty("prompt", promptField)
                .input("Xin chào")
                .model(ChatModel.GPT_4O_MINI)
                .maxOutputTokens(MAX_OUTPUT_TOKEN)
                .temperature(TEMPERATURE)
                .store(true)
                .build();

        var response = this.openAIClient.responses().create(params);
        var text = response.output().stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .map(ResponseOutputText::text)
                .findFirst()
                .orElse(null);

        var message = ChatMessage.builder()
                .message(text)
                .sender(SenderType.AI)
                .session(newSess)
                .build();
        newSess.setChatMessages(List.of(message));
        newSess.setUser(user);
        newSess.setLastedMessageId(response.id());
        newSess = examSessRepository.save(newSess);

        return CreateExamResponse.builder()
                .sessId(newSess.getId())
                .mess(text)
                .timeLeft(Long.valueOf(EXAM_SESSION_DURATION_MINUTES))
                .build();
    }

    @Override
    public GetChatSessionResponse getChatSession(GetChatSessionRequest req) {
        var examSession = examSessRepository.findById(req.getSessId())
                .orElseThrow(() -> new ResourceNotFoundException("Exam session not found with id: " + req.getSessId()));

        if (!Objects.equals(examSession.getUser().getUsername(), req.getUsername())) {
            throw new ResourceNotFoundException("You are not authorized to access this exam session.");
        }

        if (examSession.getExpiresAt().isBefore(LocalDateTime.now()) || examSession.getFinishedAt() != null) {
            throw new ResourceNotFoundException("Exam session has expired.");
        }

        var existMessages = chatLogRepository.findChatLogsBySessionId(examSession.getId());
        ChatMessageDTO[] chatLogDTOs = existMessages.stream()
                .map(msg -> ChatMessageDTO.builder()
                        .id(msg.getId())
                        .message(msg.getMessage())
                        .senderType(msg.getSender())
                        .build())
                .toArray(ChatMessageDTO[]::new);

        return GetChatSessionResponse.builder()
                .sessId(req.getSessId())
                .existMessages(chatLogDTOs)
                .timeLeft(LocalDateTime.now().until(examSession.getExpiresAt(), java.time.temporal.ChronoUnit.MINUTES))
                .build();
    }

    @Override
    public SendChatMessageResponse sendChatMessage(SendChatMessageRequest req) {
        var session = examSessRepository.findById(req.getSessId())
                .orElseThrow(() -> new ResourceNotFoundException("Exam session not found with id: " + req.getSessId()));

        if (session.getExpiresAt().isBefore(LocalDateTime.now()) || session.getFinishedAt() != null) {
            throw new ResourceNotFoundException("Exam session has expired.");
        }

        if (session.getChatMessages().size() > MAX_CHAT_LOGS) {
            return SendChatMessageResponse.builder()
                    .message("Hết lượt chat vui lòng đưa ra kết quả chuẩn đoán nhé >.<")
                    .sessId(req.getSessId())
                    .msgId(null)
                    .attachments(new String[]{})
                    .sentAt(LocalDateTime.now().toString())
                    .build();
        }

        JsonValue promptField = buildPromptField(session.getPatient());
        ResponseCreateParams params = ResponseCreateParams.builder()
                .putAdditionalBodyProperty("prompt", promptField)
                .previousResponseId(session.getLastedMessageId())
                .input(req.getMessage())
                .model(ChatModel.GPT_4O_MINI)
                .maxOutputTokens(MAX_OUTPUT_TOKEN)
                .temperature(TEMPERATURE)
                .store(true)
                .build();

        var response = this.openAIClient.responses().create(params);
        var text = extractResponseText(response);

        //user chat msg
        var usrMsg = ChatMessage.builder()
                .message(req.getMessage())
                .sender(SenderType.USER)
                .session(session)
                .build();
        usrMsg.setSession(session);

        //ai chat msg
        var aiMsg = ChatMessage.builder()
                .message(text)
                .sender(SenderType.AI)
                .openaiMsgId(response.id())
                .session(session)
                .build();
        aiMsg.setSession(session);

        session.getChatMessages().add(usrMsg);
        session.getChatMessages().add(aiMsg);
        examSessRepository.save(session);

        return SendChatMessageResponse.builder()
                .message(text)
                .sessId(req.getSessId())
                .msgId(aiMsg.getId())
                .attachments(new String[]{})
                .sentAt(LocalDateTime.now().toString())
                .build();
    }

    private TestCaseDTO mapToTestCaseDTO(Patient patient) {
        return TestCaseDTO.builder().id(patient.getId())
                .name(patient.getName())
                .mode(patient.getMode().toString())
                .requestCount(patient.getRequestCounter())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .build();
    }

    private JsonValue buildPromptField(Patient patient) {
        JsonValue variable = JsonValue.from(Map.of(
                "patient_json", patient.getOpenAPIVariableForPatient()));
        return JsonValue.from(Map.of(
                "id", PATIENT_ASSISTANT_PROMPT_ID,
                "version", PATIENT_PROMPT_VERSION,
                "variables", variable
        ));
    }

    private String extractResponseText(Response response) {
        return response.output().stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .map(ResponseOutputText::text)
                .findFirst()
                .orElse(null);
    }
}
