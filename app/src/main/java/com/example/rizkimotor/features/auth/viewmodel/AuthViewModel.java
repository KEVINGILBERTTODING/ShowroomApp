package com.example.rizkimotor.features.auth.viewmodel;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.repository.auth.AuthRepository;
import com.example.rizkimotor.features.auth.model.user.UserModel;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthViewModel extends ViewModel {
    private AuthRepository authRepository;
    MutableLiveData<ResponseModel<UserModel>>  responseModelMutableLiveData  = new MutableLiveData<>();

    @Inject
    public AuthViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LiveData<ResponseModel<UserModel>> login(String email, String password) {
        if (email != null && !email.isEmpty() && password != null && !password.isEmpty()) {
            return authRepository.login(email, password);
        }else {
            responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel> register(HashMap map) {
        String email = map.get("email").toString();
        String password = map.get("password").toString();
        String fullName = map.get("nama_lengkap").toString();

        MutableLiveData<ResponseModel>  _responseModelMutableLiveData  = new MutableLiveData<>();


        if (!map.containsKey("email") || email.isEmpty()) {
            responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, "Email tidak boleh kosong", null));
        }else if (!map.containsKey("nama_lengkap") || fullName.isEmpty()) {
            responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, "Nama lengkap tidak boleh kosong", null));
        }else if (!map.containsKey("password") || password.isEmpty()) {
            responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, "Kata sandi tidak boleh kosong", null));
        }else {
            return authRepository.register(map);
        }

        return _responseModelMutableLiveData;



    }

    public LiveData<ResponseModel<UserModel>> authGoogle(HashMap<String, String> map) {
        MutableLiveData<ResponseModel<UserModel>> responseModelMutableLiveData1 = new MutableLiveData<>();
        if (map != null) {
            return authRepository.authGoogle(map);
        }else{
            responseModelMutableLiveData1.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, ErrorMsg.SERVER_ERR, null));

        }
        return responseModelMutableLiveData1;
    }

    public void signInWithGoogle( AuthCredential credential) {
        authRepository.signInWithGoogle(credential);
    }

    public void signOutGoogle() {
        authRepository.signOut();
    }



}
