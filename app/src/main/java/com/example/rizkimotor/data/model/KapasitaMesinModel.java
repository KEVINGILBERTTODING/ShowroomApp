package com.example.rizkimotor.data.model;

public class KapasitaMesinModel {
    private int km_id;
    private String kapasitas;

    public KapasitaMesinModel(int km_id, String kapasitas) {
        this.km_id = km_id;
        this.kapasitas = kapasitas;
    }

    @Override
    public String toString() {
        return getKapasitas();
    }

    public int getKm_id() {
        return km_id;
    }

    public void setKm_id(int km_id) {
        this.km_id = km_id;
    }

    public String getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(String kapasitas) {
        this.kapasitas = kapasitas;
    }
}
