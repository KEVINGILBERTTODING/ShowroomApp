package com.example.rizkimotor.data.model;

import com.airbnb.lottie.L;

import java.util.List;

public class ResponseFilterModel {
    private String state;
    private String message;
    private List<MerkModel> data_merk;
    private List<TransmisiModel> data_transmisi;
    private List<BodyModel> data_body;

    public ResponseFilterModel(String state, String message, List<MerkModel> data_merk, List<TransmisiModel> data_transmisi, List<BodyModel> data_body) {
        this.state = state;
        this.message = message;
        this.data_merk = data_merk;
        this.data_transmisi = data_transmisi;
        this.data_body = data_body;
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

    public List<MerkModel> getData_merk() {
        return data_merk;
    }

    public void setData_merk(List<MerkModel> data_merk) {
        this.data_merk = data_merk;
    }

    public List<TransmisiModel> getData_transmisi() {
        return data_transmisi;
    }

    public void setData_transmisi(List<TransmisiModel> data_transmisi) {
        this.data_transmisi = data_transmisi;
    }

    public List<BodyModel> getData_body() {
        return data_body;
    }

    public void setData_body(List<BodyModel> data_body) {
        this.data_body = data_body;
    }
}
