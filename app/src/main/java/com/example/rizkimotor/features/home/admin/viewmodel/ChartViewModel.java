package com.example.rizkimotor.features.home.admin.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.features.home.admin.model.ChartModel;
import com.example.rizkimotor.features.home.admin.repository.ChartRepository;

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
}
