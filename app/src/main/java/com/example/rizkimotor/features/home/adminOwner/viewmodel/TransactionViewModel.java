package com.example.rizkimotor.features.home.adminOwner.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rizkimotor.data.model.ResponseDownloaModel;
import com.example.rizkimotor.features.home.adminOwner.repository.TransactionRepository;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;

import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TransactionViewModel extends ViewModel {
    private TransactionRepository transactionRepository;
    @Inject

    public TransactionViewModel(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public LiveData<ResponseDownloaModel> downloadProfit(HashMap<String, String> map) {
        MutableLiveData<ResponseDownloaModel> responseDownloaModelMutableLiveData = new MutableLiveData<>();
        if (map != null) {
            return transactionRepository.downloadProfit(map);
        }else {
            responseDownloaModelMutableLiveData.postValue(new ResponseDownloaModel(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }

        return responseDownloaModelMutableLiveData;
    }

}
