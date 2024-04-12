package com.example.rizkimotor.data.viewmodel.car;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rizkimotor.data.model.CarModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.repository.car.CarRepository;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CarViewModel  extends ViewModel {
    private CarRepository carRepository;

    @Inject
    public CarViewModel(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public LiveData<ResponseModel<List<CarModel>>> getAllCar() {
        return carRepository.getAllCar();
    }

    public LiveData<ResponseModel<CarModel>> getDetailCar(int carId) {
        MutableLiveData<ResponseModel<CarModel>> responseModelMutableLiveData = new MutableLiveData<>();

        if (carId != 0) {
            return carRepository.getCarDetail(carId);
        }else {
            responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }
        return responseModelMutableLiveData;
    }
}
