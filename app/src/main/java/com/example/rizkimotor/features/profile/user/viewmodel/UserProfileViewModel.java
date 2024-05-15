package com.example.rizkimotor.features.profile.user.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.remote.ApiService;
import com.example.rizkimotor.data.repository.auth.AuthRepository;
import com.example.rizkimotor.features.auth.model.user.UserModel;
import com.example.rizkimotor.features.profile.user.repository.UserProfileRepository;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;

import java.io.Closeable;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@HiltViewModel
public class UserProfileViewModel extends ViewModel {
    private UserProfileRepository userProfileRepository;

    @Inject
    public UserProfileViewModel(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }



    public LiveData<ResponseModel> updatePhotoProfile(HashMap<String, RequestBody> map, MultipartBody.Part part) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        if (part != null) {
            return userProfileRepository.updatePhotoProfile(map, part);
        }else {
            responseModelMutableLiveData.postValue(new ResponseModel(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel<UserModel>> profile(String userId, int role) {
        MutableLiveData<ResponseModel<UserModel>> responseModelMutableLiveData = new MutableLiveData<>();
        if (!userId.equals("0") && role != 0) {
            return userProfileRepository.profile(userId, role);
        }else {
            responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }
        return responseModelMutableLiveData;
    }


    public LiveData<ResponseModel> updateProfile(HashMap map) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        if (map != null) {
            return userProfileRepository.updateProfile(map);
        }else {
            responseModelMutableLiveData.postValue(new ResponseModel(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel> updatePassword(HashMap map) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        if (map != null) {
            return userProfileRepository.updatePassword(map);
        }else {
            responseModelMutableLiveData.postValue(new ResponseModel(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }
        return responseModelMutableLiveData;
    }
}
