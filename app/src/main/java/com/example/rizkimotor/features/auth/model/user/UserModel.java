package com.example.rizkimotor.features.auth.model.user;

import com.example.rizkimotor.data.remote.ApiService;

public class UserModel {
    private int user_id;
    private String email;
    private String profile_photo;
    private String photo_profile;
    private String name;
    private String nama_lengkap;
    private String no_hp;
    private String alamat;
    private String kota;
    private String provinsi;
    private int role;
    private String sign_in;
    private int status;


    public UserModel(int user_id, String email, String profile_photo, String photo_profile, String name, String nama_lengkap, String no_hp, String alamat, String kota, String provinsi, int role, String sign_in, int status) {
        this.user_id = user_id;
        this.email = email;
        this.profile_photo = profile_photo;
        this.photo_profile = photo_profile;
        this.name = name;
        this.nama_lengkap = nama_lengkap;
        this.no_hp = no_hp;
        this.alamat = alamat;
        this.kota = kota;
        this.provinsi = provinsi;
        this.role = role;
        this.sign_in = sign_in;
        this.status = status;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_photo() {
        if (sign_in != null && sign_in.equals("google")) { // jika login google
            return  profile_photo;
        }
        return ApiService.END_POINT + "data/profile_photo/" + profile_photo;
    }

    public String getPhoto_profile() {


            return ApiService.END_POINT + "data/profile_photo/" + photo_profile;




    }


    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public void setNama_lengkap(String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getSign_in() {
        return sign_in;
    }

    public void setSign_in(String sign_in) {
        this.sign_in = sign_in;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
