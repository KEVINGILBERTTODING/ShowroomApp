package com.example.rizkimotor.data.repository.bankaccount;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rizkimotor.data.model.BankAccountModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.remote.ApiService;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BankAccountRepository {
    private ApiService apiService;

    @Inject
    public BankAccountRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ResponseModel<List<BankAccountModel>>> getBankAcc() {
        MutableLiveData<ResponseModel<List<BankAccountModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getBankAcc().enqueue(new Callback<ResponseModel<List<BankAccountModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<BankAccountModel>>> call, Response<ResponseModel<List<BankAccountModel>>> response) {
                if (response.isSuccessful() && response.code() == 200 && response.body() != null) {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(SuccessMsg.SUCCESS_STATE, SuccessMsg.SUCCESS_MSG, response.body().getData()));
                }else {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<BankAccountModel>>> call, Throwable t) {
                responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE_SERVER, ErrorMsg.SERVER_ERR, null));

            }
        });

        return responseModelMutableLiveData;
    }
}
