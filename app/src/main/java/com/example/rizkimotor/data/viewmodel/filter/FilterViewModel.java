package com.example.rizkimotor.data.viewmodel.filter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rizkimotor.data.model.CarModel;
import com.example.rizkimotor.data.model.ResponseFilterModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.repository.filter.FilterRepository;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FilterViewModel extends ViewModel {
    private FilterRepository filterRepository;
    @Inject

    public FilterViewModel(FilterRepository filterRepository) {
        this.filterRepository = filterRepository;
    }

    public LiveData<ResponseFilterModel> getDataFilter() {
        return filterRepository.getFilterData();
    }

    public LiveData<ResponseModel<List<CarModel>>> carFilter(HashMap<String, String> map) {
        MutableLiveData<ResponseModel<List<CarModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        if (map != null) {
            return filterRepository.carFilter(map);
        }else {
            responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }

        return responseModelMutableLiveData;
    }
}
