package com.example.rizkimotor.features.car.admin.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rizkimotor.data.model.CarModel;
import com.example.rizkimotor.data.model.ResponseDownloaModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.features.car.admin.repository.AdminCarRepository;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


@HiltViewModel
public class AdminCarViewModel extends ViewModel {
    private AdminCarRepository adminCarRepository;

    @Inject

    public AdminCarViewModel(AdminCarRepository admiarRepository) {
        this.adminCarRepository = admiarRepository;
    }

    public LiveData<ResponseModel> storeCar(Map<String, RequestBody> map, List<MultipartBody.Part> partList) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        if (map != null && partList != null) {
            return adminCarRepository.storeCar(map, partList);
        }else {
            responseModelMutableLiveData.postValue(new ResponseModel(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel> destroyCar(int carId) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        if (carId != 0) {
            return adminCarRepository.destroyCar(carId);
        }else {
            responseModelMutableLiveData.postValue(new ResponseModel(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }
        return responseModelMutableLiveData;
    }


    public LiveData<ResponseModel<CarModel>> getCarById(int carId) {
        MutableLiveData<ResponseModel<CarModel>> responseModelMutableLiveData = new MutableLiveData<>();
        if (carId != 0) {
            return adminCarRepository.getCarById(carId);
        }else {
            responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }

        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel> updateCar(int carId, Map<String, RequestBody> map, List<MultipartBody.Part> partList) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        if (map != null && partList != null) {
            return adminCarRepository.update(carId, map, partList);
        }else {
            responseModelMutableLiveData.postValue(new ResponseModel(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseDownloaModel> downloadCarReport(String id, HashMap map) {
        MutableLiveData<ResponseDownloaModel> responseModelMutableLiveData = new MutableLiveData<>();
        if (id != null) {
            return adminCarRepository.downloadCarReport(id, map);
        }else {
            responseModelMutableLiveData.postValue(new ResponseDownloaModel(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }

        return responseModelMutableLiveData;
    }
}
