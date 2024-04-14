package com.example.rizkimotor.features.transactions.user.viewmodel;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.features.transactions.user.repository.UserReviewRepository;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@HiltViewModel
public class UserReviewViewModel extends ViewModel {
    private UserReviewRepository userReviewRepository;

    @Inject

    public UserReviewViewModel(UserReviewRepository userReviewRepository) {
        this.userReviewRepository = userReviewRepository;
    }

    public LiveData<ResponseModel> storeTransaction(Map<String, RequestBody> requestBodyMap,  List<MultipartBody.Part> part) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        if (requestBodyMap != null && part != null) {
            return userReviewRepository.store(requestBodyMap, part);
        }else {
            responseModelMutableLiveData.postValue(new ResponseModel(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }

        return responseModelMutableLiveData;
    }



}
