package com.example.rizkimotor.data.viewmodel.finance;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rizkimotor.data.model.FinanceModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.repository.finance.FinanceRepository;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;

import java.io.Closeable;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FinanceViewModel extends ViewModel {
    private FinanceRepository financeRepository;

    @Inject

    public FinanceViewModel(FinanceRepository financeRepository) {
        this.financeRepository = financeRepository;
    }

    public LiveData<ResponseModel<List<FinanceModel>>> getAllFinance() {
        return financeRepository.getAllFinance();
    }

    public LiveData<ResponseModel<FinanceModel>> getFinanceById(int id) {
        MutableLiveData<ResponseModel<FinanceModel>> responseModelMutableLiveData = new MutableLiveData<>();
        if (id != 0) {
            return financeRepository.getFinanceById(id);

        }else {
            responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, "Finance tidak ditemukan", null));
        }
        return responseModelMutableLiveData;
    }



}
