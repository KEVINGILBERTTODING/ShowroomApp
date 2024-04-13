package com.example.rizkimotor.features.transactions.user.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rizkimotor.data.model.CreditModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.repository.credit.CreditRepository;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@HiltViewModel
public class UserCreditViewModel extends ViewModel {
    private CreditRepository creditRepository;

    @Inject

    public UserCreditViewModel(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    public LiveData<ResponseModel<CreditModel>> getDp(int mobilId, int financeId) {
        MutableLiveData<ResponseModel<CreditModel>> responseModelMutableLiveData = new MutableLiveData<>();
        if (mobilId != 0 && financeId != 0) {
            return creditRepository.getDp(mobilId, financeId);
        }else {
            responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel<CreditModel>> countCredit(HashMap map) {
        MutableLiveData<ResponseModel<CreditModel>> responseModelMutableLiveData = new MutableLiveData<>();
        if ((int) map.get("finance_id") == 0) {
            responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, "Finance tidak ditemukan", null));

        }else  if ((int) map.get("total_pembayaran") == 0) {
            responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, "Harga mobil tidak valid", null));

        }else  if ((int) map.get("total_dp") == 0) {
            responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, "Jumlah DP yang dimasukkan tidak valid", null));

        }else  if ((int) map.get("durasi") == 0) {
            responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, "Lama pinjaman tidak valid", null));
        }else {
            return creditRepository.countCredit(map);
        }
        return responseModelMutableLiveData;
    }



    public LiveData<ResponseModel> sendCreditRequest(Map<String, RequestBody> map, List<MultipartBody.Part> partMap) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        if (map != null) {
            return creditRepository.uploadFileCredit(map, partMap);
        }else {
            responseModelMutableLiveData.postValue(new ResponseModel(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }
        return responseModelMutableLiveData;
    }





}
