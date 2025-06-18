package com.aide.backend.config;

import com.openai.client.okhttp.OpenAIOkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIClient {
    @Value("${openai.api-key}")
    private String apiKey;

    public static final String PATIENT_ASSISTANT_PROMPT_ID = "pmpt_6850fe51e6c08193a662059de7dc225e0bba29368db60a0e";
    public static final String PATIENT_PROMPT_VERSION = "3";

    @Bean
    public com.openai.client.OpenAIClient getOpenAIClient() {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("OpenAI API key is not configured.");
        }
        return OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .build();
    }
}
