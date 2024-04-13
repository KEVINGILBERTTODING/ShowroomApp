package com.example.rizkimotor.features.profile.user.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.remote.ApiService;
import com.example.rizkimotor.features.auth.model.user.UserModel;
import com.example.rizkimotor.util.contstans.Constants;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;
import com.google.gson.Gson;

import java.util.HashMap;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileRepository {
    private ApiService apiService;

    @Inject
    public UserProfileRepository(ApiService apiService) {
        this.apiService = apiService;
    }



    public LiveData<ResponseModel> updatePhotoProfile(RequestBody userId, MultipartBody.Part part) {


        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.updatePhotoProfile(userId, part).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    responseModelMutableLiveData.postValue(new ResponseModel(SuccessMsg.SUCCESS_STATE, response.body().getMessage(), null));
                }else {


                    responseModelMutableLiveData.postValue(new ResponseModel(ErrorMsg.ERR_STATE, response.body().getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                responseModelMutableLiveData.postValue(new ResponseModel(ErrorMsg.ERR_STATE_SERVER, ErrorMsg.SERVER_ERR, null));


            }
        });
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel<UserModel>> profile(int userId) {
        MutableLiveData<ResponseModel<UserModel>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getUserById(userId).enqueue(new Callback<ResponseModel<UserModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<UserModel>> call, Response<ResponseModel<UserModel>> response) {
                if (response != null && response.code() == 200 && response.body() != null) {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(SuccessMsg.SUCCESS_STATE, SuccessMsg.SUCCESS_MSG, response.body().getData()));
                }else {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));

                }
            }

            @Override
            public void onFailure(Call<ResponseModel<UserModel>> call, Throwable t) {
                responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE_SERVER, ErrorMsg.SERVER_ERR, null));

            }
        });
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel> updateProfile(HashMap map) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.updateUserProfile(map).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response != null && response.code() == 200 && response.body() != null) {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(SuccessMsg.SUCCESS_STATE, response.body().getMessage(), null));
                }else {
                    Log.d("TAG", "onResponse: " + response.code());
                    responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, response.body().getMessage(), null));

                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE_SERVER, ErrorMsg.SERVER_ERR, null));

            }
        });
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel> updatePassword(HashMap map) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.userUpdatePassword(map).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response != null && response.code() == 200 && response.body() != null) {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(SuccessMsg.SUCCESS_STATE, response.body().getMessage(), null));
                }else {

                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, responseModel.getMessage(), null));

                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE_SERVER, ErrorMsg.SERVER_ERR, null));

            }
        });
        return responseModelMutableLiveData;
    }
}
