package com.example.rizkimotor.data.viewmodel.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rizkimotor.data.model.CarModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.repository.search.SearchRepository;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SearchViewModel extends ViewModel {
    private SearchRepository searchRepository;


    @Inject
    public SearchViewModel(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    public LiveData<ResponseModel<List<CarModel>>> searchCar(String query) {
        MutableLiveData<ResponseModel<List<CarModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        if (query != null && !query.isEmpty()) {
            return searchRepository.carSearch(query);
        }else {
            responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }

        return responseModelMutableLiveData;
    }
}
