package com.aide.backend.config;

public class Constants {
    public static final Integer EXAM_SESSION_DURATION_MINUTES = 10; // Example duration for exam sessions
    public static final Integer MAX_OUTPUT_TOKEN = 100;
    public static final Double TEMPERATURE = 0.8; // Example temperature for OpenAI API
    public static final Integer MAX_CHAT_LOGS = 20; // Maximum number of chat logs to keep per session
    public static final Integer MAX_ACCESS_TOKEN_EXP = 60 * 60 * 24;
    public static final Integer MAX_REFRESH_TOKEN_EXP = 60 * 60 * 24 * 7;
    public static final Integer SECOND_TO_MILLI = 1000;
    public static final Integer DEFAULT_TTL_MIN=30; // Default time to live in minutes for cache entries
}
