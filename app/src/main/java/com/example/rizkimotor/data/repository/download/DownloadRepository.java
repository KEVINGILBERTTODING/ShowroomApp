package com.example.rizkimotor.data.repository.download;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rizkimotor.data.model.ResponseDownloaModel;
import com.example.rizkimotor.data.remote.ApiService;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadRepository {
    private ApiService apiService;
    private String TAG = "TAG";
    @Inject

    public DownloadRepository(ApiService apiService) {
        this.apiService = apiService;
    }


    public LiveData<ResponseDownloaModel> downloadFile(String type, String filename) {
        MutableLiveData<ResponseDownloaModel> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.downloadFileServer(type, filename).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.code() == 200 ) {
                    Log.d(TAG, "onResponse: berhasil download file" );
                    responseModelMutableLiveData.postValue(new ResponseDownloaModel(SuccessMsg.SUCCESS_STATE, SuccessMsg.SUCCESS_MSG, response.body()));

                }else {
                    Log.d(TAG, "onResponse: gagal download file" );

                    responseModelMutableLiveData.postValue(new ResponseDownloaModel(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                responseModelMutableLiveData.postValue(new ResponseDownloaModel(ErrorMsg.ERR_STATE_SERVER, ErrorMsg.SERVER_ERR, null));

            }
        });

        return responseModelMutableLiveData;
    }
}
