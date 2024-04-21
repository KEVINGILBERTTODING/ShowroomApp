package com.example.rizkimotor.features.car.admin.model;

import com.airbnb.lottie.L;
import com.example.rizkimotor.data.model.BahanBakarModel;
import com.example.rizkimotor.data.model.BodyModel;
import com.example.rizkimotor.data.model.ColorModel;
import com.example.rizkimotor.data.model.KapasitaMesinModel;
import com.example.rizkimotor.data.model.KapasitasPenumpangModel;
import com.example.rizkimotor.data.model.MerkModel;
import com.example.rizkimotor.data.model.TankModel;
import com.example.rizkimotor.data.model.TransmisiModel;

import java.util.List;

public class CarComponentModel {
    private List<MerkModel> dataMerk;
    private List<BodyModel> dataBody;
    private List<ColorModel> dataWarna;
    private List<KapasitaMesinModel> dataKapasitasMesin;
    private List<BahanBakarModel> dataBahanBakar;
    private List<TankModel> dataTangki;
    private List<TransmisiModel> dataTransmisi;
    private List<KapasitasPenumpangModel> dataKapasitasPenumpang;


    public CarComponentModel() {
    }

    public List<MerkModel> getDataMerk() {
        return dataMerk;
    }

    public void setDataMerk(List<MerkModel> dataMerk) {
        this.dataMerk = dataMerk;
    }

    public List<BodyModel> getDataBody() {
        return dataBody;
    }

    public void setDataBody(List<BodyModel> dataBody) {
        this.dataBody = dataBody;
    }

    public List<ColorModel> getDataWarna() {
        return dataWarna;
    }

    public void setDataWarna(List<ColorModel> dataWarna) {
        this.dataWarna = dataWarna;
    }

    public List<KapasitaMesinModel> getDataKapasitasMesin() {
        return dataKapasitasMesin;
    }

    public void setDataKapasitasMesin(List<KapasitaMesinModel> dataKapasitasMesin) {
        this.dataKapasitasMesin = dataKapasitasMesin;
    }

    public List<BahanBakarModel> getDataBahanBakar() {
        return dataBahanBakar;
    }

    public void setDataBahanBakar(List<BahanBakarModel> dataBahanBakar) {
        this.dataBahanBakar = dataBahanBakar;
    }

    public List<TankModel> getDataTangki() {
        return dataTangki;
    }

    public void setDataTangki(List<TankModel> dataTangki) {
        this.dataTangki = dataTangki;
    }

    public List<TransmisiModel> getDataTransmisi() {
        return dataTransmisi;
    }

    public void setDataTransmisi(List<TransmisiModel> dataTransmisi) {
        this.dataTransmisi = dataTransmisi;
    }

    public List<KapasitasPenumpangModel> getDataKapasitasPenumpang() {
        return dataKapasitasPenumpang;
    }

    public void setDataKapasitasPenumpang(List<KapasitasPenumpangModel> dataKapasitasPenumpang) {
        this.dataKapasitasPenumpang = dataKapasitasPenumpang;
    }
}
