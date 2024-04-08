package com.example.rizkimotor.features.auth.ui.model;

public class UserModel {
    private int user_id;
    private String email;
    private String profile_photo;
    private String nama_lengkap;

    public UserModel(int user_id, String email, String profile_photo, String nama_lengkap) {
        this.user_id = user_id;
        this.email = email;
        this.profile_photo = profile_photo;
        this.nama_lengkap = nama_lengkap;
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
        return profile_photo;
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
}
