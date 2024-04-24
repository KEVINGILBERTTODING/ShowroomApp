package com.example.rizkimotor.data.model;

public class ColorModel {
    private int warna_id;
    private String warna;

    public ColorModel(int warna_id, String warna) {
        this.warna_id = warna_id;
        this.warna = warna;
    }

    @Override
    public String toString() {
        return getWarna();
    }
    public int getWarna_id() {
        return warna_id;
    }

    public void setWarna_id(int warna_id) {
        this.warna_id = warna_id;
    }

    public String getWarna() {
        return warna;
    }

    public void setWarna(String warna) {
        this.warna = warna;
    }
}
