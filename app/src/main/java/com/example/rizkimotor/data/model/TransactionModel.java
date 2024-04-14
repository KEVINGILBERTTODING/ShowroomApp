package com.example.rizkimotor.data.model;

import com.example.rizkimotor.data.remote.ApiService;

public class TransactionModel {
    private String transaksi_id;
    private int mobil_id;
    private int user_id;
    private Integer pelanggan_id; // Gunakan Integer jika nilainya bisa null
    private String payment_method;
    private long total_pembayaran;
    private int status;
    private String alasan; // Gunakan String jika nilainya bisa null
    private String bukti_pembayaran; // Gunakan String jika nilainya bisa null
    private long biaya_pengiriman;
    private String created_at;
    private String updated_at;
    private String nama_user;
    private String no_hp_user;
    private String alamat_user;
    private String nama_finance;
    private int finance_id;
    private String nama_model;
    private String tahun;
    private String gambar1;
    private String merk;
    private String review_text;

    public TransactionModel() {
    }

    public String getTransaksi_id() {
        return transaksi_id;
    }

    public void setTransaksi_id(String transaksi_id) {
        this.transaksi_id = transaksi_id;
    }

    public int getMobil_id() {
        return mobil_id;
    }

    public void setMobil_id(int mobil_id) {
        this.mobil_id = mobil_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Integer getPelanggan_id() {
        return pelanggan_id;
    }

    public void setPelanggan_id(Integer pelanggan_id) {
        this.pelanggan_id = pelanggan_id;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public long getTotal_pembayaran() {
        return total_pembayaran;
    }

    public void setTotal_pembayaran(long total_pembayaran) {
        this.total_pembayaran = total_pembayaran;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAlasan() {
        return alasan;
    }

    public void setAlasan(String alasan) {
        this.alasan = alasan;
    }

    public String getBukti_pembayaran() {
        return bukti_pembayaran;
    }

    public void setBukti_pembayaran(String bukti_pembayaran) {
        this.bukti_pembayaran = bukti_pembayaran;
    }

    public long getBiaya_pengiriman() {
        return biaya_pengiriman;
    }

    public void setBiaya_pengiriman(long biaya_pengiriman) {
        this.biaya_pengiriman = biaya_pengiriman;
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

    public String getNama_user() {
        return nama_user;
    }

    public void setNama_user(String nama_user) {
        this.nama_user = nama_user;
    }

    public String getNo_hp_user() {
        return no_hp_user;
    }

    public void setNo_hp_user(String no_hp_user) {
        this.no_hp_user = no_hp_user;
    }

    public String getAlamat_user() {
        return alamat_user;
    }

    public void setAlamat_user(String alamat_user) {
        this.alamat_user = alamat_user;
    }

    public String getNama_finance() {
        return nama_finance;
    }

    public void setNama_finance(String nama_finance) {
        this.nama_finance = nama_finance;
    }

    public int getFinance_id() {
        return finance_id;
    }

    public void setFinance_id(int finance_id) {
        this.finance_id = finance_id;
    }

    public String getNama_model() {
        return nama_model;
    }

    public void setNama_model(String nama_model) {
        this.nama_model = nama_model;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

    public String getGambar1() {
        return ApiService.END_POINT + "data/cars/" +  gambar1;
    }

    public void setGambar1(String gambar1) {
        this.gambar1 = gambar1;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getReview_text() {
        return review_text;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }
}
