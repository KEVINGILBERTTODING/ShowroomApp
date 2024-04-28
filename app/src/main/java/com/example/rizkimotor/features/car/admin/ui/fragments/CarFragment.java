package com.example.rizkimotor.features.car.admin.ui.fragments;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.rizkimotor.R;
import com.example.rizkimotor.data.adapter.SpinnerAdapter;
import com.example.rizkimotor.data.model.CarModel;
import com.example.rizkimotor.data.model.ResponseDownloaModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.services.UserService;
import com.example.rizkimotor.data.viewmodel.car.CarViewModel;
import com.example.rizkimotor.data.viewmodel.filter.FilterViewModel;
import com.example.rizkimotor.data.viewmodel.search.SearchViewModel;
import com.example.rizkimotor.databinding.FragmentCarBinding;
import com.example.rizkimotor.features.car.admin.viewmodel.AdminCarViewModel;
import com.example.rizkimotor.features.car.user.ui.fragments.CarDetailFragment;
import com.example.rizkimotor.features.home.user.ui.adapters.car.CarAdapter;
import com.example.rizkimotor.shared.SharedUserData;
import com.example.rizkimotor.util.contstans.Constants;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;
import com.example.rizkimotor.util.listener.ClickListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.aviran.cookiebar2.CookieBar;
import org.aviran.cookiebar2.OnActionClickListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import kotlin.Pair;

@AndroidEntryPoint
public class CarFragment extends Fragment implements ClickListener {
    private FragmentCarBinding binding;
    private SearchViewModel searchViewModel;
    private CarAdapter carAdapter;
    private String TAG = Constants.LOG;
    private CarViewModel carViewModel;
    private List<CarModel> allCarList;
    private UserService userService = new UserService();
    private FilterViewModel filterViewModel;
    private BottomSheetBehavior bottomSheetAction;
    private int mobilId, carPosition, statuState= 1, userId, role;
    private AdminCarViewModel adminCarViewModel;
    private BottomSheetBehavior bottomSheetFilter, bottomSheetOption;





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
        initUserService();
        checkAndRequestStoragePermission();
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        filterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);
        adminCarViewModel = new ViewModelProvider(this).get(AdminCarViewModel.class);

        setUpBottomSheetAction();
        bottomSheetAction.setState(BottomSheetBehavior.STATE_HIDDEN);
        setUpBottomSheetFilter();
        bottomSheetFilter.setState(BottomSheetBehavior.STATE_HIDDEN);
        setUpBottomSheetOption();
        bottomSheetOption.setState(BottomSheetBehavior.STATE_HIDDEN);


    }

    private void checkAndRequestStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Minta izin secara runtime
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        }
    }

    private void initUserService() {
        if (userService != null) {
            userService.initService(requireContext());
            userId = userService.loadInt(SharedUserData.PREF_USER_ID);
            role = userService.loadInt(SharedUserData.PREF_ROLE);
        }
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
            showBottomSheetOption();
        });

        binding.vOverlay.setOnClickListener(view -> {
            hideBottomSheetFilter();
            hideBottomSheetAction();
            hideBottomSheetOption();
        });

        binding.cvDetail.setOnClickListener(view -> {
            Fragment fragment = new CarDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(SharedUserData.CAR_ID, mobilId);
            fragment.setArguments(bundle);
            fragmentTransaction(fragment);

        });

        binding.swipeRefesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllCar();
                binding.swipeRefesh.setRefreshing(false);
            }
        });

        binding.btnDownload.setOnClickListener(view -> {
            downloadCarReport();
        });
        binding.vOverlay2.setOnClickListener(view -> {
            hideBottomSheetFilter();
        });

        binding.btnFilter.setOnClickListener(view -> {
           if (statuState > 3) {
               showToast("Pilih status mobil terlebih dahulu");
               return;
           }

            filterCar(statuState);
        });


        binding.cvDelete.setOnClickListener(view -> {
            destroyCar();
        });

        binding.cvEdit.setOnClickListener(view -> {
            Fragment fragment = new UpdateCarFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("car_id", mobilId);
            fragment.setArguments(bundle);
            fragmentTransaction(fragment);
        });

        binding.cvAdd.setOnClickListener(view -> {
            fragmentTransaction(new AddCarFragment());
        });

        binding.cvFilter.setOnClickListener(view -> {
            showBottomSheetFilter();
            setUpSpinnerStatus();
            hideBottomSheetOption();
            binding.vOverlay.setVisibility(View.VISIBLE);
        });


    }

    private void setUpSpinnerStatus() {
        List<String> stringList = new ArrayList<>();
        stringList.add("Semua mobil");
        stringList.add("Tersedia");
        stringList.add("Dipesan");
        stringList.add("Terjual");

        SpinnerAdapter<String> stringSpinnerAdapter = new SpinnerAdapter<>(requireContext(), stringList);
        binding.spinnerStatus.setAdapter(stringSpinnerAdapter);

        binding.spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String statusSelected = stringList.get(position);
                if (statusSelected.equals("Tersedia")) {
                   statuState = 1;
                }else if (statusSelected.equals("Dipesan")) {
                    statuState = 2;

                }else if (statusSelected.equals("Semua mobil")) {
                    statuState = 3;

                }
                else {
                    statuState = 0;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void filterCar(int status) {
        List<CarModel> carModels = new ArrayList<>();
        binding.rvSearch.setAdapter(null);
        binding.progressLoad.setVisibility(View.VISIBLE);
        if (carModels != null) {
            carModels.clear();
        }

        // jika status mobil semua
        // maka ambil dari list car base
       if (status == 3) {
           if (allCarList != null) {
               carModels = allCarList;

           }else {
               showToast(ErrorMsg.SOMETHING_WENT_WRONG);
           }

       }else {
           for (CarModel carModel : allCarList) {
               if (carModel.getStatus_mobil() == status) {
                   carModels.add(carModel);
               }
           }
       }




        binding.progressLoad.setVisibility(View.GONE);
        binding.rvSearch.setVisibility(View.GONE);
        binding.lrEmpty.setVisibility(View.GONE);
        carAdapter = new CarAdapter(requireContext(), carModels);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        binding.rvSearch.setAdapter(carAdapter);
        binding.rvSearch.setLayoutManager(gridLayoutManager);
        binding.rvSearch.setVisibility(View.VISIBLE);
        binding.rvSearch.setHasFixedSize(true);
        carAdapter.onClickListener(CarFragment.this);
        hideBottomSheetFilter();



    }





    private void getAllCar() {

        binding.progressLoad.setVisibility(View.VISIBLE);
        binding.rvSearch.setVisibility(View.GONE);
        binding.lrEmpty.setVisibility(View.GONE);
        binding.rvSearch.setAdapter(null);
        binding.swipeRefesh.setRefreshing(false);

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
                if (listResponseModel != null && listResponseModel.getData() != null && listResponseModel.getData().size() > 0) {
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

    private void setUpBottomSheetAction() {


        bottomSheetAction = BottomSheetBehavior.from(binding.bottomSheetAction);
        bottomSheetAction.setHideable(true);

        bottomSheetAction.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    hideBottomSheetAction();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }

    private void showBottomSheetAction() {
        binding.vOverlay.setVisibility(View.VISIBLE);
        bottomSheetAction.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideBottomSheetAction() {
        bottomSheetAction.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay.setVisibility(View.GONE);

    }


    private void setUpBottomSheetOption() {


        bottomSheetOption = BottomSheetBehavior.from(binding.bottomSheetOption);
        bottomSheetOption.setHideable(true);

        bottomSheetOption.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    hideBottomSheetOption();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }

    private void showBottomSheetOption() {
        binding.vOverlay.setVisibility(View.VISIBLE);
        bottomSheetOption.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideBottomSheetOption() {
        bottomSheetOption.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay.setVisibility(View.GONE);

    }

    private void destroyCar() {
        carAdapter.destroyCar(carPosition);
        hideBottomSheetAction();
        adminCarViewModel.destroyCar(mobilId).observe(getViewLifecycleOwner(), new Observer<ResponseModel>() {
            @Override
            public void onChanged(ResponseModel carModelResponseModel) {
                if (carModelResponseModel != null && carModelResponseModel.getState().equals(SuccessMsg.SUCCESS_STATE)) {

                    showToast("Berhasil menghapus data mobil");

                }else {
                    showToast(carModelResponseModel.getMessage());
                }
            }
        });

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
        binding.vOverlay2.setVisibility(View.VISIBLE);
        bottomSheetFilter.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideBottomSheetFilter() {

        bottomSheetFilter.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay2.setVisibility(View.GONE);
    }

    private void downloadCarReport() {
        if (userId != 0 && statuState <= 3 && role <=3) {
            HashMap map = new HashMap<>();
            map.put("role", String.valueOf(role));
            map.put("user_id", String.valueOf(userId));
            map.put("status", String.valueOf(statuState));
            binding.progressDownload.setVisibility(View.VISIBLE);
            binding.btnDownload.setVisibility(View.GONE);
            adminCarViewModel.downloadCarReport(String.valueOf(1), map).observe(getViewLifecycleOwner()
                    , new Observer<ResponseDownloaModel>() {
                        @Override
                        public void onChanged(ResponseDownloaModel responseModel) {
                            binding.progressDownload.setVisibility(View.GONE);
                            binding.btnDownload.setVisibility(View.VISIBLE);
                            if (responseModel != null && responseModel.getState().equals(SuccessMsg.SUCCESS_STATE)) {
                                try {
                                    // check permisssion
                                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                                        savePdfToDownloadFolder(responseModel.getResponseBody().bytes());
                                    } else {
                                        showToast("Akses tidak diberikan");
                                        checkAndRequestStoragePermission();

                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else {
                                showToast(responseModel.getMessage());
                            }
                        }
                    });
        }else {
            showToast(ErrorMsg.SOMETHING_WENT_WRONG);
        }
    }

    public void savePdfToDownloadFolder(byte[] pdfData) {
        // Mendapatkan direktori folder Download
        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        // Membuat file PDF baru di folder Download
        File pdfFile = new File(downloadDir, "Laporan rekap mobil.pdf");

        try {
            // Membuat FileOutputStream untuk menulis data PDF ke file
            FileOutputStream fos = new FileOutputStream(pdfFile);

            // Menulis data PDF ke file
            fos.write(pdfData);

            // Tutup FileOutputStream setelah selesai menulis
            fos.close();


            // show notif
            CookieBar.build(requireActivity())
                    .setTitle("Unduhan Selesai")
                    .setMessage("Berhasil mengunduh laporan")
                    .setCookiePosition(CookieBar.BOTTOM)
                    .setAction("Lihat", new OnActionClickListener() {
                        @Override
                        public void onClick() {
                            openPdfFile(pdfFile);
                        }
                    })
                    .setDuration(3000)

                    .show();
        } catch (IOException e) {
            // Tangani kesalahan saat menyimpan file
            e.printStackTrace();
            Log.d(TAG, "savePdfToDownloadFolder: " + e.getMessage());
            Toast.makeText(requireContext(), "Gagal menyimpan PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void openPdfFile(File pdfFile) {
        // Membuat Intent untuk membuka file PDF
        Intent intent = new Intent(Intent.ACTION_VIEW);

        Uri uri = FileProvider.getUriForFile(requireContext(), "com.example.rizkimotor", pdfFile);


        intent.setDataAndType(uri, "application/pdf");


        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Mulai aktivitas untuk membuka file PDF
        try {
            requireContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Tangani ketika tidak ada aplikasi yang dapat membuka file PDF
            e.printStackTrace();
            Toast.makeText(requireContext(), "Tidak ada aplikasi untuk membuka PDF", Toast.LENGTH_SHORT).show();
        }
    }





    @Override
    public void onClickListener(int position, Object object) {
        if (object != null ) {

            CarModel carModel = (CarModel) object;


            if (carModel.getMobil_id() != 0) {
                mobilId = carModel.getMobil_id();
                carPosition = position;
            }else {
                showToast(ErrorMsg.SOMETHING_WENT_WRONG);

            }

            showBottomSheetAction();
        }else {
            showToast(ErrorMsg.SOMETHING_WENT_WRONG);
        }
    }


}