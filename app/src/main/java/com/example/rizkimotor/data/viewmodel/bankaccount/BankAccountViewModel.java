package com.example.rizkimotor.data.viewmodel.bankaccount;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.rizkimotor.data.model.BankAccountModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.repository.bankaccount.BankAccountRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class BankAccountViewModel extends ViewModel {
    private BankAccountRepository bankAccountRepository;

    @Inject
    public BankAccountViewModel(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public LiveData<ResponseModel<List<BankAccountModel>>> getBankAccount() {
        return bankAccountRepository.getBankAcc();
    }
}
