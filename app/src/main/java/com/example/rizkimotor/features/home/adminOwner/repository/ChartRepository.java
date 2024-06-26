package com.example.rizkimotor.features.home.adminOwner.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.remote.ApiService;
import com.example.rizkimotor.features.home.adminOwner.model.ChartModel;
import com.example.rizkimotor.features.home.adminOwner.model.FilterChartModel;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartRepository {
    private ApiService apiService;

    @Inject

    public ChartRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ResponseModel<ChartModel>> getChart() {
        MutableLiveData<ResponseModel<ChartModel>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getChart().enqueue(new Callback<ResponseModel<ChartModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<ChartModel>> call, Response<ResponseModel<ChartModel>> response) {
                if (response.isSuccessful() && response.code() == 200 && response.body() != null ) {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(SuccessMsg.SUCCESS_STATE, SuccessMsg.SUCCESS_MSG, response.body().getData()));

                }else {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ChartModel>> call, Throwable t) {
                responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE_SERVER, ErrorMsg.SERVER_ERR, null));

            }
        });
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel<FilterChartModel>> filterChartProfit(String month) {
        MutableLiveData<ResponseModel<FilterChartModel>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.filterChartProfit(month).enqueue(new Callback<ResponseModel<FilterChartModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<FilterChartModel>> call, Response<ResponseModel<FilterChartModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("Error filter", "onResponse: " + response.body());

                    responseModelMutableLiveData.postValue(new ResponseModel<>(SuccessMsg.SUCCESS_STATE, SuccessMsg.SUCCESS_MSG, response.body().getData()));

                }else {
                    Log.d("Error filter", "onResponse: " + "ASASSASSASAS");
                    responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<FilterChartModel>> call, Throwable t) {
                Log.d("Error filter", "onResponse: " + t.getMessage());


                responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE_SERVER, ErrorMsg.ERR_STATE_SERVER, null));

            }

        });

        return responseModelMutableLiveData;
    }
}
