package com.example.rizkimotor.data.repository.filter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rizkimotor.R;
import com.example.rizkimotor.data.model.CarModel;
import com.example.rizkimotor.data.model.ResponseFilterModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.remote.ApiService;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterRepository {
    private ApiService apiService;

    @Inject

    public FilterRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ResponseFilterModel> getFilterData() {
        MutableLiveData<ResponseFilterModel> responseFilterModelMutableLiveData = new MutableLiveData<>();
        apiService.getDataFilter().enqueue(new Callback<ResponseFilterModel>() {
            @Override
            public void onResponse(Call<ResponseFilterModel> call, Response<ResponseFilterModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responseFilterModelMutableLiveData.postValue(new ResponseFilterModel(
                            SuccessMsg.SUCCESS_STATE, SuccessMsg.SUCCESS_MSG, response.body().getData_merk(),
                            response.body().getData_transmisi(),
                            response.body().getData_body()
                    ));
                }else {
                    responseFilterModelMutableLiveData.postValue(new ResponseFilterModel(
                            ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null,null,
                            null
                    ));
                }
            }

            @Override
            public void onFailure(Call<ResponseFilterModel> call, Throwable t) {
                responseFilterModelMutableLiveData.postValue(new ResponseFilterModel(
                        ErrorMsg.ERR_STATE_SERVER, ErrorMsg.SERVER_ERR, null,null,
                        null
                ));

            }
        });

        return responseFilterModelMutableLiveData;
    }

    public LiveData<ResponseModel<List<CarModel>>> carFilter(HashMap<String, String> map) {
        MutableLiveData<ResponseModel<List<CarModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.carFilter(map).enqueue(new Callback<ResponseModel<List<CarModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<CarModel>>> call, Response<ResponseModel<List<CarModel>>> response) {
                if (response != null && response.isSuccessful() && response.code() == 200) {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(SuccessMsg.SUCCESS_STATE, SuccessMsg.SUCCESS_MSG, response.body().getData()));


                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, responseModel.getMessage(), null));
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
