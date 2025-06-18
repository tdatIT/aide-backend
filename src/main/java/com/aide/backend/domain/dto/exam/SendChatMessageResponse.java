package com.aide.backend.domain.dto.exam;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendChatMessageResponse {
    private Long sessId;
    private Long msgId;
    private String message;
    private String[] attachments;
    private String sentAt;
}
