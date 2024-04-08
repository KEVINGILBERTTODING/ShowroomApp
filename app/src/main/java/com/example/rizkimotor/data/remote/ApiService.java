package com.example.rizkimotor.data.remote;

import com.example.rizkimotor.data.model.AppInfoModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.features.auth.ui.model.UserModel;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    public static final String IP_ADDRESS = "192.168.43.215";
    public static final String BASE_URL = "http://" + IP_ADDRESS + ":8000/api/";
    public static final String END_POINT =  "http://" + IP_ADDRESS + ":8000/";

    @FormUrlEncoded
    @POST("auth/login")
    Call<ResponseModel<UserModel>> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("auth/register")
    Call<ResponseModel> registerUser(
            @FieldMap HashMap<String, String> data
            );

    @GET("client/main")
    Call<ResponseModel<AppInfoModel>> getAppInfo();
}
