package com.example.rizkimotor.features.transactions.admin.ui;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.rizkimotor.R;
import com.example.rizkimotor.data.adapter.SpinnerAdapter;
import com.example.rizkimotor.data.model.CarModel;
import com.example.rizkimotor.data.model.FinanceModel;
import com.example.rizkimotor.data.model.ResponseDownloaModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.model.TransactionModel;
import com.example.rizkimotor.data.remote.ApiService;
import com.example.rizkimotor.data.services.UserService;
import com.example.rizkimotor.data.viewmodel.download.DownloadViewModel;
import com.example.rizkimotor.databinding.FragmentAdminTransactionBinding;
import com.example.rizkimotor.databinding.FragmentUserHistoryTransactionBinding;
import com.example.rizkimotor.features.auth.ui.activities.AuthActivity;
import com.example.rizkimotor.features.car.user.ui.adapters.PhotoReviewAdapter;
import com.example.rizkimotor.features.transactions.admin.adapters.AdminTransactionHistoryAdapter;
import com.example.rizkimotor.features.transactions.admin.adapters.PhotoViewAdapter;
import com.example.rizkimotor.features.transactions.admin.model.ResponseAdminTransactionModel;
import com.example.rizkimotor.features.transactions.admin.viewmodel.AdminTransactionViewModel;
import com.example.rizkimotor.features.transactions.user.adapters.ImageReviewAdapter;
import com.example.rizkimotor.features.transactions.user.adapters.StatusAdapter;
import com.example.rizkimotor.features.transactions.user.adapters.TransactionHistoryAdapter;
import com.example.rizkimotor.features.transactions.user.viewmodel.UserReviewViewModel;
import com.example.rizkimotor.features.transactions.user.viewmodel.UserTransactionViewModel;
import com.example.rizkimotor.shared.SharedUserData;
import com.example.rizkimotor.util.contstans.Constants;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;
import com.example.rizkimotor.util.listener.ItemClickListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.aviran.cookiebar2.CookieBar;
import org.aviran.cookiebar2.OnActionClickListener;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@AndroidEntryPoint
public class AdminTransactionFragment extends Fragment implements ItemClickListener {
    private UserService userService = new UserService();
    private FragmentAdminTransactionBinding binding;
    private AdminTransactionViewModel adminTransactionViewModel;
    private UserTransactionViewModel userTransactionViewModel;
    private DownloadViewModel downloadViewModel;
    private List<String> statusList = new ArrayList<>();
    private List<String> statusListSpinnerFilter = new ArrayList<>();
    private TransactionModel transactionModel;
    private boolean isValidationReport;


    private int transactionPosition, statusTransaction, userId,
    role;

    private PhotoViewAdapter photoReviewAdapter;
    private String transactionId, paymentMethod, stateFormatFile;

    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;

    private int stateStatus = 4, statusStateSpinner, statusStateSpinnerFilter;
    private String TAG = Constants.LOG, fileState, fileDownloadName;
    private List<TransactionModel> transactionModels;
    private BottomSheetBehavior bottomSheetDetailTransaction, bottomSheetOption, bottomSheetFilter;
    private AdminTransactionHistoryAdapter adminTransactionHistoryAdapter;
    private StatusAdapter statusAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService.initService(requireContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminTransactionBinding.inflate(inflater, container, false);
        init();
        listener();
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Minta izin secara runtime
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        }
        return binding.getRoot();
    }

    private void init() {


        try {
            if (userService.loadBool(SharedUserData.PREF_IS_LOGIN)
                    && userService.loadInt(SharedUserData.PREF_USER_ID) != 0) {
                checkAndRequestStoragePermission();
                userId = userService.loadInt(SharedUserData.PREF_USER_ID);
                role = userService.loadInt(SharedUserData.PREF_ROLE);
                Log.d(TAG, "role gweh: " + role);

                binding.lrErrorLogin.setVisibility(View.GONE);
                adminTransactionViewModel = new ViewModelProvider(this).get(AdminTransactionViewModel.class);
                userTransactionViewModel = new ViewModelProvider(this).get(UserTransactionViewModel.class);
                downloadViewModel = new ViewModelProvider(this).get(DownloadViewModel.class);


                initStatusCategories();
                getTransaction();
                initSpinnerStatus();
                initSpinnerStatusFilter();


                setUpBottomSheetDetailTrans();
                bottomSheetDetailTransaction.setState(BottomSheetBehavior.STATE_HIDDEN);

                setUpBottomSheetOption();
                bottomSheetOption.setState(BottomSheetBehavior.STATE_HIDDEN);

                setUpBottomSheetFilter();
                bottomSheetFilter.setState(BottomSheetBehavior.STATE_HIDDEN);

            }else {
                binding.lrErrorLogin.setVisibility(View.VISIBLE);
            }

        }catch (Throwable t) {
            showToast(ErrorMsg.SOMETHING_WENT_WRONG);
            binding.lrError.setVisibility(View.VISIBLE);
            binding.progressLoadTrans.setVisibility(View.GONE);

        }


    }


    private void listener() {
        binding.btnLogin.setOnClickListener(view -> {
            startActivity(new Intent(requireActivity(), AuthActivity.class));
        });

        binding.btnRefresh.setOnClickListener(view -> {
            getTransaction();

        });

        binding.vOverlay.setOnClickListener(view -> {

            hideBottomSheetDetailTrans();
            hidebottomSheetFilter();
            hidebottomSheetOption();
            binding.fabAction.setVisibility(View.VISIBLE);


        });

        binding.btnDeleteTrans.setOnClickListener(view -> {
            deleteTransaction();
        });


        binding.btnClosePreviewPhoto.setOnClickListener(view -> {
            binding.overlayPhoto.setVisibility(View.GONE);
            binding.rlPhotoView.setVisibility(View.GONE);
        });

        binding.spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String status = statusList.get(position);
                selectedSpinner(status);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.etOngkir.addTextChangedListener(new TextWatcher() {
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
                    binding.etOngkir.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[Rp,.]", "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }

                    String formatted =
                            formatRupiah.format(parsed);

                    current = formatted;
                    binding.etOngkir.setText(formatted);
                    binding.etOngkir.setSelection(formatted.length());
                    binding.etOngkir.addTextChangedListener(this);
                }
            }
        });



        binding.swipeRefesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTransaction();
                binding.swipeRefesh.setRefreshing(false);
            }
        });

        binding.btnDownloadFile.setOnClickListener(view -> {
            if (fileState != null && fileDownloadName != null) {
                stateFormatFile = "image/*";
                downloadFile();
            }else {
                showToast("Gambar tidak valid");
            }
        });

        binding.btnSubmit.setOnClickListener(view -> {
            validateUpdateStatus();
        });

        binding.fabAction.setOnClickListener(view -> {
            showbottomSheetOption();
            binding.fabAction.setVisibility(View.GONE);
        });

        binding.cvFilter.setOnClickListener(view -> {
            hidebottomSheetOption();

            showbottomSheetFilter();

        });

        binding.spinnerStatusFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSpinnerFilter(statusListSpinnerFilter.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.vOverlayFilter.setOnClickListener(view -> {
            hidebottomSheetFilter();
            binding.fabAction.setVisibility(View.VISIBLE);
        });

        binding.etDateFrom.setOnClickListener(view -> {
            showDatePicker(binding.etDateFrom);
        });

        binding.etDateEnd.setOnClickListener(view -> {
            showDatePicker(binding.etDateEnd);
        });


        binding.btnFilter.setOnClickListener(view -> {
            isValidationReport = false;

            validateFilter();

        });

        binding.btnDownloadReport.setOnClickListener(view -> {
            isValidationReport = true;
            stateFormatFile = "application/pdf";
           validateFilter();


        });
    }

    private void selectedSpinner(String status) {


        if (status.equals("Valid")) {

            statusStateSpinner = 1;
            binding.tilOngkir.setVisibility(View.VISIBLE);
            binding.tilAlasan.setVisibility(View.GONE);
        }else  if (status.equals("Proses")) {
            statusStateSpinner = 2;
            binding.tilOngkir.setVisibility(View.GONE);
            binding.tilAlasan.setVisibility(View.GONE);
        }else  if (status.equals("Finance proses")) {
            statusStateSpinner = 3;
            binding.tilOngkir.setVisibility(View.GONE);
            binding.tilAlasan.setVisibility(View.GONE);
        }else {
            statusStateSpinner = 0;

            binding.tilOngkir.setVisibility(View.GONE);
            binding.tilAlasan.setVisibility(View.VISIBLE);
        }


    }

    private void selectedSpinnerFilter(String status) {


        if (status.equals("Valid")) {

            statusStateSpinnerFilter = 1;

            statusAdapter.setSelectedState(2);
        }else  if (status.equals("Proses")) {
            statusStateSpinnerFilter = 2;
            statusAdapter.setSelectedState(1);


        }else  if (status.equals("Finance proses")) {
            statusStateSpinnerFilter = 3;
            statusAdapter.setSelectedState(3);


        }else  if (status.equals("Semua")) {
            statusStateSpinnerFilter = 4;
            statusAdapter.setSelectedState(0);


        }
        else {
            statusStateSpinnerFilter = 0;
            statusAdapter.setSelectedState(4);



        }



    }
    private void initStatusCategories() {
        List<Pair<String, String>> statusList = new ArrayList<>();
        statusList.add(new Pair<>("Semua", "4"));
        statusList.add(new Pair<>("Proses", "2"));
        statusList.add(new Pair<>("Selesai", "1"));
        statusList.add(new Pair<>("Proses Finance", "3"));
        statusList.add(new Pair<>("Tidak Valid", "0"));

        statusAdapter = new StatusAdapter(requireContext(), statusList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvStatusTrans.setAdapter(statusAdapter);
        binding.rvStatusTrans.setLayoutManager(linearLayoutManager);
        binding.rvStatusTrans.setHasFixedSize(true);
        statusAdapter.setItemClickListener(AdminTransactionFragment.this);
    }

    private void validateUpdateStatus() {
        if (transactionModel == null) {
            showCookieBar("Gagal", ErrorMsg.SOMETHING_WENT_WRONG);
            return;
        }

        if (statusStateSpinner > 3) {
            showCookieBar("Error", "Status transaksi tidak valid");
            return;
        }

        if (transactionId == null || transactionId.equals("")) {
            showCookieBar("Error", "Transaksi tidak ditemukan");
            return;
        }

        if (transactionModel.getMobil_id() == 0) {
            showCookieBar("Error", "Mobil tidak ditemukan");
            return;
        }

        if (statusStateSpinner >3) {
            showCookieBar("Error", "Status transaksi tidak valid");
            return;
        }

        if (statusStateSpinner == 1 && binding.etOngkir.getText().toString().isEmpty()) {
            binding.tilOngkir.setError("Tidak boleh kosong");
            return;
        }

        if (statusStateSpinner == 0 && binding.etAlasan.getText().toString().isEmpty()) {
            binding.tilAlasan.setError("Tidak boleh kosong");
            return;
        }
        updateStatusTransaction();
    }
    private void updateStatusTransaction() {

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(transactionModel.getUser_id()));
        map.put("status", String.valueOf(statusStateSpinner));
        map.put("mobil_id", String.valueOf(transactionModel.getMobil_id()));
        map.put("transaksi_id", transactionModel.getTransaksi_id());

        if (statusStateSpinner == 1) {
            map.put("biaya_pengiriman", binding.etOngkir.getText().toString());
        }

        if (statusStateSpinner == 0) {
            map.put("alasan", binding.etAlasan.getText().toString());

        }

        binding.progressSubmit.setVisibility(View.VISIBLE);
        binding.btnSubmit.setVisibility(View.GONE);
        adminTransactionViewModel.updateStatusTransaction(transactionModel.getTransaksi_id(), map)
                .observe(getViewLifecycleOwner(), new Observer<ResponseModel>() {
                    @Override
                    public void onChanged(ResponseModel responseModel) {
                        binding.progressSubmit.setVisibility(View.GONE);
                        binding.btnSubmit.setVisibility(View.VISIBLE);
                        if (responseModel != null && responseModel.getState().equals(SuccessMsg.SUCCESS_STATE)) {
                            getTransaction();
                            hideBottomSheetDetailTrans();
                            showCookieBar("Success", responseModel.getMessage());

                        }else {
                            showCookieBar("Error", responseModel.getMessage());
                        }
                    }
                });
    }

    private void resetState() {
        transactionModel = null;
        transactionId = null;
        statusStateSpinner = 0;
        binding.etAlasan.setText("");
        binding.etOngkir.setText("");
    }

    private void filterTransaction() {
        HashMap map  = new HashMap();
        map.put("status", String.valueOf(statusStateSpinnerFilter));
        map.put("date_from", binding.etDateFrom.getText().toString());
        map.put("date_end", binding.etDateEnd.getText().toString());


            binding.progressLoadTrans.setVisibility(View.VISIBLE);
            binding.rvTransactions.setVisibility(View.GONE);
            binding.lrEmpty.setVisibility(View.GONE);
            binding.lrErrorLogin.setVisibility(View.GONE);
            binding.lrError.setVisibility(View.GONE);
            binding.rvTransactions.setAdapter(null);
            binding.swipeRefesh.setRefreshing(false);
            binding.fabAction.setVisibility(View.GONE);





            adminTransactionViewModel.filterTransaction(map).observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<TransactionModel>>>() {
                @Override
                public void onChanged(ResponseModel<List<TransactionModel>> listResponseModel) {
                    binding.progressLoadTrans.setVisibility(View.GONE);
                    binding.fabAction.setVisibility(View.GONE);
                    hidebottomSheetFilter();
                    if (listResponseModel.getState().equals(SuccessMsg.SUCCESS_STATE)) {
                        if (listResponseModel.getData() != null && listResponseModel.getData() != null && listResponseModel.getData().size() > 0
                              ) {
                            transactionModels = listResponseModel.getData();


                            adminTransactionHistoryAdapter = new AdminTransactionHistoryAdapter(requireContext(), transactionModels);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL, false);
                            binding.rvTransactions.setAdapter(adminTransactionHistoryAdapter);
                            binding.rvTransactions.setLayoutManager(linearLayoutManager);
                            binding.rvTransactions.setHasFixedSize(true);
                            binding.rvTransactions.setVisibility(View.VISIBLE);
                            adminTransactionHistoryAdapter.setItemClickListener(AdminTransactionFragment.this);


                        }else {

                            binding.lrEmpty.setVisibility(View.VISIBLE);
                            binding.fabAction.setVisibility(View.GONE);

                        }
                    }else {
                        binding.lrEmpty.setVisibility(View.VISIBLE);



                    }
                }
            });

    }

    private void getTransaction() {


            binding.progressLoadTrans.setVisibility(View.VISIBLE);
            binding.rvTransactions.setVisibility(View.GONE);
            binding.lrEmpty.setVisibility(View.GONE);
            binding.lrErrorLogin.setVisibility(View.GONE);
            binding.lrError.setVisibility(View.GONE);
            binding.rvTransactions.setAdapter(null);
            binding.swipeRefesh.setRefreshing(false);
            binding.fabAction.setVisibility(View.GONE);
        Log.d(TAG, "getTransaction: " + stateStatus);





            adminTransactionViewModel.getTransaction(stateStatus).observe(getViewLifecycleOwner(), new Observer<ResponseModel<ResponseAdminTransactionModel>>() {
                @Override
                public void onChanged(ResponseModel<ResponseAdminTransactionModel> listResponseModel) {
                    binding.progressLoadTrans.setVisibility(View.GONE);
                    binding.fabAction.setVisibility(View.VISIBLE);
                    if (listResponseModel.getState().equals(SuccessMsg.SUCCESS_STATE)) {
                        if (listResponseModel.getData() != null && listResponseModel.getData() != null && listResponseModel.getData().getDataTransactions()
                        != null && listResponseModel.getData().getDataTransactions().size() > 0) {
                            transactionModels = listResponseModel.getData().getDataTransactions();



                            adminTransactionHistoryAdapter = new AdminTransactionHistoryAdapter(requireContext(), transactionModels);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL, false);
                            binding.rvTransactions.setAdapter(adminTransactionHistoryAdapter);
                            binding.rvTransactions.setLayoutManager(linearLayoutManager);
                            binding.rvTransactions.setHasFixedSize(true);
                            binding.rvTransactions.setVisibility(View.VISIBLE);
                            adminTransactionHistoryAdapter.setItemClickListener(AdminTransactionFragment.this);


                        }else {

                            binding.lrEmpty.setVisibility(View.VISIBLE);
                            binding.fabAction.setVisibility(View.GONE);

                        }
                    }else {
                        binding.lrEmpty.setVisibility(View.VISIBLE);
                        showToast(listResponseModel.getMessage());


                    }
                }
            });

    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void validateFilter() {
        if (binding.etDateFrom.getText().toString().isEmpty()) {
            binding.etDateFrom.setError("Tidak boleh kosong");
            return;
        }

        if (binding.etDateEnd.getText().toString().isEmpty()) {
            binding.etDateEnd.setError("Tidak boleh kosong");
            return;
        }

        if (statusStateSpinnerFilter > 4) {
            showCookieBar("Error", "Status tidak valid");
            return;
        }

        int yearDateFrom = Integer.parseInt(binding.etDateFrom.getText().toString().substring(0, 4));
        int yearDateEnd = Integer.parseInt(binding.etDateEnd.getText().toString().substring(0, 4));
        int monthDateFrom = Integer.parseInt(binding.etDateFrom.getText().toString().substring(6, 7));
        int monthDateEnd = Integer.parseInt(binding.etDateEnd.getText().toString().substring(6, 7));
        int dayDateFrom = Integer.parseInt(binding.etDateFrom.getText().toString().substring(binding.etDateFrom.length() - 2));
        int dayDateEnd = Integer.parseInt(binding.etDateEnd.getText().toString().substring(binding.etDateEnd.length() - 2));

        Log.d(TAG, "validateFilter: " + String.valueOf(yearDateFrom));
        Log.d(TAG, "validateFilter: " + String.valueOf(yearDateEnd));
        Log.d(TAG, "validateFilter: " + String.valueOf(monthDateEnd));
        Log.d(TAG, "validateFilter: " + String.valueOf(monthDateFrom));
        Log.d(TAG, "validateFilter: " + String.valueOf(dayDateFrom));
        Log.d(TAG, "validateFilter: " + String.valueOf(dayDateEnd));

        if (yearDateEnd < yearDateFrom) {
            showCookieBar("Error", "Tahun tidak valid");
            return;
        }

        if (yearDateEnd == yearDateFrom) {
           if (monthDateEnd < monthDateFrom) {
               showCookieBar("Error", "Bulan tidak valid");
               return;
           }

            if (monthDateFrom == monthDateEnd  && dayDateFrom > dayDateEnd) {
                showCookieBar("Error", "Tanggal tidak valid");
                return;
            }
        }

        if (userId == 0) {
            showCookieBar("Error", "Anda tidak memiliki akses untuk mengakses fitur ini");
            return;

        }

        if (role == 0 || role == 1) {
            showCookieBar("Error", "Anda tidak memiliki akses untuk mengakses fitur ini");
            return;

        }

        if (isValidationReport) {
            downloadTransReport();
        }else {
            filterTransaction();

        }



    }







    private void downloadTransReport() {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(userId));
        map.put("role", String.valueOf(role));
        map.put("status", String.valueOf(statusStateSpinnerFilter));
        map.put("date_from", binding.etDateFrom.getText().toString());
        map.put("date_end", binding.etDateEnd.getText().toString());
        binding.progressDownloadReport.setVisibility(View.VISIBLE);
        binding.btnDownloadReport.setVisibility(View.GONE);
        adminTransactionViewModel.downloadTransactionReport(map).observe(getViewLifecycleOwner()
                , new Observer<ResponseDownloaModel>() {
                    @Override
                    public void onChanged(ResponseDownloaModel responseModel) {
                        binding.progressDownloadReport.setVisibility(View.GONE);
                        binding.btnDownloadReport.setVisibility(View.VISIBLE);
                        Log.d(TAG, "onChanged: " + fileState + " " + fileDownloadName);
                        if (responseModel != null && responseModel.getState().equals(SuccessMsg.SUCCESS_STATE)) {
                            try {
                                // check permisssion
                                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                                    String fileName = "Laporan_transaksi_" + binding.etDateFrom.getText() + "-" + binding.etDateEnd.getText();
                                    savefile(fileName, responseModel.getResponseBody().bytes());
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

    }


    private void downloadFile() {
        binding.progressDownloadFile.setVisibility(View.VISIBLE);
        binding.btnDownloadFile.setVisibility(View.GONE);
        downloadViewModel.downloadFile(fileState, fileDownloadName).observe(getViewLifecycleOwner()
                , new Observer<ResponseDownloaModel>() {
                    @Override
                    public void onChanged(ResponseDownloaModel responseModel) {
                        binding.progressDownloadFile.setVisibility(View.GONE);
                        binding.btnDownloadFile.setVisibility(View.VISIBLE);
                        Log.d(TAG, "onChanged: " + fileState + " " + fileDownloadName);
                        if (responseModel != null && responseModel.getState().equals(SuccessMsg.SUCCESS_STATE)) {
                            try {
                                // check permisssion
                                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                                    savefile(fileDownloadName, responseModel.getResponseBody().bytes());
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

    }





    private void setUpBottomSheetDetailTrans() {


        bottomSheetDetailTransaction = BottomSheetBehavior.from(binding.bottomSheetDetailTransaction);
        bottomSheetDetailTransaction.setHideable(true);

        bottomSheetDetailTransaction.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    hideBottomSheetDetailTrans();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }

    private void showBottomSheetDetailTrans() {
        binding.vOverlay.setVisibility(View.VISIBLE);
        bottomSheetDetailTransaction.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        binding.fabAction.setVisibility(View.GONE);

    }

    private void hideBottomSheetDetailTrans() {
        resetState();

        bottomSheetDetailTransaction.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay.setVisibility(View.GONE);
        binding.fabAction.setVisibility(View.VISIBLE);


      
    }

    private void setUpBottomSheetOption() {


        bottomSheetOption = BottomSheetBehavior.from(binding.bottomSheetOption);
        bottomSheetOption.setHideable(true);

        bottomSheetOption.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    hidebottomSheetOption();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }





    private void showbottomSheetOption() {
        binding.fabAction.setVisibility(View.GONE);

        binding.vOverlay.setVisibility(View.VISIBLE);
        bottomSheetOption.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
    }

    private void hidebottomSheetOption() {
        resetState();

        bottomSheetOption.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay.setVisibility(View.GONE);
        binding.fabAction.setVisibility(View.VISIBLE);



    }

    private void setUpBottomSheetFilter() {


        bottomSheetFilter = BottomSheetBehavior.from(binding.bottomSheetFilter);
        bottomSheetFilter.setHideable(true);

        bottomSheetFilter.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    hidebottomSheetFilter();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }

    private void showbottomSheetFilter() {
        disableEdittext();
        binding.fabAction.setVisibility(View.GONE);

        binding.vOverlayFilter.setVisibility(View.VISIBLE);
        bottomSheetFilter.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
    }

    private void hidebottomSheetFilter() {
        resetState();


        binding.vOverlayFilter.setVisibility(View.GONE);
        binding.fabAction.setVisibility(View.VISIBLE);
        bottomSheetFilter.setState(BottomSheetBehavior.STATE_HIDDEN);



    }

    private void disableEdittext() {
        binding.etDateFrom.setEnabled(true);
        binding.etDateFrom.setFocusable(false);

        binding.etDateEnd.setEnabled(true);
        binding.etDateEnd.setFocusable(false);
    }








    private void getDetailTransaction(String transactionId) {
        if (transactionId != null) {
            binding.progressLoadTrans.setVisibility(View.VISIBLE);
            binding.scrollDetailTrans.setVisibility(View.GONE);
            userTransactionViewModel.getTransactionDetail(transactionId).observe(getViewLifecycleOwner(), new Observer<ResponseModel<TransactionModel>>() {
                @Override
                public void onChanged(ResponseModel<TransactionModel> transactionModelResponseModel) {
                    binding.vOverlay.setVisibility(View.GONE);
                    binding.progressBarDetailTrans.setVisibility(View.GONE);
                    binding.progressLoadTrans.setVisibility(View.GONE);
                    if (transactionModelResponseModel != null
                            && transactionModelResponseModel.getState().equals(SuccessMsg.SUCCESS_STATE)
                            &&  transactionModelResponseModel.getData() != null) {

                        transactionModel = transactionModelResponseModel.getData();
                        Log.d(TAG, "onChanged: " + transactionModel.getUser_id());
                        binding.scrollDetailTrans.setVisibility(View.VISIBLE);

                        setupDetailTrans(transactionModelResponseModel.getData());

                        showBottomSheetDetailTrans();




                    }else {

                        showCookieBar("Error", "Gagal memuat data transaksi");

                    }
                }
            });
        }else {
            binding.vOverlay.setVisibility(View.GONE);
            binding.progressBarDetailTrans.setVisibility(View.GONE);
            showCookieBar("Error", "Gagal memuat data transaksi");
        }
    }


    public void savefile(String fileName, byte[] data) {
        // Mendapatkan direktori folder Download
        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


        File file = new File(downloadDir, fileName);

        try {
            // Membuat FileOutputStream untuk menulis data PDF ke file
            FileOutputStream fos = new FileOutputStream(file);

            // Menulis data PDF ke file
            fos.write(data);

            // Tutup FileOutputStream setelah selesai menulis
            fos.close();


            // show notif
            CookieBar.build(requireActivity())
                    .setTitle("Unduhan Selesai")
                    .setMessage("Berhasil mengunduh file")
                    .setCookiePosition(CookieBar.BOTTOM)
                    .setAction("Lihat", new OnActionClickListener() {
                        @Override
                        public void onClick() {
                            openPdfFile(file);
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

    private void openPdfFile(File file) {
        // Membuat Intent untuk membuka file PDF
        Intent intent = new Intent(Intent.ACTION_VIEW);

        Uri uri = FileProvider.getUriForFile(requireContext(), "com.example.rizkimotor", file);


        intent.setDataAndType(uri, stateFormatFile);


        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            requireContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {

            e.printStackTrace();
            Toast.makeText(requireContext(), "Tidak ada aplikasi untuk membuka gambar", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupDetailTrans(TransactionModel data) {
        binding.btnDownloadInvoice.setVisibility(View.GONE);
        binding.tvTransactionCode.setText(data.getTransaksi_id());
        setStatusTrans(data);
        binding.tvCarName.setText(data.getMerk());
        binding.tvPhoneNumber.setText(data.getNo_hp_user());
        binding.tvAddress.setText(data.getAlamat_user());
        binding.tvPlatNumber.setText(data.getNoPlat());
        binding.tvCarYear.setText(data.getTahun());
        binding.tvEngineCapacity.setText(data.getKapasitas_mesin());
        binding.tvCarPrice.setText(formatRupiah(data.getHarga_jual()));
        binding.tvDisscount.setText(formatRupiah(data.getDiskon()));
        binding.tvOngkir.setText(formatRupiah(data.getBiaya_pengiriman()));
        binding.tvFullName.setText(data.getNama_user());
        binding.tvDateTransaction.setText(convertDate(data.getCreated_at()));
        binding.tvTotalTransaction.setText(formatRupiah(data.getHarga_jual() - data.getDiskon()));

       setTransFile(data);
       statusTransaction = data.getStatus();


        if (data.getPayment_method().equals("1")) {
            binding.tvPaymentmethod.setText("Cash");
        }else if (data.getPayment_method().equals("2")) {
            binding.tvPaymentmethod.setText("Kredit");
        }else if (data.getPayment_method().equals("3")) {
            binding.tvPaymentmethod.setText("Transfer");
        }else {
            binding.tvPaymentmethod.setText("-");
        }


        // setup status spinner
        if (data.getStatus() == 1) {

            binding.spinnerStatus.setSelection(0);

        }else if (data.getStatus() == 2) {
            binding.spinnerStatus.setSelection(1);

        }else if (data.getStatus() == 3) {
            binding.spinnerStatus.setSelection(2);

        }else if (data.getStatus() == 0) {
            binding.spinnerStatus.setSelection(3);

        }




    }

    private void initSpinnerStatus() {

        statusList.add("Valid");
        statusList.add("Proses");
        statusList.add("Finance proses");
        statusList.add("Tidak valid");


        SpinnerAdapter spinnerAdapter = new SpinnerAdapter<>(requireContext(), statusList);
        binding.spinnerStatus.setAdapter(spinnerAdapter);
    }

    private void initSpinnerStatusFilter() {

        statusListSpinnerFilter.add("Semua");
        statusListSpinnerFilter.add("Valid");
        statusListSpinnerFilter.add("Proses");
        statusListSpinnerFilter.add("Finance proses");
        statusListSpinnerFilter.add("Tidak valid");



        SpinnerAdapter spinnerAdapterFilter = new SpinnerAdapter(requireContext(), statusListSpinnerFilter);
        binding.spinnerStatusFilter.setAdapter(spinnerAdapterFilter);
        binding.spinnerStatusFilter.setSelection(0);

    }



    private void showDatePicker(TextView tvDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateFormatted, monthFormatted;
                if (month < 10) {
                    monthFormatted = String.format("%02d", month + 1);
                }else {
                    monthFormatted = String.valueOf(month + 1);
                }

                if (dayOfMonth < 10) {
                    dateFormatted = String.format("%02d",dayOfMonth);
                }else {
                    dateFormatted = String.valueOf(dayOfMonth);
                }

                tvDate.setText(year + "-" + monthFormatted + "-" + dateFormatted);

            }
        });

        datePickerDialog.show();
    }

    private void setTransFile(TransactionModel data) {

        binding.lrFileCredit.setVisibility(View.GONE);
        binding.lrEvidence.setVisibility(View.GONE);

        if (data.getPayment_method().equals("2")) { // jika kredit

            List<Pair<String, String>>fileImgCreditList = new ArrayList<>();
            String kk = data.getKk();

            if (kk != null) {
                fileImgCreditList.add(new Pair<>(data.getKk_file(), "credit"));
            }

            if (data.getKtp_istri()!= null) {
                fileImgCreditList.add(new Pair<>(data.getKtp_istri_file(), "credit"));

            }

            if (data.getKtp_suami()!= null) {
                fileImgCreditList.add(new Pair<>(data.getKtp_suami_file(), "credit"));

            }


            photoReviewAdapter = new PhotoViewAdapter(requireContext(), fileImgCreditList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
            binding.rvCreditFile.setAdapter(photoReviewAdapter);
            binding.rvCreditFile.setLayoutManager(linearLayoutManager);
            binding.rvCreditFile.setHasFixedSize(true);
            photoReviewAdapter.setItemClickListener(AdminTransactionFragment.this);

            binding.lrFileCredit.setVisibility(View.VISIBLE);
        }else { // cash
            fileState = "evidence";
            fileDownloadName = data.getBukti_pembayaran_name();
            Glide.with(requireContext())
                    .load(data.getBukti_pembayaran())
                    .override(70, 70)
                    .into(binding.ivEvidence);
            binding.ivEvidence.setOnClickListener(view -> {
                showPhoto(data.getBukti_pembayaran());
            });

            binding.lrEvidence.setVisibility(View.VISIBLE);
        }
    }

    private void setStatusTrans(TransactionModel data) {
        if (data.getStatus() == 1) {
            binding.tvStatusTrans.setText("Selesai");
            binding.tvStatusTrans.setTextColor(requireContext().getColor(R.color.green));
            binding.cvStatusTrans.setCardBackgroundColor(requireContext().getColor(R.color.soft_green));
            binding.tilOngkir.setVisibility(View.VISIBLE);
            binding.etOngkir.setText(formatRupiahNoSymbol(data.getBiaya_pengiriman()));
            binding.tilAlasan.setVisibility(View.GONE);
            Log.d(TAG, "setStatusTrans: status selesai");
            return;
        }

        if (data.getStatus() == 2) {
            binding.tvStatusTrans.setText("Proses");
            binding.tvStatusTrans.setTextColor(requireContext().getColor(R.color.primary));
            binding.cvStatusTrans.setCardBackgroundColor(requireContext().getColor(R.color.bg_second));
            binding.tilOngkir.setVisibility(View.GONE);
            binding.tilAlasan.setVisibility(View.GONE);
            return;
        }
        if (data.getStatus() == 3) {
            binding.tvStatusTrans.setText("Proses Finance");
            binding.tvStatusTrans.setTextColor(requireContext().getColor(R.color.blue));
            binding.cvStatusTrans.setCardBackgroundColor(requireContext().getColor(R.color.soft_blue));
            binding.tilOngkir.setVisibility(View.GONE);
            binding.tilAlasan.setVisibility(View.GONE);
            return;
        }

        if (data.getStatus() == 0) {
            binding.tvStatusTrans.setText("Tidak Valid");
            binding.tvStatusTrans.setTextColor(requireContext().getColor(R.color.red));
            binding.cvStatusTrans.setCardBackgroundColor(requireContext().getColor(R.color.soft_red));
            if (data.getAlasan() != null) {
                binding.etAlasan.setText(data.getAlasan());

            }
            binding.tilAlasan.setVisibility(View.VISIBLE);
            binding.tilOngkir.setVisibility(View.GONE);
            return;
        }
    }

    private void showCookieBar(String title, String message) {
        CookieBar.build(requireActivity())
                .setTitle(title)
                .setMessage(message)
                .setCookiePosition(CookieBar.BOTTOM)
                .setDuration(3000)
                .show();

    }

    private void deleteTransaction() {
        binding.progressDeleteTrans.setVisibility(View.VISIBLE);
        binding.btnDeleteTrans.setVisibility(View.GONE);
        if (transactionId != null && paymentMethod != null) {
            adminTransactionViewModel.deleteTransaction(transactionId, Integer.parseInt(paymentMethod))
                    .observe(getViewLifecycleOwner(), new Observer<ResponseModel>() {
                        @Override
                        public void onChanged(ResponseModel responseModel) {
                            binding.progressDeleteTrans.setVisibility(View.GONE);
                            binding.btnDeleteTrans.setVisibility(View.VISIBLE);
                            if (responseModel != null && responseModel.getState() != null
                            && responseModel.getState().equals(SuccessMsg.SUCCESS_STATE)) {
                                adminTransactionHistoryAdapter.deleteTransaction(transactionPosition);
                                hideBottomSheetDetailTrans();
                                showCookieBar("Berhasil", responseModel.getMessage());

                            }else {
                                showCookieBar("Gagal", responseModel.getMessage());

                            }
                        }
                    });
        }else {
            showCookieBar("Gagal", ErrorMsg.SOMETHING_WENT_WRONG);
        }
    }



    private void checkAndRequestStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Minta izin secara runtime
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        }
    }


   


    public static String convertDate(String isoDate) {
        // Format input: ISO 8601 (contoh: "2024-01-11T16:15:01.000000Z")
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());

        // Format output: Contoh "1 Maret 2024"
        SimpleDateFormat outputFormat = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());

        try {
            // Parsing tanggal dari format input
            Date date = inputFormat.parse(isoDate);

            // Mengubah tanggal ke format output
            return outputFormat.format(date);
        } catch (ParseException e) {
            // Tangani parsing error jika format input tidak sesuai
            e.printStackTrace();
            return null; // Kembalikan null jika ada error
        }
    }

    public static String formatRupiah(long nominal) {
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

    public static String formatRupiahNoSymbol(long nominal) {
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        // Mengatur jumlah digit desimal menjadi 0
        kursIndonesia.setMaximumFractionDigits(0);


        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(nominal);
    }

    private void showPhoto(String imageUrl) {
        binding.overlayPhoto.setVisibility(View.VISIBLE);
        binding.rlPhotoView.setVisibility(View.VISIBLE);
        Glide.with(requireContext())
                .load(imageUrl)
                .into(binding.photoView);
    }





    @Override
    public void itemClickListener(String type, int position, Object object) {
        if (type != null && type.equals("status")) {
            Pair<String, String> statusList = (Pair<String, String>) object;
            stateStatus = Integer.parseInt(statusList.second);
            Log.d("TAG", "itemClickListener: " + stateStatus);
            Log.d(TAG, "itemClickListener:  diklik");

            getTransaction();
        } else if (type != null && type.equals(Constants.REVIEW_IMAGE) && object != null) {
            Pair<String, String> fileList = (Pair<String, String>) object;
            fileState = "credit";
            fileDownloadName = fileList.first;

            Log.d(TAG, "file state: " + fileState + " ====== file name: " + fileDownloadName);


            showPhoto(ApiService.END_POINT + "data/credit/" + fileList.first);
        }

        else if (type != null && type.equals("history") && object != null) {
            TransactionModel transactionModel = (TransactionModel) object;
            transactionPosition = position;
            transactionId = transactionModel.getTransaksi_id();
            paymentMethod = transactionModel.getPayment_method();
            getDetailTransaction(transactionModel.getTransaksi_id());
            binding.vOverlay.setVisibility(View.VISIBLE);
            binding.progressBarDetailTrans.setVisibility(View.VISIBLE);

        } else {
            showToast(ErrorMsg.SOMETHING_WENT_WRONG);
        }
    }


}