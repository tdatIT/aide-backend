package com.aide.backend.model.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private boolean success;
    private String error;
    private String message;
    private T data;


    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(true, null, "success", data);
    }

    public static <T> BaseResponse<T> success(String message, T data) {
        return new BaseResponse<>(true, null, message, data);
    }

    public static <T> BaseResponse<T> error(String error) {
        return new BaseResponse<>(false, error, null, null);
    }

    public static <T> BaseResponse<T> error(String message, String error) {
        return new BaseResponse<>(false, error, message, null);
    }
}
