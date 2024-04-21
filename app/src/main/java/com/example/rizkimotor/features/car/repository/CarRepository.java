package com.example.rizkimotor.features.car.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.remote.ApiService;
import com.example.rizkimotor.features.car.admin.model.CarComponentModel;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarRepository {
    private ApiService apiService;
    @Inject

    public CarRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ResponseModel<CarComponentModel>> getCarComponent(){
        MutableLiveData<ResponseModel<CarComponentModel>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getCarComponents().enqueue(new Callback<ResponseModel<CarComponentModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<CarComponentModel>> call, Response<ResponseModel<CarComponentModel>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(SuccessMsg.SUCCESS_STATE, SuccessMsg.SUCCESS_MSG, response.body().getData()));

                }else {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<CarComponentModel>> call, Throwable t) {
                responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE_SERVER, ErrorMsg.SERVER_ERR, null));

            }
        });

        return responseModelMutableLiveData;
    }
}
