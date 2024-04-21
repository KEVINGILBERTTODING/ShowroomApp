package com.example.rizkimotor.features.car.admin.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rizkimotor.R;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.remote.ApiService;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminCarRepository {
    private ApiService apiService;
    @Inject

    public AdminCarRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ResponseModel> storeCar(Map<String, RequestBody> map, List<MultipartBody.Part> partList) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.storeCar(map, partList).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response != null && response.isSuccessful() && response.code() == 200 &&
                response.body() != null) {
                    responseModelMutableLiveData.postValue(new ResponseModel(SuccessMsg.SUCCESS_STATE, SuccessMsg.SUCCESS_MSG, null));

                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.postValue(new ResponseModel(ErrorMsg.ERR_STATE, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                responseModelMutableLiveData.postValue(new ResponseModel(ErrorMsg.ERR_STATE_SERVER, ErrorMsg.SERVER_ERR, null));
                Log.d("ERROR", "onFailure: " + t.getMessage());

            }
        });

        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel> destroyCar(int id) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.destroyCar(id).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response != null && response.isSuccessful() && response.code() == 200) {
                    responseModelMutableLiveData.postValue(new ResponseModel(SuccessMsg.SUCCESS_STATE, SuccessMsg.SUCCESS_MSG, null));

                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.postValue(new ResponseModel(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                responseModelMutableLiveData.postValue(new ResponseModel(ErrorMsg.ERR_STATE_SERVER, ErrorMsg.SERVER_ERR, null));

            }
        });

        return responseModelMutableLiveData;
    }
}
