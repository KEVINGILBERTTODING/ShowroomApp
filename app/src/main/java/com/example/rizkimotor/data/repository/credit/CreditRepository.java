package com.example.rizkimotor.data.repository.credit;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rizkimotor.data.model.CreditModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.remote.ApiService;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreditRepository {
    private ApiService apiService;

    @Inject

    public CreditRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ResponseModel<CreditModel>> getDp(int mobilId, int financeId) {
        MutableLiveData<ResponseModel<CreditModel>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getDP(mobilId, financeId).enqueue(new Callback<ResponseModel<CreditModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<CreditModel>> call, Response<ResponseModel<CreditModel>> response) {
                if (response.code() == 200 && response.body().getData() != null) {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(SuccessMsg.SUCCESS_STATE, SuccessMsg.SUCCESS_MSG, response.body().getData()));
                }else {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG,null));

                }
            }

            @Override
            public void onFailure(Call<ResponseModel<CreditModel>> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE_SERVER, ErrorMsg.SERVER_ERR,null));


            }
        });

        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel<CreditModel>> countCredit(HashMap map) {
        MutableLiveData<ResponseModel<CreditModel>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.countCredit(map).enqueue(new Callback<ResponseModel<CreditModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<CreditModel>> call, Response<ResponseModel<CreditModel>> response) {
                if (response.code() == 200 && response.body().getData() != null) {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(SuccessMsg.SUCCESS_STATE, SuccessMsg.SUCCESS_MSG, response.body().getData()));
                }else {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG,null));

                }
            }

            @Override
            public void onFailure(Call<ResponseModel<CreditModel>> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE_SERVER, ErrorMsg.SERVER_ERR,null));


            }
        });

        return responseModelMutableLiveData;
    }


    public LiveData<ResponseModel> uploadFileCredit(Map<String, RequestBody> map, List<MultipartBody.Part> partMap) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.sendCredit(map, partMap).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful() && response != null && response.code() == 200) {
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

            }
        });

        return responseModelMutableLiveData;
    }


}
