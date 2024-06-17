package com.example.rizkimotor.features.home.adminOwner.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rizkimotor.data.model.ResponseDownloaModel;
import com.example.rizkimotor.data.remote.ApiService;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;

import java.util.HashMap;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionRepository {
    private ApiService apiService;

    @Inject

    public TransactionRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ResponseDownloaModel> downloadProfit(HashMap<String, String> map) {
        MutableLiveData<ResponseDownloaModel> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.downloadProfitPdf(map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.code() == 200 ) {
                    responseModelMutableLiveData.postValue(new ResponseDownloaModel(SuccessMsg.SUCCESS_STATE, SuccessMsg.SUCCESS_MSG, response.body()));

                }else {

                    responseModelMutableLiveData.postValue(new ResponseDownloaModel(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                responseModelMutableLiveData.postValue(new ResponseDownloaModel(ErrorMsg.ERR_STATE_SERVER, ErrorMsg.SERVER_ERR, null));

            }
        });

        return responseModelMutableLiveData;
    }
}
