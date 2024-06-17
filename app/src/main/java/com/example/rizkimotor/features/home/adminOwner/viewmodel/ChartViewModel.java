package com.example.rizkimotor.features.home.adminOwner.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.features.home.adminOwner.model.ChartModel;
import com.example.rizkimotor.features.home.adminOwner.model.FilterChartModel;
import com.example.rizkimotor.features.home.adminOwner.repository.ChartRepository;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ChartViewModel extends ViewModel {
    private ChartRepository chartRepository;
    @Inject

    public ChartViewModel(ChartRepository chartRepository) {
        this.chartRepository = chartRepository;
    }

    public LiveData<ResponseModel<ChartModel>> getChart() {
        return chartRepository.getChart();
    }

    public LiveData<ResponseModel<FilterChartModel>> filterChartProfit(String month) {
        MutableLiveData<ResponseModel<FilterChartModel>> responseModelMutableLiveData = new MutableLiveData<>();
        if (month != null && !month.isEmpty()) {
            return chartRepository.filterChartProfit(month);
        }else {
            responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, "Bulan tidak valid", null));
        }
        return responseModelMutableLiveData;
    }
}
