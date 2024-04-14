package com.example.rizkimotor.data.model;

public class BankAccountModel {
    private int bank_id;
    private String no_rekening;
    private String nama;
    private String bank_name;

    public BankAccountModel(int bank_id, String no_rekening, String nama, String bank_name) {
        this.bank_id = bank_id;
        this.no_rekening = no_rekening;
        this.nama = nama;
        this.bank_name = bank_name;
    }

    public int getBank_id() {
        return bank_id;
    }

    public void setBank_id(int bank_id) {
        this.bank_id = bank_id;
    }

    public String getNo_rekening() {
        return no_rekening;
    }

    public void setNo_rekening(String no_rekening) {
        this.no_rekening = no_rekening;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }
}
