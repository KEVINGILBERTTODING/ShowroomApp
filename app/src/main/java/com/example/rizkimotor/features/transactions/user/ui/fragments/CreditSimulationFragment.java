package com.example.rizkimotor.features.transactions.user.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rizkimotor.R;
import com.example.rizkimotor.data.model.CarModel;
import com.example.rizkimotor.data.model.CreditModel;
import com.example.rizkimotor.data.model.FinanceModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.remote.ApiService;
import com.example.rizkimotor.data.services.UserService;
import com.example.rizkimotor.data.viewmodel.finance.FinanceViewModel;
import com.example.rizkimotor.databinding.FragmentCreditSimulationBinding;
import com.example.rizkimotor.features.auth.ui.fragments.LoginFragment;
import com.example.rizkimotor.features.profile.user.ui.fragments.UserProfileFragment;
import com.example.rizkimotor.features.transactions.user.viewmodel.UserCreditViewModel;
import com.example.rizkimotor.shared.SharedUserData;
import com.example.rizkimotor.util.contstans.Constants;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.slider.RangeSlider;

import org.aviran.cookiebar2.CookieBar;
import org.aviran.cookiebar2.OnActionClickListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreditSimulationFragment extends Fragment {
    private FragmentCreditSimulationBinding binding;
    private String carName = null, financeName = null, TAG = Constants.LOG;
    private int financeId = 0, carPrice = 0, carId = 0;
    private UserCreditViewModel userCreditViewModel;
    private float totalMinDp = 0, totalMaxDp =0;
    private int monthDuration = 0;

    private FinanceViewModel financeViewModel;
    private  UserService userService;
    private BottomSheetBehavior bottomSheetCredit, bottomSheetFinance;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreditSimulationBinding.inflate(inflater, container, false);
        init();
        listener();
        setUpBottomSheetCredit();
        setUpBottomSheetFinance();
        bottomSheetCredit.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetFinance.setState(BottomSheetBehavior.STATE_HIDDEN);
        return binding.getRoot();
    }

    private void init(){
        userCreditViewModel = new ViewModelProvider(this).get(UserCreditViewModel.class);
        financeViewModel = new ViewModelProvider(this).get(FinanceViewModel.class);
        userService = new UserService();
        userService.initService(requireContext());



        try {

            financeId = getArguments().getInt("finance_id", 0);
            carId = getArguments().getInt("car_id", 0);
            carPrice = getArguments().getInt("car_price", 0);
            carName = getArguments().getString("car_name", null);
            financeName = getArguments().getString("finance_name", null);

            Glide.with(requireContext())
                            .load(getArguments().getString("finance_image"))
                                    .into(binding.ivFinances);


            binding.tvCarName.setText(carName);
            binding.tvFinanceName.setText(financeName);
            getDp();

        } catch (Exception e) {
            showToast(ErrorMsg.SOMETHING_WENT_WRONG);
        }



    }

    private void listener() {
        binding.btnRefresh.setOnClickListener(view -> {
            getDp();
        });

        binding.btnBack.setOnClickListener(view -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        binding.rangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider rangeSlider, float v, boolean b) {
                binding.etDp.setText(formatRupiahFloat(v));
            }
        });

        binding.etDp.addTextChangedListener(new TextWatcher() {
            DecimalFormat formatRupiah = new DecimalFormat("#,###");

            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    binding.etDp.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[Rp,.]", "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }

                    String formatted = formatRupiah.format(parsed);

                    current = formatted;
                    binding.etDp.setText(formatted);
                    binding.etDp.setSelection(formatted.length());
                    binding.etDp.addTextChangedListener(this);
                }
            }
        });

        binding.btnCountNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputValidation();
            }
        });
        binding.btn13.setOnClickListener(view -> {
            monthDuration = 13;
            resetBgButtonColor();

            binding.btn13.setCardBackgroundColor(requireContext().getColor(R.color.primary));

        });

        binding.btn24.setOnClickListener(view -> {
            monthDuration = 24;
            resetBgButtonColor();
            binding.btn24.setCardBackgroundColor(requireContext().getColor(R.color.primary));

        });

        binding.btn36.setOnClickListener(view -> {
            monthDuration = 36;
            resetBgButtonColor();
            binding.btn36.setCardBackgroundColor(requireContext().getColor(R.color.primary));

        });

        binding.btn48.setOnClickListener(view -> {
            monthDuration = 48;
            resetBgButtonColor();
            binding.btn48.setCardBackgroundColor(requireContext().getColor(R.color.primary));

        });

        binding.btn60.setOnClickListener(view -> {
            monthDuration = 60;
            resetBgButtonColor();
            binding.btn60.setCardBackgroundColor(requireContext().getColor(R.color.primary));

        });

        binding.vOverlay.setOnClickListener(view -> {
            hideBottomSheetCredit();
            hidebottomSheetFinance();
        });

        binding.tvFinanceName.setOnClickListener(view -> {
            showbottomSheetFinance();
            getDetaiLFinance();
        });


        binding.btnCreditNow.setOnClickListener(view -> {
            if (isLogin()) {
                Fragment fragment = new CreditTransactionFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("finance_id", financeId);
                bundle.putInt("car_id", carId);
                bundle.putString("car_name", carName);
                bundle.putInt("car_price", carPrice);
                bundle.putString("finance_name", financeName);
                fragment.setArguments(bundle);
                fragmentTransaction(fragment);

            }else  {
                showCookieBar("Akses ditolak", "Anda harus login terlebih dahulu", "Login");

            }

        });



    }

    private void showCookieBar(String title, String message, String action) {
        CookieBar.build(requireActivity())
                .setTitle(title)
                .setMessage(message)
                .setCookiePosition(CookieBar.BOTTOM)
                .setDuration(5000)
                .setBackgroundColor(R.color.bg_second)
                .setMessageColor(R.color.black)
                .setTitleColor(R.color.primary)
                .setAction(action, new OnActionClickListener() {
                    @Override
                    public void onClick() {
                        fragmentTransaction(new LoginFragment());
                    }
                })
                .show();

    }

    Boolean isLogin() {

        if (userService.loadBool(SharedUserData.PREF_IS_LOGIN)) {
            return true;
        }else {
            return false;
        }
    }

    private void getDp() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.lrError.setVisibility(View.GONE);
        binding.mainScroll.setVisibility(View.GONE);
        userCreditViewModel.getDp(carId, financeId).observe(getViewLifecycleOwner(), new Observer<ResponseModel<CreditModel>>() {
            @Override
            public void onChanged(ResponseModel<CreditModel> creditModelResponseModel) {
                binding.progressBar.setVisibility(View.GONE);

                if (creditModelResponseModel.getState().equals(SuccessMsg.SUCCESS_STATE)
                && creditModelResponseModel.getData() != null) {

                    binding.mainScroll.setVisibility(View.VISIBLE);

                    totalMaxDp = (float) creditModelResponseModel.getData().getTotalMaxDp();
                    totalMinDp = (float) creditModelResponseModel.getData().getTotalMinDp();
                    binding.tvMaxDp.setText(formatRupiah(creditModelResponseModel.getData().getTotalMaxDp()));
                    binding.tvMinDp.setText(formatRupiah(creditModelResponseModel.getData().getTotalMinDp()));

                    binding.rangeSlider.setValueFrom(totalMinDp);
                    binding.rangeSlider.setValueTo(totalMaxDp);
                    binding.rangeSlider.setValues(totalMinDp);
                    binding.etDp.setText(formatRupiahFloat(totalMinDp));

                }else {
                    binding.lrError.setVisibility(View.VISIBLE);
                    showToast(creditModelResponseModel.getMessage());
                }
            }
        });
    }



    private String formatRupiah(int nominal) {
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setMaximumFractionDigits(0);


        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(nominal);
    }

    private static String formatRupiahFloat(Float nominal) {
        DecimalFormat kursIndonesia = new DecimalFormat("#,###");
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setMaximumFractionDigits(0);

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(nominal);
    }

    private void inputValidation() {
        float currentValDp = cleanString(binding.etDp.getText().toString());
        if (totalMinDp <= 0) {
            showToast("Jumlah minimum DP tidak valid");
            binding.etDp.setText(formatRupiahFloat(totalMinDp));

            return;
        }

        if (totalMaxDp <= 0) {
            showToast("Jumlah maximum DP tidak valid");
            binding.etDp.setText(formatRupiahFloat(totalMinDp));
            return;
        }

        if (totalMinDp > currentValDp ) {
            showToast("Jumlah DP tidak boleh kurang dari minimum DP");
            binding.etDp.setText(formatRupiahFloat(totalMinDp));

            return;
        }

        if (currentValDp > totalMaxDp) {
            showToast("Jumlah DP tidak boleh lebih dari maximum DP");
            binding.etDp.setText(formatRupiahFloat(totalMaxDp));
            return;
        }

        if (monthDuration == 0) {
            showToast("Anda belum memilih lama pinjaman");
            return;
        }

        if (financeId == 0 || carId == 0 || carPrice == 0) {
            showToast(ErrorMsg.SOMETHING_WENT_WRONG);
            return;
        }

        countCredit(currentValDp);
    }

    private void getDetaiLFinance() {
        binding.progressDetFinance.setVisibility(View.VISIBLE);
        binding.lrDetFinance.setVisibility(View.GONE);
        if (financeId != 0) {
            financeViewModel.getFinanceById(financeId).observe(getViewLifecycleOwner(), new Observer<ResponseModel<FinanceModel>>() {
                @Override
                public void onChanged(ResponseModel<FinanceModel> financeModelResponseModel) {
                    binding.progressDetFinance.setVisibility(View.GONE);
                    if (financeModelResponseModel.getState().equals(SuccessMsg.SUCCESS_STATE)
                    && financeModelResponseModel.getData() != null) {
                        binding.tvDescFinance.setText(financeModelResponseModel.getData().getDeskripsi());
                        binding.tvDetailFinanceName.setText(financeModelResponseModel.getData().getNama_finance());
                        Glide.with(requireContext())
                                        .load(financeModelResponseModel.getData().getImage())
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                        .into(binding.ivFinance);
                        binding.lrDetFinance.setVisibility(View.VISIBLE);
                    }
                }
            });
        }else {
            showToast("Finance tidak ditemukan");
        }
    }

    private void countCredit(float currentDp) {
        int dpFinal = (int) currentDp;
        HashMap map = new HashMap();
        map.put("durasi", monthDuration);
        map.put("finance_id", financeId);
        map.put("total_pembayaran", carPrice);
        map.put("total_dp", dpFinal);

        binding.btnCountNow.setVisibility(View.GONE);
        binding.progressCount.setVisibility(View.VISIBLE);

        userCreditViewModel.countCredit(map).observe(getViewLifecycleOwner(), new Observer<ResponseModel<CreditModel>>() {
            @Override
            public void onChanged(ResponseModel<CreditModel> creditModelResponseModel) {
                binding.btnCountNow.setVisibility(View.VISIBLE);
                binding.progressCount.setVisibility(View.GONE);

                if (creditModelResponseModel.getState().equals(SuccessMsg.SUCCESS_STATE)
                && creditModelResponseModel.getData() != null) {
                    int biayaAdmin = (int) creditModelResponseModel.getData().getBiaya_admin();
                    int biayaAsuransi = (int) creditModelResponseModel.getData().getBiaya_asuransi();
                    int biayaProvisi = (int) creditModelResponseModel.getData().getBiaya_provisi();
                    int totalCicilan = (int) creditModelResponseModel.getData().getTotalCicilan();
                    int biayaTambahan = biayaAdmin + biayaAsuransi + biayaProvisi;
                    int totalPembayaran = dpFinal + biayaTambahan;

                    binding.tvBiayaAdministrasi.setText(formatRupiah(biayaAdmin));
                    binding.tvBiayaProvisi.setText(formatRupiah(biayaProvisi));
                    binding.tvBiayaAsuransi.setText(formatRupiah(biayaAsuransi));
                    binding.tvTotalCicilan.setText(formatRupiah(totalCicilan));
                    binding.tvTotalBiayaTambahan.setText(formatRupiah(biayaTambahan));
                    binding.tvTotalPembayaranPertama.setText(formatRupiah(totalPembayaran));
                    showBottomSheetCredit();
                }else {
                    showToast(creditModelResponseModel.getMessage());
                }
            }
        });

    }

    private void setUpBottomSheetCredit() {


        bottomSheetCredit = BottomSheetBehavior.from(binding.bottomSheetFinancePicker);
        bottomSheetCredit.setHideable(true);

        bottomSheetCredit.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    hideBottomSheetCredit();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }

    private void showBottomSheetCredit() {
        binding.vOverlay.setVisibility(View.VISIBLE);
        bottomSheetCredit.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideBottomSheetCredit() {
        bottomSheetCredit.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay.setVisibility(View.GONE);
    }


    private void fragmentTransaction(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameHome, fragment)
                .addToBackStack(null).commit();
    }

    private void setUpBottomSheetFinance() {


        bottomSheetFinance = BottomSheetBehavior.from(binding.bottomSheetFinance);
        bottomSheetFinance.setHideable(true);

        bottomSheetFinance.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    hidebottomSheetFinance();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }

    private void showbottomSheetFinance() {
        binding.vOverlay.setVisibility(View.VISIBLE);
        bottomSheetFinance.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hidebottomSheetFinance() {
        bottomSheetFinance.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay.setVisibility(View.GONE);
    }


    private void resetBgButtonColor() {
        binding.btn13.setCardBackgroundColor(requireContext().getColor(R.color.white));
        binding.btn24.setCardBackgroundColor(requireContext().getColor(R.color.white));
        binding.btn36.setCardBackgroundColor(requireContext().getColor(R.color.white));
        binding.btn48.setCardBackgroundColor(requireContext().getColor(R.color.white));
        binding.btn60.setCardBackgroundColor(requireContext().getColor(R.color.white));
    }
    private float cleanString(String value) {
        String cleanString = value.toString().replaceAll("[Rp,.]", "");
        return Float.valueOf(cleanString);

    }



    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}