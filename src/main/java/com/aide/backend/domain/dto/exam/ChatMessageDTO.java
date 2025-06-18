package com.aide.backend.domain.dto.exam;

import com.aide.backend.domain.enums.SenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private Long id;
    private String message;
    private SenderType senderType;
    private String openaiMessageId;
    private LocalDateTime createdAt;
}
