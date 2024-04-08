package com.example.rizkimotor.data.model;

import com.example.rizkimotor.data.remote.ApiService;

public class CarModel {
    private String nama_model;
    private int mobil_id;
    private String no_plat;
    private String tahun;
    private String km;
    private int harga_jual;
    private int diskon;
    private int status_mobil;
    private String gambar1;
    private String gambar2;
    private String gambar3;
    private String gambar4;
    private String gambar5;
    private String gambar6;
    private String merk;
    private String transmisi;
    private String kapasitas_mesin;
    private double total_cicilan;

    public CarModel(String nama_model, int mobil_id, String no_plat, String tahun, String km, int harga_jual, int diskon, int status_mobil, String gambar1, String gambar2, String gambar3, String gambar4, String gambar5, String gambar6, String merk, String transmisi, String kapasitas_mesin, double total_cicilan) {
        this.nama_model = nama_model;
        this.mobil_id = mobil_id;
        this.no_plat = no_plat;
        this.tahun = tahun;
        this.km = km;
        this.harga_jual = harga_jual;
        this.diskon = diskon;
        this.status_mobil = status_mobil;
        this.gambar1 = gambar1;
        this.gambar2 = gambar2;
        this.gambar3 = gambar3;
        this.gambar4 = gambar4;
        this.gambar5 = gambar5;
        this.gambar6 = gambar6;
        this.merk = merk;
        this.transmisi = transmisi;
        this.kapasitas_mesin = kapasitas_mesin;
        this.total_cicilan = total_cicilan;
    }

    public String getNama_model() {
        return nama_model;
    }

    public void setNama_model(String nama_model) {
        this.nama_model = nama_model;
    }

    public int getMobil_id() {
        return mobil_id;
    }

    public void setMobil_id(int mobil_id) {
        this.mobil_id = mobil_id;
    }

    public String getNo_plat() {
        return no_plat;
    }

    public void setNo_plat(String no_plat) {
        this.no_plat = no_plat;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public int getHarga_jual() {
        return harga_jual;
    }

    public void setHarga_jual(int harga_jual) {
        this.harga_jual = harga_jual;
    }

    public int getDiskon() {
        return diskon;
    }

    public void setDiskon(int diskon) {
        this.diskon = diskon;
    }

    public int getStatus_mobil() {
        return status_mobil;
    }

    public void setStatus_mobil(int status_mobil) {
        this.status_mobil = status_mobil;
    }

    public String getGambar1() {
        if (gambar1 != null) {
            return ApiService.END_POINT + "data/cars/" + gambar1;
        }else {
            return null;

        }
    }

    public void setGambar1(String gambar1) {

        this.gambar1 = gambar1;
    }

    public String getGambar2() {
        if (gambar2 != null) {
            return ApiService.END_POINT + "data/cars/" + gambar2;
        }else {
            return null;

        }
    }

    public void setGambar2(String gambar2) {
        this.gambar2 = gambar2;
    }

    public String getGambar3() {
        if (gambar3 != null) {
            return ApiService.END_POINT + "data/cars/" + gambar3;
        }else {
            return null;

        }
    }

    public void setGambar3(String gambar3) {
        this.gambar3 = gambar3;
    }

    public String getGambar4() {
        if (gambar4 != null) {
            return ApiService.END_POINT + "data/cars/" + gambar4;
        }else {
            return null;
        }



    }

    public void setGambar4(String gambar4) {
        this.gambar4 = gambar4;
    }

    public String getGambar5() {
        if (gambar5 != null) {
            return ApiService.END_POINT + "data/cars/" + gambar5;
        }else {
            return null;

        }

    }

    public void setGambar5(String gambar5) {
        this.gambar5 = gambar5;
    }

    public String getGambar6() {
        if (gambar6 != null) {
            return ApiService.END_POINT + "data/cars/" + gambar6;
        }else {
            return null;

        }
    }

    public void setGambar6(String gambar6) {
        this.gambar6 = gambar6;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getTransmisi() {
        return transmisi;
    }

    public void setTransmisi(String transmisi) {
        this.transmisi = transmisi;
    }

    public String getKapasitas_mesin() {
        return kapasitas_mesin;
    }

    public void setKapasitas_mesin(String kapasitas_mesin) {
        this.kapasitas_mesin = kapasitas_mesin;
    }

    public double getTotal_cicilan() {
        return total_cicilan;
    }

    public void setTotal_cicilan(double total_cicilan) {
        this.total_cicilan = total_cicilan;
    }
}
