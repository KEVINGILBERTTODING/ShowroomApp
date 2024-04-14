package com.example.rizkimotor.features.transactions.user.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.model.TransactionModel;
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

public class UserTransactionRepository {
    private ApiService apiService;
    @Inject

    public UserTransactionRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ResponseModel> storeTransaction(Map<String, RequestBody> requestBodyMap, MultipartBody.Part part) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.storeTransaction(requestBodyMap, part).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful() && response.code() == 200) {
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

    public LiveData<ResponseModel<List<TransactionModel>>> getHistoryTransaction(int userId, int status) {
        MutableLiveData<ResponseModel<List<TransactionModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getHistoryTransaction(userId, status).enqueue(new Callback<ResponseModel<List<TransactionModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<TransactionModel>>> call, Response<ResponseModel<List<TransactionModel>>> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    responseModelMutableLiveData.postValue(new ResponseModel<List<TransactionModel>>(SuccessMsg.SUCCESS_STATE, SuccessMsg.SUCCESS_MSG, response.body().getData()));

                }else {

                    responseModelMutableLiveData.postValue(new ResponseModel(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<TransactionModel>>> call, Throwable t) {
                responseModelMutableLiveData.postValue(new ResponseModel(ErrorMsg.ERR_STATE_SERVER, ErrorMsg.SERVER_ERR, null));

            }
        });

        return responseModelMutableLiveData;
    }
}
