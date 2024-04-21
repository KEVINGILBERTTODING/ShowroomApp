package com.example.rizkimotor.features.car.admin.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.rizkimotor.R;
import com.example.rizkimotor.data.model.CarModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.viewmodel.car.CarViewModel;
import com.example.rizkimotor.data.viewmodel.filter.FilterViewModel;
import com.example.rizkimotor.data.viewmodel.search.SearchViewModel;
import com.example.rizkimotor.databinding.FragmentCarBinding;
import com.example.rizkimotor.features.car.user.ui.fragments.CarDetailFragment;
import com.example.rizkimotor.features.home.user.ui.adapters.car.CarAdapter;
import com.example.rizkimotor.shared.SharedUserData;
import com.example.rizkimotor.util.contstans.Constants;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;
import com.example.rizkimotor.util.listener.ClickListener;

import java.util.HashMap;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CarFragment extends Fragment implements ClickListener {
    private FragmentCarBinding binding;
    private SearchViewModel searchViewModel;
    private CarAdapter carAdapter;
    private String TAG = Constants.LOG;
    private CarViewModel carViewModel;
    private List<CarModel> allCarList;
    private FilterViewModel filterViewModel;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCarBinding.inflate(inflater, container, false);
        initView();
        listener();
        getAllCar();
        return binding.getRoot();
    }


    private void initView(){
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        filterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);


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

        binding.fabAddCar.setOnClickListener(view -> {
            fragmentTransaction(new AddCarFragment());
        });

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
            carAdapter.onClickListener(CarFragment.this);



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
                    carAdapter.onClickListener(CarFragment.this);

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
                           carAdapter.onClickListener(CarFragment.this);


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
                            carAdapter.onClickListener(CarFragment.this);


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


    private void fragmentTransaction(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameHomeAdmin, fragment)
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