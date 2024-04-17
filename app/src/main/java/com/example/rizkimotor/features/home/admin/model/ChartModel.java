package com.example.rizkimotor.features.home.admin.model;

public class ChartModel {
    private MonthModel data_pemasukan;
    private MonthModel data_keuntungan;
    private MonthModel data_trans_selesai;
    private MonthModel data_trans_proses;
    private MonthModel data_trans_proses_finance;
    private MonthModel data_trans_proses_tidak_valid;
    private String year_now;
    private String month_now;

    public ChartModel() {
    }

    public MonthModel getData_pemasukan() {
        return data_pemasukan;
    }

    public void setData_pemasukan(MonthModel data_pemasukan) {
        this.data_pemasukan = data_pemasukan;
    }

    public MonthModel getData_keuntungan() {
        return data_keuntungan;
    }

    public void setData_keuntungan(MonthModel data_keuntungan) {
        this.data_keuntungan = data_keuntungan;
    }

    public MonthModel getData_trans_selesai() {
        return data_trans_selesai;
    }

    public void setData_trans_selesai(MonthModel data_trans_selesai) {
        this.data_trans_selesai = data_trans_selesai;
    }

    public MonthModel getData_trans_proses() {
        return data_trans_proses;
    }

    public void setData_trans_proses(MonthModel data_trans_proses) {
        this.data_trans_proses = data_trans_proses;
    }

    public MonthModel getData_trans_proses_finance() {
        return data_trans_proses_finance;
    }

    public void setData_trans_proses_finance(MonthModel data_trans_proses_finance) {
        this.data_trans_proses_finance = data_trans_proses_finance;
    }

    public MonthModel getData_trans_proses_tidak_valid() {
        return data_trans_proses_tidak_valid;
    }

    public void setData_trans_proses_tidak_valid(MonthModel data_trans_proses_tidak_valid) {
        this.data_trans_proses_tidak_valid = data_trans_proses_tidak_valid;
    }

    public String getYear_now() {
        return year_now;
    }

    public void setYear_now(String year_now) {
        this.year_now = year_now;
    }

    public String getMonth_now() {
        return month_now;
    }

    public void setMonth_now(String month_now) {
        this.month_now = month_now;
    }
}
