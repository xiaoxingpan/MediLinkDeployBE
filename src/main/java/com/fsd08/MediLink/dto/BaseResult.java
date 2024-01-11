package com.fsd08.MediLink.dto;

public class BaseResult<T> {

    private static final int OK = 200;
    private static final int ERROR = 500;

    private int status;

    private String message;

    private T data;

    public static BaseResult<?> ok(String message) {
        return new BaseResult<>(OK, message, null);
    }

    public static <T> BaseResult<T> ok(T data) {
        return new BaseResult<>(OK, null, data);
    }

    public static BaseResult<?> error(String message) {
        return new BaseResult<>(ERROR, message, null);
    }

    public BaseResult(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
