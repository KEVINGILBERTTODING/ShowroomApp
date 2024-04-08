package com.example.rizkimotor.data.repository.car;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rizkimotor.data.model.CarModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.remote.ApiService;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;

import java.util.List;

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

    public LiveData<ResponseModel<List<CarModel>>> getAllCar() {
        MutableLiveData<ResponseModel<List<CarModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getAllCar().enqueue(new Callback<ResponseModel<List<CarModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<CarModel>>> call, Response<ResponseModel<List<CarModel>>> response) {
                if (response.code() == 200 && response.body().getMessage().equals(SuccessMsg.SUCCESS_RESPONSE) ) {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(SuccessMsg.SUCCESS_STATE, SuccessMsg.SUCCESS_MSG, response.body().getData()));
                }else {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));

                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<CarModel>>> call, Throwable t) {
                responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.SERVER_ERR, ErrorMsg.SERVER_ERR, null));
            }
        });
        return responseModelMutableLiveData;
    }
}
