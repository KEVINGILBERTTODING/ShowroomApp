package com.example.rizkimotor.features.transactions.admin.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.features.transactions.admin.model.ResponseAdminTransactionModel;
import com.example.rizkimotor.features.transactions.admin.repositories.AdminTransactionRepository;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;

import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AdminTransactionViewModel extends ViewModel {
    private AdminTransactionRepository adminTransactionRepository;
    @Inject

    public AdminTransactionViewModel(AdminTransactionRepository adminTransactionRepository) {
        this.adminTransactionRepository = adminTransactionRepository;
    }

    public LiveData<ResponseModel<ResponseAdminTransactionModel>> getTransaction(int status) {
        MutableLiveData<ResponseModel<ResponseAdminTransactionModel>> responseModelMutableLiveData = new MutableLiveData<>();
        if (status <=4) {
            return adminTransactionRepository.getTransaction(status);
        }else {
            responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel> deleteTransaction(String transactionId, int payment) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        if (transactionId != null && payment <=3) {
            return adminTransactionRepository.deleteTransaction(transactionId, payment);
        }else {
            responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }
        return responseModelMutableLiveData;
    }
    public LiveData<ResponseModel> updateStatusTransaction(String transactionId, HashMap<String, String> map) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        if (transactionId != null && map != null) {
            return adminTransactionRepository.updateStatusTransaction(transactionId, map);
        }else {
            responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }
        return responseModelMutableLiveData;
    }

}
