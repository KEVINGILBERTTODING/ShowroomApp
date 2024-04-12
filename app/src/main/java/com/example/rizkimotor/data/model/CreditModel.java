package com.example.rizkimotor.data.model;

public class CreditModel {
    private int totalMinDp;
    private int totalMaxDp;
    private double biaya_admin;
    private double biaya_asuransi;
    private double biaya_provisi;
    private double totalCicilan;

    public CreditModel(int totalMinDp, int totalMaxDp, int biaya_admin, double biaya_asuransi, int biaya_provisi, double totalCicilan) {
        this.totalMinDp = totalMinDp;
        this.totalMaxDp = totalMaxDp;
        this.biaya_admin = biaya_admin;
        this.biaya_asuransi = biaya_asuransi;
        this.biaya_provisi = biaya_provisi;
        this.totalCicilan = totalCicilan;
    }

    public int getTotalMinDp() {
        return totalMinDp;
    }

    public void setTotalMinDp(int totalMinDp) {
        this.totalMinDp = totalMinDp;
    }

    public int getTotalMaxDp() {
        return totalMaxDp;
    }

    public void setTotalMaxDp(int totalMaxDp) {
        this.totalMaxDp = totalMaxDp;
    }

    public double getBiaya_admin() {
        return biaya_admin;
    }

    public void setBiaya_admin(double biaya_admin) {
        this.biaya_admin = biaya_admin;
    }

    public double getBiaya_asuransi() {
        return biaya_asuransi;
    }

    public void setBiaya_asuransi(double biaya_asuransi) {
        this.biaya_asuransi = biaya_asuransi;
    }

    public double getBiaya_provisi() {
        return biaya_provisi;
    }

    public void setBiaya_provisi(double biaya_provisi) {
        this.biaya_provisi = biaya_provisi;
    }

    public double getTotalCicilan() {
        return totalCicilan;
    }

    public void setTotalCicilan(double totalCicilan) {
        this.totalCicilan = totalCicilan;
    }
}
