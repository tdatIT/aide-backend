package com.aide.backend.domain.dto.exam;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateExamResponse {
    private Long sessId;
    private String mess;
    private Long timeLeft;
    private ChatMessageDTO[] existMessages;
}
