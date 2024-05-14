package com.example.rizkimotor.features.transactions.admin.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rizkimotor.data.model.ResponseDownloaModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.model.TransactionModel;
import com.example.rizkimotor.data.remote.ApiService;
import com.example.rizkimotor.features.transactions.admin.model.ResponseAdminTransactionModel;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminTransactionRepository {
    private ApiService apiService;
    @Inject

    public AdminTransactionRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ResponseModel<ResponseAdminTransactionModel>> getTransaction(int status) {
        MutableLiveData<ResponseModel<ResponseAdminTransactionModel>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.adminGetTransaction(status).enqueue(new Callback<ResponseModel<ResponseAdminTransactionModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<ResponseAdminTransactionModel>> call, Response<ResponseModel<ResponseAdminTransactionModel>> response) {
                if (response != null && response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(SuccessMsg.SUCCESS_STATE, SuccessMsg.SUCCESS_MSG, response.body().getData()));
                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);

                    responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ResponseAdminTransactionModel>> call, Throwable t) {
                responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE_SERVER, ErrorMsg.SERVER_ERR, null));


            }
        });
        return responseModelMutableLiveData;
    }


    public LiveData<ResponseModel> deleteTransaction(String transactionId, int payment) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.deleteTransaction(transactionId, payment).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response != null && response.isSuccessful() && response.code() == 200) {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(SuccessMsg.SUCCESS_STATE, response.body().getMessage(),null));
                }else {


                    responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE_SERVER, ErrorMsg.SERVER_ERR, null));


            }
        });
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel> updateStatusTransaction(String transactionId, HashMap<String, String> map) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.updateStatusTransaction(transactionId, map).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response != null && response.isSuccessful() && response.code() == 200) {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(SuccessMsg.SUCCESS_STATE, response.body().getMessage(),null));
                }else {

                    Log.d("TAG", "onResponse: " + response.code());


                    responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE_SERVER, ErrorMsg.SERVER_ERR, null));


            }
        });
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel<List<TransactionModel>>> filterTransaction(HashMap<String, String> map) {
        MutableLiveData<ResponseModel<List<TransactionModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.filterTransaction(map).enqueue(new Callback<ResponseModel<List<TransactionModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<TransactionModel>>> call, Response<ResponseModel<List<TransactionModel>>> response) {
                if (response != null && response.isSuccessful() && response.code() == 200) {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(SuccessMsg.SUCCESS_STATE, SuccessMsg.SUCCESS_MSG, response.body().getData()));
                }else {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG,null));

                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<TransactionModel>>> call, Throwable t) {
                responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG,null));



            }
        });
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseDownloaModel> downloadTransReport(HashMap<String, String> map) {
        MutableLiveData<ResponseDownloaModel> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.downloadReportTransaction(map).enqueue(new Callback<ResponseBody>() {
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
