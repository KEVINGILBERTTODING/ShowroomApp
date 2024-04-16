package com.example.rizkimotor.features.search.user.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.rizkimotor.R;
import com.example.rizkimotor.data.adapter.SpinnerAdapter;
import com.example.rizkimotor.data.model.BodyModel;
import com.example.rizkimotor.data.model.CarModel;
import com.example.rizkimotor.data.model.MerkModel;
import com.example.rizkimotor.data.model.ResponseFilterModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.model.TransmisiModel;
import com.example.rizkimotor.data.viewmodel.car.CarViewModel;
import com.example.rizkimotor.data.viewmodel.filter.FilterViewModel;
import com.example.rizkimotor.data.viewmodel.search.SearchViewModel;
import com.example.rizkimotor.databinding.FragmentSearchBinding;
import com.example.rizkimotor.features.car.user.ui.fragments.CarDetailFragment;
import com.example.rizkimotor.features.home.user.ui.adapters.car.CarAdapter;
import com.example.rizkimotor.shared.SharedUserData;
import com.example.rizkimotor.util.contstans.Constants;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;
import com.example.rizkimotor.util.listener.ClickListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchFragment extends Fragment implements ClickListener {
    private FragmentSearchBinding binding;
    private SearchViewModel searchViewModel;
    private CarAdapter carAdapter;
    private String TAG = Constants.LOG;
    private CarViewModel carViewModel;
    private List<CarModel> allCarList;
    private FilterViewModel filterViewModel;
    private BottomSheetBehavior bottomSheetFilter;
    private int bodyId =0 , transmisiId =  0, merkId = 0;
    private String priceStart = "0", priceEnd = "0";





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        initView();
        listener();
        getAllCar();
        return binding.getRoot();
    }


    private void initView(){
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        filterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);
        setUpBottomSheetFilter();
        bottomSheetFilter.setState(BottomSheetBehavior.STATE_HIDDEN);

    }

    private void listener() {
        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    searchCar(newText);

                }else {
                    getAllCar();
                }


                return false;
            }
        });

        binding.etPriceMin.addTextChangedListener(new TextWatcher() {
            DecimalFormat formatRupiah = new DecimalFormat("#,###");

            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    binding.etPriceMin.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[Rp,.]", "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }

                    String formatted = formatRupiah.format(parsed);

                    current = formatted;
                    binding.etPriceMin.setText(formatted);
                    binding.etPriceMin.setSelection(formatted.length());
                    binding.etPriceMin.addTextChangedListener(this);
                }
            }
        });


        binding.etPriceMax.addTextChangedListener(new TextWatcher() {
            DecimalFormat formatRupiah = new DecimalFormat("#,###");

            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    binding.etPriceMax.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[Rp,.]", "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }

                    String formatted = formatRupiah.format(parsed);

                    current = formatted;
                    binding.etPriceMax.setText(formatted);
                    binding.etPriceMax.setSelection(formatted.length());
                    binding.etPriceMax.addTextChangedListener(this);
                }
            }
        });





        binding.fabFilter.setOnClickListener(view -> {

            getDataFilter();
        });

        binding.vOverlay.setOnClickListener(view -> {
            binding.vOverlay.setVisibility(View.GONE);
            hideBottomSheetFilter();
        });


    }

    private void getDataFilter() {
        filterViewModel.getDataFilter().observe(getViewLifecycleOwner(), new Observer<ResponseFilterModel>() {
            @Override
            public void onChanged(ResponseFilterModel responseFilterModel) {
                if (responseFilterModel != null && responseFilterModel.getState().equals(SuccessMsg.SUCCESS_STATE)

                )  {

                    initSpinner(
                            responseFilterModel.getData_merk(),
                            responseFilterModel.getData_transmisi(),
                            responseFilterModel.getData_body()
                    );




                    showBottomSheetFilter();


                }else {
                    showToast("Gagal memuat data filter");
                }
            }
        });

    }

    private void initSpinner
            (List<MerkModel> dataMerk,
             List<TransmisiModel> dataTransmisi,
             List<BodyModel> dataBody
    ) {

        SpinnerAdapter<MerkModel> spinnerAdapter = new SpinnerAdapter<>(requireContext(), dataMerk);
        binding.spinnerMerk.setAdapter(spinnerAdapter);

        SpinnerAdapter<TransmisiModel> spinnerTransmisi = new SpinnerAdapter<>(requireContext(), dataTransmisi);
        binding.spinnerTransmisi.setAdapter(spinnerTransmisi);

        SpinnerAdapter<BodyModel> spinnerBody = new SpinnerAdapter<>(requireContext(), dataBody);
        binding.spinnerBody.setAdapter(spinnerBody);


        binding.spinnerMerk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                merkId = dataMerk.get(position).getMerk_id();
                Log.d(TAG, "onItemSelected: " + merkId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.spinnerTransmisi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                transmisiId = dataTransmisi.get(position).getTransmisi_id();

                Log.d(TAG, "onItemSelected: " + transmisiId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        binding.spinnerBody.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                bodyId = dataBody.get(position).getBody_id();
                Log.d(TAG, "onItemSelected: " + bodyId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        binding.btnSubmitFilter.setOnClickListener(view -> {
            filterValidation();
            hideBottomSheetFilter();
        });

    }

    private void filterValidation() {
        if(priceStart != null && priceEnd != null) {
            HashMap<String, String> map = new HashMap<>();
            map.put("bodyId", String.valueOf(bodyId));
            map.put("merkId", String.valueOf(merkId));
            map.put("transmisiId", String.valueOf(transmisiId));
            map.put("priceFrom", binding.etPriceMin.getText().toString());
            map.put("priceEnd", binding.etPriceMax.getText().toString());

            filterCar(map);
        }else {
            showToast("Data filter tidak sesuai");
        }
    }

    private void getAllCar() {

        binding.progressLoad.setVisibility(View.VISIBLE);
        binding.rvSearch.setVisibility(View.GONE);
        binding.lrEmpty.setVisibility(View.GONE);
        binding.rvSearch.setAdapter(null);

        if (allCarList != null) {
            binding.progressLoad.setVisibility(View.GONE);
            binding.rvSearch.setVisibility(View.GONE);
            binding.lrEmpty.setVisibility(View.GONE);
            carAdapter = new CarAdapter(requireContext(), allCarList);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
            binding.rvSearch.setAdapter(carAdapter);
            binding.rvSearch.setLayoutManager(gridLayoutManager);
            binding.rvSearch.setVisibility(View.VISIBLE);
            binding.rvSearch.setHasFixedSize(true);
            carAdapter.onClickListener(SearchFragment.this);



            return;
        }

        carViewModel.getAllCar().observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<CarModel>>>() {
            @Override
            public void onChanged(ResponseModel<List<CarModel>> listResponseModel) {

                binding.progressLoad.setVisibility(View.GONE);
                binding.rvSearch.setVisibility(View.GONE);
                binding.lrEmpty.setVisibility(View.GONE);
                binding.rvSearch.setAdapter(null);
                if (listResponseModel != null && listResponseModel.getData().size() > 0) {
                    allCarList = listResponseModel.getData();
                    carAdapter = new CarAdapter(requireContext(), allCarList);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
                    binding.rvSearch.setAdapter(carAdapter);
                    binding.rvSearch.setLayoutManager(gridLayoutManager);
                    binding.rvSearch.setVisibility(View.VISIBLE);
                    binding.rvSearch.setHasFixedSize(true);
                    carAdapter.onClickListener(SearchFragment.this);

                }else {
                    binding.rvSearch.setVisibility(View.GONE);
                    binding.lrEmpty.setVisibility(View.VISIBLE);
                    settextEmpty("Server Error", ErrorMsg.SOMETHING_WENT_WRONG + ".");
                }
            }
        });

    }


    private void filterCar(HashMap<String, String> map) {
        if (map != null) {


            binding.progressLoad.setVisibility(View.VISIBLE);
            binding.rvSearch.setVisibility(View.GONE);
            binding.lrEmpty.setVisibility(View.GONE);
            binding.rvSearch.setAdapter(null);

            filterViewModel.carFilter(map).observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<CarModel>>>() {
                @Override
                public void onChanged(ResponseModel<List<CarModel>> listResponseModel) {
                    binding.progressLoad.setVisibility(View.GONE);
                    if (listResponseModel != null && listResponseModel.getState().equals(SuccessMsg.SUCCESS_STATE)) {
                       if (listResponseModel.getData() != null && listResponseModel.getData().size() > 0) {
                           binding.lrEmpty.setVisibility(View.GONE);


                           carAdapter = new CarAdapter(requireContext(), listResponseModel.getData());
                           GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
                           binding.rvSearch.setAdapter(carAdapter);
                           binding.rvSearch.setLayoutManager(gridLayoutManager);
                           binding.rvSearch.setVisibility(View.VISIBLE);
                           binding.rvSearch.setHasFixedSize(true);
                           carAdapter.onClickListener(SearchFragment.this);


                       }else{
                           binding.lrEmpty.setVisibility(View.VISIBLE);
                           binding.rvSearch.setVisibility(View.GONE);

                           settextEmpty("Mobil tidak ditemukan", "Tidak ada hasil yang sesuai.");

                       }

                    }else {
                        binding.rvSearch.setVisibility(View.GONE);
                        binding.lrEmpty.setVisibility(View.VISIBLE);
                        settextEmpty("Server Error", ErrorMsg.SOMETHING_WENT_WRONG + ".");

                    }
                }
            });
        }
    }


    private void searchCar(String query) {
        if (query != null && !query.isEmpty()) {


            binding.progressLoad.setVisibility(View.VISIBLE);
            binding.rvSearch.setVisibility(View.GONE);
            binding.lrEmpty.setVisibility(View.GONE);
            binding.rvSearch.setAdapter(null);

            searchViewModel.searchCar(query).observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<CarModel>>>() {
                @Override
                public void onChanged(ResponseModel<List<CarModel>> listResponseModel) {
                    binding.progressLoad.setVisibility(View.GONE);
                    if (listResponseModel != null && listResponseModel.getState().equals(SuccessMsg.SUCCESS_STATE)) {
                        if (listResponseModel.getData() != null && listResponseModel.getData().size() > 0) {
                            binding.lrEmpty.setVisibility(View.GONE);


                            carAdapter = new CarAdapter(requireContext(), listResponseModel.getData());
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
                            binding.rvSearch.setAdapter(carAdapter);
                            binding.rvSearch.setLayoutManager(gridLayoutManager);
                            binding.rvSearch.setVisibility(View.VISIBLE);
                            binding.rvSearch.setHasFixedSize(true);
                            carAdapter.onClickListener(SearchFragment.this);


                        }else{
                            binding.lrEmpty.setVisibility(View.VISIBLE);
                            binding.rvSearch.setVisibility(View.GONE);

                            settextEmpty("Mobil tidak ditemukan", "Tidak ada hasil yang sesuai.");

                        }

                    }else {
                        binding.rvSearch.setVisibility(View.GONE);
                        binding.lrEmpty.setVisibility(View.VISIBLE);
                        settextEmpty("Server Error", ErrorMsg.SOMETHING_WENT_WRONG + ".");

                    }
                }
            });
        }
    }

    private void settextEmpty(String title, String message) {
        binding.tvTitleEmpty.setText(title);
        binding.tvEmptyMessage.setText(message);
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void setUpBottomSheetFilter() {


        bottomSheetFilter = BottomSheetBehavior.from(binding.bottomSheetFilter);
        bottomSheetFilter.setHideable(true);

        bottomSheetFilter.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    hideBottomSheetFilter();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }

    private void showBottomSheetFilter() {
        binding.vOverlay.setVisibility(View.VISIBLE);
        bottomSheetFilter.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideBottomSheetFilter() {
        bottomSheetFilter.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay.setVisibility(View.GONE);
        resetFilter();
    }

    private void resetFilter() {
        merkId = 0;
        transmisiId = 0;
        bodyId = 0;
    }

    private void fragmentTransaction(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameHome, fragment)
                .addToBackStack(null)
                .commit();
    }







    @Override
    public void onClickListener(int position, Object object) {
        if (object != null) {

            CarModel carModel = (CarModel) object;
            Fragment fragment = new CarDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(SharedUserData.CAR_ID, carModel.getMobil_id());
            fragment.setArguments(bundle);
            fragmentTransaction(fragment);
        }else {
            showToast(ErrorMsg.SOMETHING_WENT_WRONG);
        }
    }
}