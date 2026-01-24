package com.dev.dungcony.modules.authorization.dtos.responses;

public class ApiRes {
    private boolean success;
    private String message;
    private Object data;

    public ApiRes() {
    }

    public ApiRes(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiRes(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static ApiRes success(String message) {
        return new ApiRes(true, message);
    }

    public static ApiRes success(String message, Object data) {
        return new ApiRes(true, message, data);
    }

    public static ApiRes error(String message) {
        return new ApiRes(false, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
