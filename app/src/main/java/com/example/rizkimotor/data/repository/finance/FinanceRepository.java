package com.example.rizkimotor.data.repository.finance;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rizkimotor.data.model.FinanceModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.remote.ApiService;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinanceRepository {
    private ApiService apiService;

    @Inject

    public FinanceRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ResponseModel<List<FinanceModel>>> getAllFinance() {
        MutableLiveData<ResponseModel<List<FinanceModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getAllFinance().enqueue(new Callback<ResponseModel<List<FinanceModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<FinanceModel>>> call, Response<ResponseModel<List<FinanceModel>>> response) {
                if (response.code() == 200 && response.body().getData() != null) {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(SuccessMsg.SUCCESS_STATE, SuccessMsg.SUCCESS_MSG, response.body().getData()));
                }else {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<FinanceModel>>> call, Throwable t) {
                responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE_SERVER, ErrorMsg.SERVER_ERR, null));
                Log.d("TAG", "onFailure: " + t.getMessage());

            }
        });
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel<FinanceModel>> getFinanceById(int id) {
        MutableLiveData<ResponseModel<FinanceModel>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getFinanceById(id).enqueue(new Callback<ResponseModel<FinanceModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<FinanceModel>> call, Response<ResponseModel<FinanceModel>> response) {
                if (response.code() == 200 && response.body().getData() != null) {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(SuccessMsg.SUCCESS_STATE, SuccessMsg.SUCCESS_MSG, response.body().getData()));
                }else {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<FinanceModel>> call, Throwable t) {
                responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE_SERVER, ErrorMsg.SERVER_ERR, null));
                Log.d("TAG", "onFailure: " + t.getMessage());

            }
        });
        return responseModelMutableLiveData;
    }
}
