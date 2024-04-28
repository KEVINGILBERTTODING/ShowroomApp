package com.example.rizkimotor.features.transactions.admin.model;

import com.example.rizkimotor.data.model.CarModel;
import com.example.rizkimotor.data.model.FinanceModel;
import com.example.rizkimotor.data.model.TransactionModel;

import java.util.List;

public class ResponseAdminTransactionModel {
    private List<TransactionModel> dataTransactions;
    private List<CarModel> carModelList;
    private List<FinanceModel> financeModelList;

    public ResponseAdminTransactionModel(List<TransactionModel> dataTransactions, List<CarModel> carModelList, List<FinanceModel> financeModelList) {
        this.dataTransactions = dataTransactions;
        this.carModelList = carModelList;
        this.financeModelList = financeModelList;
    }

    public List<TransactionModel> getDataTransactions() {
        return dataTransactions;
    }

    public void setDataTransactions(List<TransactionModel> dataTransactions) {
        this.dataTransactions = dataTransactions;
    }

    public List<CarModel> getCarModelList() {
        return carModelList;
    }

    public void setCarModelList(List<CarModel> carModelList) {
        this.carModelList = carModelList;
    }

    public List<FinanceModel> getFinanceModelList() {
        return financeModelList;
    }

    public void setFinanceModelList(List<FinanceModel> financeModelList) {
        this.financeModelList = financeModelList;
    }
}
