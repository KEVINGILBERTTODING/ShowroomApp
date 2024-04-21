package com.example.rizkimotor.data.model;

public class TankModel {
    private int tangki_id;
    private String tangki;

    public TankModel() {
    }

    @Override
    public String toString() {
        return getTangki();
    }

    public int getTangki_id() {
        return tangki_id;
    }

    public void setTangki_id(int tangki_id) {
        this.tangki_id = tangki_id;
    }

    public String getTangki() {
        return tangki;
    }

    public void setTangki(String tangki) {
        this.tangki = tangki;
    }
}
