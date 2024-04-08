package com.example.rizkimotor.data.model;

import com.example.rizkimotor.data.remote.ApiService;

public class AppInfoModel {
    private int app_id;
    private String app_name;
    private String no_hp;
    private String logo;
    private String meta_token;
    private String email;
    private String alamat;
    private String url_facebook;
    private String url_instagram;
    private String url_youtube;
    private String jadwal;
    private String visi;
    private String misi;
    private String img_hero1;
    private String img_hero2;
    private String img_stempel;
    private String img_about_us1;
    private String img_about_us2;
    private String created_at;
    private String updated_at;


    public AppInfoModel(int app_id, String app_name, String no_hp, String logo, String meta_token, String email, String alamat, String url_facebook, String url_instagram, String url_youtube, String jadwal, String visi, String misi, String img_hero1, String img_hero2, String img_stempel, String img_about_us1, String img_about_us2, String created_at, String updated_at) {
        this.app_id = app_id;
        this.app_name = app_name;
        this.no_hp = no_hp;
        this.logo = logo;
        this.meta_token = meta_token;
        this.email = email;
        this.alamat = alamat;
        this.url_facebook = url_facebook;
        this.url_instagram = url_instagram;
        this.url_youtube = url_youtube;
        this.jadwal = jadwal;
        this.visi = visi;
        this.misi = misi;
        this.img_hero1 = img_hero1;
        this.img_hero2 = img_hero2;
        this.img_stempel = img_stempel;
        this.img_about_us1 = img_about_us1;
        this.img_about_us2 = img_about_us2;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getApp_id() {
        return app_id;
    }

    public void setApp_id(int app_id) {
        this.app_id = app_id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMeta_token() {
        return meta_token;
    }

    public void setMeta_token(String meta_token) {
        this.meta_token = meta_token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
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

    public String getUrl_youtube() {
        return url_youtube;
    }

    public void setUrl_youtube(String url_youtube) {
        this.url_youtube = url_youtube;
    }

    public String getJadwal() {
        return jadwal;
    }

    public void setJadwal(String jadwal) {
        this.jadwal = jadwal;
    }

    public String getVisi() {
        return visi;
    }

    public void setVisi(String visi) {
        this.visi = visi;
    }

    public String getMisi() {
        return misi;
    }

    public void setMisi(String misi) {
        this.misi = misi;
    }

    public String getImg_hero1() {
        return ApiService.END_POINT + "template/client/img/main/" + img_hero1;
    }

    public void setImg_hero1(String img_hero1) {
        this.img_hero1 = img_hero1;
    }

    public String getImg_hero2() {
        return ApiService.END_POINT + "template/client/img/main/" + img_hero2;
    }

    public void setImg_hero2(String img_hero2) {
        this.img_hero2 = img_hero2;
    }

    public String getImg_stempel() {
        return img_stempel;
    }

    public void setImg_stempel(String img_stempel) {
        this.img_stempel = img_stempel;
    }

    public String getImg_about_us1() {
        return img_about_us1;
    }

    public void setImg_about_us1(String img_about_us1) {
        this.img_about_us1 = img_about_us1;
    }

    public String getImg_about_us2() {
        return img_about_us2;
    }

    public void setImg_about_us2(String img_about_us2) {
        this.img_about_us2 = img_about_us2;
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
