package com.aide.backend.domain.dto.exam;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetChatSessionResponse {
    private Long sessId;
    private Long timeLeft;
    private ChatMessageDTO[] existMessages;
}
