package com.example.rizkimotor.features.car.user.ui.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.rizkimotor.R;
import com.example.rizkimotor.data.model.CarModel;
import com.example.rizkimotor.data.model.FinanceModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.services.UserService;
import com.example.rizkimotor.data.viewmodel.car.CarViewModel;
import com.example.rizkimotor.data.viewmodel.finance.FinanceViewModel;
import com.example.rizkimotor.databinding.FragmentCarDetailBinding;
import com.example.rizkimotor.features.auth.ui.fragments.LoginFragment;
import com.example.rizkimotor.features.car.user.ui.adapters.FinanceAdapter;
import com.example.rizkimotor.features.car.user.ui.adapters.PhotoReviewAdapter;
import com.example.rizkimotor.features.transactions.user.ui.fragments.CreditSimulationFragment;
import com.example.rizkimotor.features.transactions.user.ui.fragments.PayNowFragment;
import com.example.rizkimotor.features.transactions.user.ui.fragments.UserHistoryTransaction;
import com.example.rizkimotor.shared.SharedUserData;
import com.example.rizkimotor.util.contstans.Constants;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.aviran.cookiebar2.CookieBar;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CarDetailFragment extends Fragment implements com.example.rizkimotor.util.listener.ItemClickListener {
    private FragmentCarDetailBinding binding;
    private int carId = 0, carPrice = 0;
    private CarViewModel carViewModel;
    private String phoneNumber, TAG = Constants.LOG, carName;
    private List<SlideModel> slideModelList = new ArrayList<>();
    private CarModel carModelList;
    private BottomSheetBehavior bottomSheetBehaviorRating, bottomSheetBehaviorFinance;
    private UserService userService;
    private FinanceViewModel financeViewModel;
    private List<FinanceModel> financeModels;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = new UserService();


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCarDetailBinding.inflate(inflater, container, false);
        carId = getArguments().getInt(SharedUserData.CAR_ID, 0);
        phoneNumber = getArguments().getString("phone_number", null);
        init();

        getDetailCar();
        listener();
        hideWhatsAppButton();


        return binding.getRoot();
    }

    private void listener() {
        binding.btnBack.setOnClickListener(view -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        binding.btnRefresh.setOnClickListener(view -> {
            getDetailCar();
        });

        binding.btnClosePreviewPhoto.setOnClickListener(view -> {
            binding.rlPhotoView.setVisibility(View.GONE);
        });

        binding.rlPhotoView.setOnClickListener(view -> {
            binding.rlPhotoView.setVisibility(View.GONE);
        });

        binding.btnWhastapp.setOnClickListener(view -> {
            String namaMobil = "";
            String phoneNumberFormatter = "+62" + phoneNumber.substring(1, phoneNumber.length());
            if (namaMobil != null) {
                namaMobil = carName;
            }

            openWhatsApp(phoneNumberFormatter, "Halo...Saya ingin bertanya tentang mobil " + namaMobil);
        });

        binding.cvYt.setOnClickListener(view -> {
            String urlYt = carModelList.getUrl_youtube();
            Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.setData(Uri.parse(urlYt));
            PackageManager packageManager = getActivity().getPackageManager();


            if (intent.resolveActivity(packageManager) != null) {

                startActivity(intent);
            } else {

                intent.setData(Uri.parse(urlYt));
                startActivity(intent);
            }
        });

        binding.cvFb.setOnClickListener(view -> {
            String facebookUrl = carModelList.getUrl_facebook();
            Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.setData(Uri.parse(facebookUrl));
            PackageManager packageManager = getActivity().getPackageManager();


            if (intent.resolveActivity(packageManager) != null) {

                startActivity(intent);
            } else {

                intent.setData(Uri.parse(facebookUrl));
                startActivity(intent);
            }
        });

        binding.cvInstagram.setOnClickListener(view -> {
            String instagramUrl = carModelList.getUrl_instagram();
            Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.setData(Uri.parse(instagramUrl));
            PackageManager packageManager = getActivity().getPackageManager();


            if (intent.resolveActivity(packageManager) != null) {

                startActivity(intent);
            } else {

                intent.setData(Uri.parse(instagramUrl));
                startActivity(intent);
            }
        });

        binding.vOverlay.setOnClickListener(view -> {
            hideBottomSheetRating();
            hideBottomSheetFinance();
        });

        binding.lrRating.setOnClickListener(view -> {
            showBottomSheetRating();
        });

        binding.btnSimulasiKredit.setOnClickListener(view -> {
            if (!isLogin()) {
                fragmentTransaction(new LoginFragment());
                showSnackbar("Pemberitahuan", "Anda harus login terlebih dahulu");

            }else {
                showBottomSheetFinance();
                getAllFinance();
            }


        });

        binding.btnPesanSekarang.setOnClickListener(view -> {
            if (!isLogin()) {
                fragmentTransaction(new LoginFragment());
                showSnackbar("Pemberitahuan", "Anda harus login terlebih dahulu");

            }else {
                Fragment fragment = new PayNowFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("car_id", carId);
                bundle.putString("car_name", carName);
                bundle.putInt("car_price", carPrice);
                fragment.setArguments(bundle);
                fragmentTransaction(fragment);
            }


        });


    }

    private void init() {
        userService.initService(requireContext());
        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        financeViewModel = new ViewModelProvider(this).get(FinanceViewModel.class);
        setUpBottomSheetStream();
        setUpBottomSheetFinance();
        bottomSheetBehaviorRating.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehaviorFinance.setState(BottomSheetBehavior.STATE_HIDDEN);

    }

    private void hideWhatsAppButton() {
        if (phoneNumber != null) {
            binding.btnWhastapp.setVisibility(View.VISIBLE);
        }
    }

    private void getDetailCar() {
        binding.lrError.setVisibility(View.GONE);
        binding.mainScroll.setVisibility(View.GONE);
        binding.progressCircular.setVisibility(View.VISIBLE);
        if(carId != 0) {
            carViewModel.getDetailCar(carId).observe(getViewLifecycleOwner(), new Observer<ResponseModel<CarModel>>() {
                @Override
                public void onChanged(ResponseModel<CarModel> carModelResponseModel) {


                    binding.progressCircular.setVisibility(View.GONE);
                    if (carModelResponseModel.getState().equals(SuccessMsg.SUCCESS_STATE)
                    && carModelResponseModel.getData() != null
                    ){
                        carModelList = carModelResponseModel.getData();
                        binding.mainScroll.setVisibility(View.VISIBLE);
                        binding.lrError.setVisibility(View.GONE);
                        binding.tvCarName.setText(carModelResponseModel.getData().getNama_model());
                        binding.tvCarPrice.setText(formatRupiah(carModelResponseModel.getData().getHarga_jual() - carModelResponseModel.getData().getDiskon()));
                        binding.tvColor.setText(carModelResponseModel.getData().getWarna());
                        binding.tvBodyCar.setText(carModelResponseModel.getData().getBody());
                        binding.tvEngineCapacity.setText(carModelResponseModel.getData().getKapasitas_mesin());
                        binding.tvTransimition.setText(carModelResponseModel.getData().getTransmisi());
                        binding.tvKm.setText(carModelResponseModel.getData().getKm() + "  Km");
                        binding.tvGas.setText(carModelResponseModel.getData().getBahan_bakar());
                        binding.tvGroup.setText(carModelResponseModel.getData().getKapasitas_penumpang());
                        carName = carModelResponseModel.getData().getNama_model();
                        carPrice = carModelResponseModel.getData().getHarga_jual() - carModelResponseModel.getData().getDiskon();

                        if (carModelResponseModel.getData().getDeskripsi() != null) {
                            binding.tvDesc.setText(carModelResponseModel.getData().getDeskripsi());
                        }

                        // sembunyikan cv action button ketika role admin atau owner

                        if (carModelResponseModel.getData().getStatus_mobil() == 1) {
                            if (userService.loadBool(SharedUserData.PREF_IS_LOGIN)) {
                                if (userService.loadInt(SharedUserData.PREF_ROLE) != 2) {
                                    binding.cvButtonAction.setVisibility(View.VISIBLE);
                                }
                            }else {
                                binding.cvButtonAction.setVisibility(View.VISIBLE);
                            }

                        }

                        HashMap map = new HashMap();
                        map.put("image1", carModelResponseModel.getData().getGambar1());
                        map.put("image2", carModelResponseModel.getData().getGambar2());
                        map.put("image3", carModelResponseModel.getData().getGambar3());
                        map.put("image4", carModelResponseModel.getData().getGambar4());
                        map.put("image5", carModelResponseModel.getData().getGambar5());
                        map.put("image6", carModelResponseModel.getData().getGambar6());
                        initImageSlider(map);


                        if (carModelResponseModel.getData().getUrl_facebook() != null) {
                            binding.cvFb.setVisibility(View.VISIBLE);
                        }

                        if (carModelResponseModel.getData().getUrl_instagram() != null) {
                            binding.cvInstagram.setVisibility(View.VISIBLE);
                        }

                        if (carModelResponseModel.getData().getUrl_youtube() != null) {
                            binding.cvYt.setVisibility(View.VISIBLE);
                        }

                        if (carModelResponseModel.getData().getStar() != 0) {
                            binding.lrRating.setVisibility(View.VISIBLE);
                            binding.tvRating.setText(String.valueOf(carModelResponseModel.getData().getStar() + "/5"));

                        }

                        setUpReview();




                    }else {
                        binding.mainScroll.setVisibility(View.GONE);
                        binding.lrError.setVisibility(View.VISIBLE);
                        showToast(ErrorMsg.SOMETHING_WENT_WRONG);

                    }
                }
            });
        }else {
            binding.lrError.setVisibility(View.GONE);
            binding.mainScroll.setVisibility(View.GONE);
            binding.progressCircular.setVisibility(View.VISIBLE);
            showToast(ErrorMsg.SOMETHING_WENT_WRONG);

        }

    }

    private void initImageSlider(HashMap map) {

        Log.d(TAG, "initImageSlider: " + map);

        for (int i = 1; i <= map.size(); i++) {
            if (map.get("image" + i) != null) {
               slideModelList.add(new SlideModel(map.get("image" + i).toString(), ScaleTypes.CENTER_CROP));
            }
        }




        binding.imageSlider.setImageList(slideModelList);

        binding.imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {

                String url = slideModelList.get(i).getImageUrl();
                previewPhoto(url);



            }

            @Override
            public void doubleClick(int i) {

            }
        });
    }
    private void setUpBottomSheetStream() {


        bottomSheetBehaviorRating = BottomSheetBehavior.from(binding.bottomSheetRating);
        bottomSheetBehaviorRating.setHideable(true);

        bottomSheetBehaviorRating.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    hideBottomSheetRating();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }

    private void showSnackbar(String title, String message) {
        CookieBar.build(requireActivity())
                .setTitle(title)
                .setMessage(message)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }
    private void setUpBottomSheetFinance() {


        bottomSheetBehaviorFinance = BottomSheetBehavior.from(binding.bottomSheetFinancePicker);
        bottomSheetBehaviorFinance.setHideable(true);

        bottomSheetBehaviorFinance.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    hideBottomSheetRating();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }

    private void setUpReview() {
        if (carModelList.getNama_lengkap() != null) {

            binding.tvCustomerName.setText(String.valueOf(carModelList.getNama_lengkap()));

        }

        if (carModelList.getReview_text() != null) {
            binding.tvReviewText.setText(carModelList.getReview_text());
        }

        if (carModelList.getPhoto_customer() != null) {
            Glide.with(requireContext())
                    .load(carModelList.getPhoto_customer())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(200, 200)
                    .into(binding.profileCustomer);
        }

        List<String> reviewPhotoList = new ArrayList<>();

        if (carModelList.getImage_review1() != null) {
            reviewPhotoList.add(carModelList.getImage_review1().toString());
        }

        if (carModelList.getImage_review2() != null) {
            reviewPhotoList.add(carModelList.getImage_review2().toString());
        }

        if (carModelList.getImage_review3() != null) {
            reviewPhotoList.add(carModelList.getImage_review3().toString());
        }

        if (carModelList.getImage_review4() != null) {
            reviewPhotoList.add(carModelList.getImage_review4().toString());
        }
        Float totalRating = (float) carModelList.getStar();

        binding.ratingBar.setRating(totalRating);


        // setup review image
        PhotoReviewAdapter photoReviewAdapter = new PhotoReviewAdapter(requireContext(), reviewPhotoList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvPhotoReview.setAdapter(photoReviewAdapter);
        binding.rvPhotoReview.setLayoutManager(linearLayoutManager);
        binding.rvPhotoReview.setHasFixedSize(true);
        photoReviewAdapter.setItemClickListener(CarDetailFragment.this);

    }

    private void getAllFinance() {
        binding.progresFinance.setVisibility(View.VISIBLE);
        binding.rvFinancePicker.setVisibility(View.GONE);
        financeViewModel.getAllFinance().observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<FinanceModel>>>() {
            @Override
            public void onChanged(ResponseModel<List<FinanceModel>> financeModelResponseModel) {
                if (financeModelResponseModel.getState().equals(SuccessMsg.SUCCESS_STATE)
                && financeModelResponseModel.getData() != null) {
                    financeModels = financeModelResponseModel.getData();
                    binding.progresFinance.setVisibility(View.GONE);
                    binding.rvFinancePicker.setVisibility(View.VISIBLE);

                    FinanceAdapter financeAdapter = new FinanceAdapter(requireContext(), financeModels);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvFinancePicker.setAdapter(financeAdapter);
                    binding.rvFinancePicker.setLayoutManager(linearLayoutManager);
                    binding.rvFinancePicker.setHasFixedSize(true);
                    financeAdapter.setItemClickListener(CarDetailFragment.this);


                }else {
                    showToast(financeModelResponseModel.getMessage());
                }
            }
        });
    }

    private void showBottomSheetRating() {
        binding.vOverlay.setVisibility(View.VISIBLE);
        bottomSheetBehaviorRating.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideBottomSheetRating() {
        bottomSheetBehaviorRating.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay.setVisibility(View.GONE);
    }

    private void showBottomSheetFinance() {
        binding.vOverlay.setVisibility(View.VISIBLE);
        bottomSheetBehaviorFinance.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideBottomSheetFinance() {
        bottomSheetBehaviorFinance.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay.setVisibility(View.GONE);
    }


    private void fragmentTransaction(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameHome, fragment).addToBackStack(null)
                .commit();
    }

    private void previewPhoto(String url) {
        if (url != null) {
            Glide.with(requireContext())
                    .load(url)
                    .into(binding.photoView);
            binding.rlPhotoView.setVisibility(View.VISIBLE);
        }else {
            showToast(ErrorMsg.SOMETHING_WENT_WRONG);
        }
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void openWhatsApp(String number, String message) {
        try {
            // Buka WhatsApp menggunakan Intent
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + number + "&text=" + message));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "aAplikasi whatsapp belum terinstal", Toast.LENGTH_SHORT).show();
        }
    }

    public static String formatRupiah(int nominal) {
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        // Mengatur jumlah digit desimal menjadi 0
        kursIndonesia.setMaximumFractionDigits(0);


        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(nominal);
    }

    private boolean isLogin () {
        if (userService.loadBool(SharedUserData.PREF_IS_LOGIN) == true) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void itemClickListener(String type, int position, Object object) {

        if (type.equals(Constants.REVIEW_IMAGE) && object != null) {
            previewPhoto(object.toString());
            hideBottomSheetRating();
        } else if (type.equals(Constants.FINANCE) && object != null) {
            FinanceModel financeModel = (FinanceModel) object;
            Fragment fragment = new CreditSimulationFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("finance_id",financeModel.getFinance_id());
            bundle.putString("finance_name",financeModel.getNama_finance());
            bundle.putInt("car_price", carPrice);
            bundle.putInt("car_id", carId);
            bundle.putString("finance_image", financeModel.getImage());
            bundle.putString("car_name", carName);
            fragment.setArguments(bundle);
            fragmentTransaction(fragment);

        } else {
            showToast(ErrorMsg.SOMETHING_WENT_WRONG);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        slideModelList.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        slideModelList.clear();



    }
}