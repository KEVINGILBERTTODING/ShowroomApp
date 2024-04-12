package com.example.rizkimotor.data.model;

import com.example.rizkimotor.data.remote.ApiService;

public class FinanceModel {
    private int finance_id;
    private String nama_finance;
    private double bunga;
    private double biaya_asuransi;
    private double biaya_administrasi;
    private double uang_muka;
    private double biaya_provisi;
    private String deskripsi;
    private String url_website;
    private String url_facebook;
    private String url_instagram;
    private String telepon;
    private String email;
    private String image;
    private int status;
    private String created_at;
    private String updated_at;


    public FinanceModel(int finance_id, String nama_finance, double bunga, double biaya_asuransi, double biaya_administrasi, double uang_muka, double biaya_provisi, String deskripsi, String url_website, String url_facebook, String url_instagram, String telepon, String email, String image, int status, String created_at, String updated_at) {
        this.finance_id = finance_id;
        this.nama_finance = nama_finance;
        this.bunga = bunga;
        this.biaya_asuransi = biaya_asuransi;
        this.biaya_administrasi = biaya_administrasi;
        this.uang_muka = uang_muka;
        this.biaya_provisi = biaya_provisi;
        this.deskripsi = deskripsi;
        this.url_website = url_website;
        this.url_facebook = url_facebook;
        this.url_instagram = url_instagram;
        this.telepon = telepon;
        this.email = email;
        this.image = image;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getFinance_id() {
        return finance_id;
    }

    public void setFinance_id(int finance_id) {
        this.finance_id = finance_id;
    }

    public String getNama_finance() {
        return nama_finance;
    }

    public void setNama_finance(String nama_finance) {
        this.nama_finance = nama_finance;
    }

    public double getBunga() {
        return bunga;
    }

    public void setBunga(double bunga) {
        this.bunga = bunga;
    }

    public double getBiaya_asuransi() {
        return biaya_asuransi;
    }

    public void setBiaya_asuransi(double biaya_asuransi) {
        this.biaya_asuransi = biaya_asuransi;
    }

    public double getBiaya_administrasi() {
        return biaya_administrasi;
    }

    public void setBiaya_administrasi(double biaya_administrasi) {
        this.biaya_administrasi = biaya_administrasi;
    }

    public double getUang_muka() {
        return uang_muka;
    }

    public void setUang_muka(double uang_muka) {
        this.uang_muka = uang_muka;
    }

    public double getBiaya_provisi() {
        return biaya_provisi;
    }

    public void setBiaya_provisi(double biaya_provisi) {
        this.biaya_provisi = biaya_provisi;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getUrl_website() {
        return url_website;
    }

    public void setUrl_website(String url_website) {
        this.url_website = url_website;
    }

    public String getUrl_facebook() {
        return url_facebook;
    }

    public void setUrl_facebook(String url_facebook) {
        this.url_facebook = url_facebook;
    }

    public String getUrl_instagram() {
        return url_instagram;
    }

    public void setUrl_instagram(String url_instagram) {
        this.url_instagram = url_instagram;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return ApiService.END_POINT + "data/finance/img/" + image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
