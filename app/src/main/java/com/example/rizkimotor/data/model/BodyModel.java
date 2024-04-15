package com.example.rizkimotor.data.model;

public class BodyModel {
    private int body_id;
    private String body;


    public BodyModel(int body_id, String body) {
        this.body_id = body_id;
        this.body = body;
    }

    @Override
    public String toString() {
        return getBody();
    }

    public int getBody_id() {
        return body_id;
    }

    public void setBody_id(int body_id) {
        this.body_id = body_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
