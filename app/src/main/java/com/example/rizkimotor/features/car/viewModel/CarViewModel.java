package com.example.rizkimotor.features.car.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.features.car.admin.model.CarComponentModel;
import com.example.rizkimotor.features.car.repository.CarRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CarViewModel extends ViewModel {
    private CarRepository carRepository;
    @Inject

    public CarViewModel(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public LiveData<ResponseModel<CarComponentModel>> getCarComponent() {
        return carRepository.getCarComponent();
    }
}
