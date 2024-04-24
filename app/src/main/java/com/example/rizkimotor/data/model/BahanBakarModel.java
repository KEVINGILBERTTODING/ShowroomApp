package com.example.rizkimotor.data.model;

public class BahanBakarModel {
    private int bahan_bakar_id;
    private String bahan_bakar;

    public BahanBakarModel(int bahan_bakar_id, String bahan_bakar) {
        this.bahan_bakar_id = bahan_bakar_id;
        this.bahan_bakar = bahan_bakar;
    }

    @Override
    public String toString() {
        return getBahan_bakar();
    }

    public int getBahan_bakar_id() {
        return bahan_bakar_id;
    }

    public void setBahan_bakar_id(int bahan_bakar_id) {
        this.bahan_bakar_id = bahan_bakar_id;
    }

    public String getBahan_bakar() {
        return bahan_bakar;
    }

    public void setBahan_bakar(String bahan_bakar) {
        this.bahan_bakar = bahan_bakar;
    }
}
