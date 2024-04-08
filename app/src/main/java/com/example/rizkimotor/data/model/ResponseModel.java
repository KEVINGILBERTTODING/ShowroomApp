package com.example.rizkimotor.data.model;

public class ResponseModel<T> {
    private String state;
    private String message;
    private T data;

    public ResponseModel(String status, String message, T data) {
        this.state = status;
        this.message = message;
        this.data = data;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
