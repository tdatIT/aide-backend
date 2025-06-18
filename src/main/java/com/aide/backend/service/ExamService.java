package com.aide.backend.service;


import com.aide.backend.domain.dto.common.PageResponse;
import com.aide.backend.domain.dto.exam.*;
import org.springframework.data.domain.Pageable;

public interface ExamService {
    PageResponse<TestCaseDTO> listTestCases(Pageable pageable);

    CreateExamResponse createNewExam(CreateExamRequest req);

    SendChatMessageResponse sendChatMessage(SendChatMessageRequest req);

    GetChatSessionResponse getChatSession(GetChatSessionRequest req);
}
