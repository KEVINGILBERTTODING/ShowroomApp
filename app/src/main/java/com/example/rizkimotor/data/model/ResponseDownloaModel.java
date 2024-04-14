package com.example.rizkimotor.data.model;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class ResponseDownloaModel {
    private String state;
    private String message;
    private ResponseBody responseBody;

    public ResponseDownloaModel(String state, String message, ResponseBody responseBody) {
        this.state = state;
        this.message = message;
        this.responseBody = responseBody;
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

    public ResponseBody getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }
}
