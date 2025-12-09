package com.example.assesment.dtos;



public class BaseApiResponse<T> {

    private boolean success;
    private String message;
    private T data;

    public BaseApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> BaseApiResponse<T> success(String message, T data) {
        return new BaseApiResponse<>(true, message, data);
    }

    public static <T> BaseApiResponse<T> failure(String message) {
        return new BaseApiResponse<>(false, message, null);
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public T getData() { return data; }
}
