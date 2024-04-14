package com.example.rizkimotor.features.transactions.user.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rizkimotor.data.model.ResponseDownloaModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.model.TransactionModel;
import com.example.rizkimotor.features.transactions.user.repository.UserTransactionRepository;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@HiltViewModel
public class UserTransactionViewModel extends ViewModel {
    private UserTransactionRepository userTransactionRepository;
    @Inject

    public UserTransactionViewModel(UserTransactionRepository userTransactionRepository) {
        this.userTransactionRepository = userTransactionRepository;
    }

    public LiveData<ResponseModel> storeTransaction(Map<String, RequestBody> requestBodyMap, MultipartBody.Part part) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        if (requestBodyMap != null && part != null) {
            return userTransactionRepository.storeTransaction(requestBodyMap, part);
        }else {
            responseModelMutableLiveData.postValue(new ResponseModel(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }

        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel<List<TransactionModel>>> historyTransaction(int userId, int status) {
        MutableLiveData<ResponseModel<List<TransactionModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        if (userId != 0) {
            return userTransactionRepository.getHistoryTransaction(userId, status);
        }else {
            responseModelMutableLiveData.postValue(new ResponseModel(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }

        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel<TransactionModel>> getTransactionDetail(String id) {
        MutableLiveData<ResponseModel<TransactionModel>> responseModelMutableLiveData = new MutableLiveData<>();
        if (id != null) {
            return userTransactionRepository.getTransactionById(id);
        }else {
            responseModelMutableLiveData.postValue(new ResponseModel(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }

        return responseModelMutableLiveData;
    }
    public LiveData<ResponseDownloaModel> downloadInvoice(String id) {
        MutableLiveData<ResponseDownloaModel> responseModelMutableLiveData = new MutableLiveData<>();
        if (id != null) {
            return userTransactionRepository.downloadInvoice(id);
        }else {
            responseModelMutableLiveData.postValue(new ResponseDownloaModel(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }

        return responseModelMutableLiveData;
    }
}
