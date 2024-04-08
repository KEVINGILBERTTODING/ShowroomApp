package com.example.rizkimotor.data.repository.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rizkimotor.data.model.AppInfoModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.remote.ApiService;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppRepository {
    private ApiService apiService;

    @Inject
    public AppRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ResponseModel<AppInfoModel>> getAppInfo() {
        MutableLiveData<ResponseModel<AppInfoModel>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getAppInfo().enqueue(new Callback<ResponseModel<AppInfoModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<AppInfoModel>> call, Response<ResponseModel<AppInfoModel>> response) {
                if (response.code() == 200 && response.body().getData() != null) {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(SuccessMsg.SUCCESS_MSG, SuccessMsg.SUCCESS_MSG, response.body().getData()));
                }else {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<AppInfoModel>> call, Throwable t) {
                responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SERVER_ERR, null));


            }
        });
        return responseModelMutableLiveData;
    }
}
