package com.example.rizkimotor.data.viewmodel.download;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rizkimotor.data.model.ResponseDownloaModel;
import com.example.rizkimotor.data.repository.download.DownloadRepository;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DownloadViewModel extends ViewModel {
    private DownloadRepository downloadRepository;
    @Inject

    public DownloadViewModel(DownloadRepository downloadRepository) {
        this.downloadRepository = downloadRepository;
    }

    public LiveData<ResponseDownloaModel> downloadFile(String type, String fileName) {
        MutableLiveData<ResponseDownloaModel> responseDownloaModelMutableLiveData = new MutableLiveData<>();
        if (type != null && fileName != null) {
            return downloadRepository.downloadFile(type, fileName);
        }else {
            responseDownloaModelMutableLiveData.postValue(new ResponseDownloaModel(ErrorMsg.ERR_STATE, ErrorMsg.SOMETHING_WENT_WRONG, null));
        }
        return responseDownloaModelMutableLiveData;
    }
}
