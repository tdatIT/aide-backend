package com.aide.backend.domain.dto.exam;

import com.aide.backend.domain.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendChatMessageRequest {
    private Long sessId;
    private MessageType messageType;
    private String message;
    private String testCatId;
    private String previousMessId;
    private String sentAt;
}
