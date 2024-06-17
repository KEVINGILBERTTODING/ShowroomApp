package com.example.rizkimotor.features.home.user.ui.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;
import com.denzcoskun.imageslider.constants.ScaleTypes;

import com.denzcoskun.imageslider.models.SlideModel;
import com.example.rizkimotor.R;
import com.example.rizkimotor.data.model.AppInfoModel;
import com.example.rizkimotor.data.model.CarModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.services.UserService;
import com.example.rizkimotor.data.viewmodel.app.AppViewModel;
import com.example.rizkimotor.data.viewmodel.car.CarViewModel;
import com.example.rizkimotor.databinding.FragmentHomeBinding;
import com.example.rizkimotor.features.car.user.ui.fragments.CarDetailFragment;
import com.example.rizkimotor.features.home.user.ui.adapters.car.CarAdapter;
import com.example.rizkimotor.shared.SharedUserData;
import com.example.rizkimotor.util.contstans.Constants;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;
import com.example.rizkimotor.util.listener.ClickListener;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment implements ClickListener {

    private UserService userService;
    private FragmentHomeBinding binding;
    private String username;
    private AppViewModel appViewModel;
    private String TAG = Constants.LOG;
    private ArrayList<SlideModel> imgBannerList = new ArrayList<>();
    private List<CarModel> carModelsList;
    private CarViewModel carViewModel;
    private CarAdapter carAdapter;
    private String phoneNumber;




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

        binding.tvFullname.setText("Hai, " +username +"!");
        getCar();
        listener();


        return binding.getRoot();
    }

    private void listener() {
        binding.swipeRefesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCar();
                getAppInfo();
                binding.swipeRefesh.setRefreshing(false);
            }
        });
    }

    private void init() {
        username = userService.loadString(SharedUserData.PREF_USERNAME, "Guest");
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);

    }

    private void serviceInit() {
        userService =  new UserService();
        userService.initService(requireContext());
    }

    // void get banner

    private void getAppInfo() {
        imgBannerList.clear();
        appViewModel.getAppInfo().observe(getViewLifecycleOwner(), new Observer<ResponseModel<AppInfoModel>>() {
            @Override
            public void onChanged(ResponseModel<AppInfoModel> appInfoModelResponseModel) {
                if (appInfoModelResponseModel.getState().equals(SuccessMsg.SUCCESS_MSG)
                        && appInfoModelResponseModel.getData() != null) {
                    binding.tvAppName.setText(appInfoModelResponseModel.getData().getApp_name());
                   initBanner(appInfoModelResponseModel.getData().getImg_hero1(),
                           appInfoModelResponseModel.getData().getImg_hero2()
                           );
                   phoneNumber = appInfoModelResponseModel.getData().getNo_hp();



                }else {
                    showToast(ErrorMsg.SOMETHING_WENT_WRONG);
                }
            }
        });
    }

    private void getCar() {
        binding.rvCar.setVisibility(View.GONE);
        binding.lrError.setVisibility(View.GONE);
        binding.carProgressBar.setVisibility(View.VISIBLE);
       carViewModel.getAllCar().observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<CarModel>>>() {
           @Override
           public void onChanged(ResponseModel<List<CarModel>> listResponseModel) {

               binding.carProgressBar.setVisibility(View.GONE);
               if (listResponseModel.getData() != null && listResponseModel.getData().size() > 0) {
                   carModelsList = listResponseModel.getData();
                   carAdapter = new CarAdapter(requireContext(), carModelsList);
                   GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
                   binding.rvCar.setAdapter(carAdapter);
                   binding.rvCar.setLayoutManager(gridLayoutManager);
                   binding.rvCar.hasFixedSize();
                   binding.rvCar.setVisibility(View.VISIBLE);
                   carAdapter.onClickListener(HomeFragment.this);

               }else {
                   binding.lrError.setVisibility(View.VISIBLE);
                   showToast(listResponseModel.getMessage());
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

    private void fragmentTransaction(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameHome, fragment).addToBackStack(null).commit();
    }
    @Override
    public void onClickListener(int position, Object object) {
        CarModel carModel = (CarModel) object;
        if (carModel != null) {
            Fragment fragment = new CarDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(SharedUserData.CAR_ID, carModel.getMobil_id());
            bundle.putString("phone_number", phoneNumber);
            fragment.setArguments(bundle);
            fragmentTransaction(fragment);

        }else {
            showToast(ErrorMsg.SOMETHING_WENT_WRONG);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        imgBannerList.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        imgBannerList.clear();



    }
}