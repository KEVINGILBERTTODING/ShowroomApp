package com.example.rizkimotor.data.model;

import com.example.rizkimotor.data.remote.ApiService;

public class CarModel {
    private String nama_model;
    private int mobil_id;
    private String no_plat;
    private String nama_lengkap;
    private String tahun;
    private String tgl_masuk;
    private int biaya_perbaikan;
    private String review_text;
    private String km;
    private String no_rangka;
    private int harga_jual;
    private int harga_beli;
    private int diskon;
    private String deskripsi;
    private int status_mobil;
    private String gambar1;
    private String gambar2;
    private String gambar3;
    private int star;
    private String gambar4;
    private String gambar5;
    private String gambar6;
    private String merk;
    private String tangki;
    private String body;
    private String warna;
    private String url_youtube;
    private String photo_customer;
    private String url_instagram;
    private String url_facebook;
    private String image_review1;
    private String image_review2;
    private String image_review3;
    private String bahan_bakar;
    private String image_review4;
    private String transmisi;
    private String no_mesin;
    private String nama_pemilik;
    private int tangki_id;
    private int warna_id;
    private int merk_id;
    private int body_id;
    private int km_id;
    private int bahan_bakar_id;
    private int transmisi_id;
    private int kp_id;

    private String kapasitas_mesin;
    private String kapasitas_penumpang;
    private double total_cicilan;

    public CarModel() {
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

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getMerk() {
        return merk;
    }



    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getBahan_bakar() {
        return bahan_bakar;
    }

    public void setBahan_bakar(String bahan_bakar) {
        this.bahan_bakar = bahan_bakar;
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

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTangki() {
        return tangki;
    }

    public void setTangki(String tangki) {
        this.tangki = tangki;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getWarna() {
        return warna;
    }

    public void setWarna(String warna) {
        this.warna = warna;
    }

    public String getUrl_youtube() {
        return url_youtube;
    }

    public void setUrl_youtube(String url_youtube) {
        this.url_youtube = url_youtube;
    }

    public String getUrl_instagram() {
        return url_instagram;
    }

    public void setUrl_instagram(String url_instagram) {
        this.url_instagram = url_instagram;
    }

    public String getUrl_facebook() {
        return url_facebook;
    }

    public void setUrl_facebook(String url_facebook) {
        this.url_facebook = url_facebook;
    }

    public String getImage_review1() {
        return ApiService.END_POINT  + "data/review/" +  image_review1;
    }

    public void setImage_review1(String image_review1) {
        this.image_review1 = image_review1;
    }

    public String getImage_review2() {
        return ApiService.END_POINT  + "data/review/" +  image_review2;
    }

    public void setImage_review2(String image_review2) {
        this.image_review2 = image_review2;
    }

    public String getImage_review3() {
        return ApiService.END_POINT  + "data/review/" +  image_review3;
    }

    public void setImage_review3(String image_review3) {
        this.image_review3 = image_review3;
    }

    public String getImage_review4() {
        return ApiService.END_POINT  + "data/review/" +  image_review4;
    }

    public void setImage_review4(String image_review4) {
        this.image_review4 = image_review4;
    }

    public String getKapasitas_penumpang() {
        return kapasitas_penumpang;
    }

    public void setKapasitas_penumpang(String kapasitas_penumpang) {
        this.kapasitas_penumpang = kapasitas_penumpang;
    }

    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public void setNama_lengkap(String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;
    }

    public String getReview_text() {
        return review_text;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }

    public double getTotal_cicilan() {
        return total_cicilan;
    }

    public String getPhoto_customer() {
        return ApiService.END_POINT  + "data/profile_photo/"+ photo_customer;
    }

    public void setPhoto_customer(String photo_customer) {
        this.photo_customer = photo_customer;
    }

    public void setTotal_cicilan(double total_cicilan) {
        this.total_cicilan = total_cicilan;
    }


    public String getTgl_masuk() {
        return tgl_masuk;
    }

    public void setTgl_masuk(String tgl_masuk) {
        this.tgl_masuk = tgl_masuk;
    }

    public String getNo_rangka() {
        return no_rangka;
    }

    public void setNo_rangka(String no_rangka) {
        this.no_rangka = no_rangka;
    }

    public String getNo_mesin() {
        return no_mesin;
    }

    public void setNo_mesin(String no_mesin) {
        this.no_mesin = no_mesin;
    }

    public String getNama_pemilik() {
        return nama_pemilik;
    }

    public void setNama_pemilik(String nama_pemilik) {
        this.nama_pemilik = nama_pemilik;
    }

    public int getTangki_id() {
        return tangki_id;
    }

    public void setTangki_id(int tangki_id) {
        this.tangki_id = tangki_id;
    }

    public int getWarna_id() {
        return warna_id;
    }

    public void setWarna_id(int warna_id) {
        this.warna_id = warna_id;
    }

    public int getMerk_id() {
        return merk_id;
    }

    public void setMerk_id(int merk_id) {
        this.merk_id = merk_id;
    }

    public int getBody_id() {
        return body_id;
    }

    public void setBody_id(int body_id) {
        this.body_id = body_id;
    }

    public int getKm_id() {
        return km_id;
    }

    public void setKm_id(int km_id) {
        this.km_id = km_id;
    }

    public int getBahan_bakar_id() {
        return bahan_bakar_id;
    }

    public void setBahan_bakar_id(int bahan_bakar_id) {
        this.bahan_bakar_id = bahan_bakar_id;
    }

    public int getTransmisi_id() {
        return transmisi_id;
    }

    public void setTransmisi_id(int transmisi_id) {
        this.transmisi_id = transmisi_id;
    }

    public int getKp_id() {
        return kp_id;
    }

    public void setKp_id(int kp_id) {
        this.kp_id = kp_id;
    }

    public int getHarga_beli() {
        return harga_beli;
    }

    public void setHarga_beli(int harga_beli) {
        this.harga_beli = harga_beli;
    }

    public int getBiaya_perbaikan() {
        return biaya_perbaikan;
    }

    public void setBiaya_perbaikan(int biaya_perbaikan) {
        this.biaya_perbaikan = biaya_perbaikan;
    }
}
