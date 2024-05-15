package com.example.rizkimotor.features.home.admin.model;

public class FilterChartModel {
    private int data_pemasukan;
    private int data_keuntungan;


    public FilterChartModel(int data_pemasukan, int data_keuntungan) {
        this.data_pemasukan = data_pemasukan;
        this.data_keuntungan = data_keuntungan;
    }

    public int getData_pemasukan() {
        return data_pemasukan;
    }

    public void setData_pemasukan(int data_pemasukan) {
        this.data_pemasukan = data_pemasukan;
    }

    public int getData_keuntungan() {
        return data_keuntungan;
    }

    public void setData_keuntungan(int data_keuntungan) {
        this.data_keuntungan = data_keuntungan;
    }
}
