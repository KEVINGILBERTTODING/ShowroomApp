package com.example.rizkimotor.data.repository.auth;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.remote.ApiService;
import com.example.rizkimotor.features.auth.model.user.UserModel;
import com.example.rizkimotor.util.contstans.Constants;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private ApiService apiService;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private MutableLiveData<ResponseModel<FirebaseUser>> userModelMutableLiveData = new MutableLiveData<>();

    @Inject
    public AuthRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ResponseModel<UserModel>> login(String email, String password) {
        MutableLiveData<ResponseModel<UserModel>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.loginUser(email, password).enqueue(new Callback<ResponseModel<UserModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<UserModel>> call, Response<ResponseModel<UserModel>> response) {
                if (response.code() == 201 && response.body().getData() != null) {

                    responseModelMutableLiveData.postValue(new ResponseModel<>(SuccessMsg.SUCCESS_MSG, response.body().getMessage(), response.body().getData()));
                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);


                    responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, responseModel.getMessage(), null));

                }
            }

            @Override
            public void onFailure(Call<ResponseModel<UserModel>> call, Throwable t) {
                responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE_SERVER, t.getMessage(), null));

            }

        });
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel> register(HashMap map) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        try {
            apiService.registerUser(map).enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.code() == 201 ) {

                        responseModelMutableLiveData.postValue(new ResponseModel<>(SuccessMsg.SUCCESS_MSG, null, null));
                    }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                        Log.d(Constants.LOG, "onResponse: " + responseModel.getMessage() );

                    responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE, responseModel.getMessage(), null));

                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE_SERVER, t.getMessage(), null));

                }

            });
        }catch (Exception e){
            Log.d(Constants.LOG, "register: " + e.getStackTrace());
        }
        return responseModelMutableLiveData;
    }





    public void signInWithGoogle(AuthCredential credential) {
     MutableLiveData<FirebaseUser> userModelMutableLiveData = new MutableLiveData<>();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        userModelMutableLiveData.setValue(user);
                    } else {
                        userModelMutableLiveData.setValue(null);
                    }
                });

    }


    public LiveData<ResponseModel<UserModel>> authGoogle(HashMap<String, String> map) {
        MutableLiveData<ResponseModel<UserModel>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.authGoogle(map).enqueue(new Callback<ResponseModel<UserModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<UserModel>> call, Response<ResponseModel<UserModel>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    responseModelMutableLiveData.postValue(new ResponseModel<>(SuccessMsg.SUCCESS_STATE, SuccessMsg.SUCCESS_MSG, response.body().getData()));
                }else {
                    Log.d("TAG", "onResponse: " + response);
                    responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.ERR_STATE,"SDSD", null));

                }
            }

            @Override
            public void onFailure(Call<ResponseModel<UserModel>> call, Throwable t) {
                responseModelMutableLiveData.postValue(new ResponseModel<>(ErrorMsg.SERVER_ERR, ErrorMsg.SERVER_ERR, null));

            }
        });
        return responseModelMutableLiveData;
    }




    public void signOut() {
        firebaseAuth.signOut();
        userModelMutableLiveData.setValue(null);
    }

}
