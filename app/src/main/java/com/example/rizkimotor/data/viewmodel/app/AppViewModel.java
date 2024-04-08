package com.example.rizkimotor.data.viewmodel.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rizkimotor.data.model.AppInfoModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.repository.app.AppRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AppViewModel  extends ViewModel {
    private AppRepository appRepository;

    @Inject
    public AppViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }


    public LiveData<ResponseModel<AppInfoModel>> getAppInfo() {
        return  appRepository.getAppInfo();
    }
}
