package com.example.rizkimotor.data.model;

public class MerkModel {
    private int merk_id;
    private String merk;


    public MerkModel(int merk_id, String merk) {
        this.merk_id = merk_id;
        this.merk = merk;
    }

    @Override
    public String toString() {
        return getMerk();
    }
    public int getMerk_id() {
        return merk_id;
    }

    public void setMerk_id(int merk_id) {
        this.merk_id = merk_id;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }
}
