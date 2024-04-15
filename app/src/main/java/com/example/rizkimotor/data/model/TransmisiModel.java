package com.example.rizkimotor.data.model;

public class TransmisiModel {

    private int transmisi_id;
    private String transmisi;


    public TransmisiModel(int transmisi_id, String transmisi) {
        this.transmisi_id = transmisi_id;
        this.transmisi = transmisi;
    }

    @Override
    public String toString() {
        return getTransmisi();
    }

    public int getTransmisi_id() {
        return transmisi_id;
    }

    public void setTransmisi_id(int transmisi_id) {
        this.transmisi_id = transmisi_id;
    }

    public String getTransmisi() {
        return transmisi;
    }

    public void setTransmisi(String transmisi) {
        this.transmisi = transmisi;
    }
}
