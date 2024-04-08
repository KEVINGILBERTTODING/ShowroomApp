package com.example.rizkimotor.features.home.user.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.denzcoskun.imageslider.constants.ScaleTypes;

import com.denzcoskun.imageslider.models.SlideModel;
import com.example.rizkimotor.data.model.AppInfoModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.services.UserService;
import com.example.rizkimotor.data.viewmodel.app.AppViewModel;
import com.example.rizkimotor.databinding.FragmentHomeBinding;
import com.example.rizkimotor.shared.SharedUserData;
import com.example.rizkimotor.util.contstans.Constants;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private UserService userService;
    private FragmentHomeBinding binding;
    private String username;
    private AppViewModel appViewModel;
    private String TAG = Constants.LOG;
    private ArrayList<SlideModel> imgBannerList = new ArrayList<>();





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceInit();
        init();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment

        getAppInfo();

        binding.tvFullname.setText("Hai " +username +"!");
        return binding.getRoot();
    }

    private void init() {
        username = userService.loadString(SharedUserData.PREF_USERNAME, "Guest");
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
    }

    private void serviceInit() {
        userService =  new UserService();
        userService.initService(requireContext());
    }

    private void getAppInfo() {
        appViewModel.getAppInfo().observe(getViewLifecycleOwner(), new Observer<ResponseModel<AppInfoModel>>() {
            @Override
            public void onChanged(ResponseModel<AppInfoModel> appInfoModelResponseModel) {
                if (appInfoModelResponseModel.getState().equals(SuccessMsg.SUCCESS_MSG)
                        && appInfoModelResponseModel.getData() != null) {
                    binding.tvAppName.setText(appInfoModelResponseModel.getData().getApp_name());
                   initBanner(appInfoModelResponseModel.getData().getImg_hero1(),
                           appInfoModelResponseModel.getData().getImg_hero2()
                           );

                    // put slide model

                }else {
                    showToast("Gagal");
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void initBanner(String banner1, String banner2) {
        imgBannerList.add(new SlideModel(
                banner1,
                "Temukan mobil impian Anda",
                ScaleTypes.FIT
        ));

        imgBannerList.add(new SlideModel(
                banner2,
                "Temukan mobil impian Anda",
                ScaleTypes.FIT
        ));

        binding.imageSlider.setImageList(imgBannerList);
    }

}