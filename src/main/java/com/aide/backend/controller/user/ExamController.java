package com.aide.backend.controller.user;

import com.aide.backend.domain.dto.common.BaseResponse;
import com.aide.backend.domain.dto.common.PageResponse;
import com.aide.backend.domain.dto.exam.*;
import com.aide.backend.domain.entity.user.AuthUserDetails;
import com.aide.backend.service.ExamService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @GetMapping("")
    public ResponseEntity<BaseResponse<PageResponse<TestCaseDTO>>> getListTestCase(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<TestCaseDTO> testCases = examService.listTestCases(pageable);
        return ResponseEntity.ok(BaseResponse.success(testCases));
    }

    @PostMapping("/sessions")
    public ResponseEntity<BaseResponse<CreateExamResponse>> createNewExamSession(
            @Valid @RequestBody CreateExamRequest request,
            @AuthenticationPrincipal AuthUserDetails user) {
        request.setUsername(user.getUsername());
        CreateExamResponse response = examService.createNewExam(request);
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @GetMapping("/sessions/{sessId}/chat")
    public ResponseEntity<BaseResponse<GetChatSessionResponse>> getSessionChat(
            @PathVariable Long sessId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal AuthUserDetails user) {

        GetChatSessionRequest request = new GetChatSessionRequest();
        request.setUsername(user.getUsername());
        request.setSessId(sessId);

        GetChatSessionResponse response = examService.getChatSession(request);
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @PostMapping("/sessions/{sessId}/chat")
    public ResponseEntity<BaseResponse<SendChatMessageResponse>> sendMessageRequest(
            @PathVariable Long sessId,
            @Valid @RequestBody SendChatMessageRequest request) {
        request.setSessId(sessId);
        var response = examService.sendChatMessage(request);
        return ResponseEntity.ok(BaseResponse.success(response));
    }
}
