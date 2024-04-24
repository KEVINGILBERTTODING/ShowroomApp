package com.example.rizkimotor.data.model;

public class KapasitasPenumpangModel {
     private  int kp_id;
     private String kapasitas;


    public KapasitasPenumpangModel(int kp_id, String kapasitas) {
        this.kp_id = kp_id;
        this.kapasitas = kapasitas;
    }

    @Override
    public String toString() {
        return getKapasitas();
    }

    public int getKp_id() {
        return kp_id;
    }

    public void setKp_id(int kp_id) {
        this.kp_id = kp_id;
    }

    public String getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(String kapasitas) {
        this.kapasitas = kapasitas;
    }
}
