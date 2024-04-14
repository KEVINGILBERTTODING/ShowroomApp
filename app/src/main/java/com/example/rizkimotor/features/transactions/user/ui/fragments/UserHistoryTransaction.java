package com.example.rizkimotor.features.transactions.user.ui.fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rizkimotor.R;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.model.TransactionModel;
import com.example.rizkimotor.data.services.UserService;
import com.example.rizkimotor.databinding.FragmentUserHistoryTransactionBinding;
import com.example.rizkimotor.databinding.FragmentUserProfileBinding;
import com.example.rizkimotor.features.auth.ui.activities.AuthActivity;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@AndroidEntryPoint
public class UserHistoryTransaction extends Fragment implements ItemClickListener {
    private UserService userService = new UserService();
    private FragmentUserHistoryTransactionBinding binding;
    private UserTransactionViewModel userTransactionViewModel;
    private UserReviewViewModel userReviewViewModel;

    private int userId = 0, stateStatus = 2, mobilId = 0, reviewPosition;
    private String TAG = Constants.LOG;
    private List<TransactionModel> transactionModels;
    private BottomSheetBehavior bottomSheetReview;
    private TransactionHistoryAdapter transactionHistoryAdapter;
    private List<Uri> uriReviewList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService.initService(requireContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserHistoryTransactionBinding.inflate(inflater, container, false);
        init();
        listener();
        return binding.getRoot();
    }

    private void init() {

        if (userService.loadBool(SharedUserData.PREF_IS_LOGIN)) {
            binding.lrErrorLogin.setVisibility(View.GONE);
            userTransactionViewModel = new ViewModelProvider(this).get(UserTransactionViewModel.class);
            userReviewViewModel = new ViewModelProvider(this).get(UserReviewViewModel.class);
            userId = userService.loadInt(SharedUserData.PREF_USER_ID);

            initStatusCategories();
            getTransaction();

            setUpBottomSheetReview();
            bottomSheetReview.setState(BottomSheetBehavior.STATE_HIDDEN);


        }else {
            binding.lrErrorLogin.setVisibility(View.VISIBLE);
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
            hideBottomSheetReview();
        });

        binding.cvReviewImagePicker.setOnClickListener(view -> {
            pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        binding.btnSubmitReview.setOnClickListener(view -> {
            reviewValidation();
        });
    }

    private void initStatusCategories() {
        List<Pair<String, String>> statusList = new ArrayList<>();
        statusList.add(new Pair<>("Proses", "2"));
        statusList.add(new Pair<>("Valid", "1"));
        statusList.add(new Pair<>("Proses Finance", "3"));
        statusList.add(new Pair<>("Tidak Valid", "0"));

        StatusAdapter statusAdapter = new StatusAdapter(requireContext(), statusList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvStatus.setAdapter(statusAdapter);
        binding.rvStatus.setLayoutManager(linearLayoutManager);
        binding.rvStatus.setHasFixedSize(true);
        statusAdapter.setItemClickListener(UserHistoryTransaction.this);
    }

    private void getTransaction() {
        if (userId != 0) {
            binding.progressLoadTrans.setVisibility(View.VISIBLE);
            binding.rvTransactions.setVisibility(View.GONE);
            binding.lrEmpty.setVisibility(View.GONE);
            binding.lrErrorLogin.setVisibility(View.GONE);
            binding.lrError.setVisibility(View.GONE);
            binding.rvTransactions.setAdapter(null);



            userTransactionViewModel.historyTransaction(userId, stateStatus).observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<TransactionModel>>>() {
                @Override
                public void onChanged(ResponseModel<List<TransactionModel>> listResponseModel) {
                    binding.progressLoadTrans.setVisibility(View.GONE);
                    if (listResponseModel.getState().equals(SuccessMsg.SUCCESS_STATE)) {
                        if (listResponseModel.getData() != null && listResponseModel.getData().size() > 0) {
                            transactionModels = listResponseModel.getData();
                            transactionHistoryAdapter = new TransactionHistoryAdapter(requireContext(), transactionModels);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL, false);
                            binding.rvTransactions.setAdapter(transactionHistoryAdapter);
                            binding.rvTransactions.setLayoutManager(linearLayoutManager);
                            binding.rvTransactions.setHasFixedSize(true);
                            binding.rvTransactions.setVisibility(View.VISIBLE);
                            transactionHistoryAdapter.setItemClickListener(UserHistoryTransaction.this);


                        }else {
                            binding.lrEmpty.setVisibility(View.VISIBLE);
                        }
                    }else {
                        binding.lrError.setVisibility(View.VISIBLE);
                        showToast(listResponseModel.getMessage());
                    }
                }
            });
        }else {

            showToast(ErrorMsg.SOMETHING_WENT_WRONG);
        }
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void setUpBottomSheetReview() {


        bottomSheetReview = BottomSheetBehavior.from(binding.bottomSheetRating);
        bottomSheetReview.setHideable(true);

        bottomSheetReview.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    hideBottomSheetReview();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }

    private void showBottomSheetReview() {
        binding.vOverlay.setVisibility(View.VISIBLE);
        bottomSheetReview.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideBottomSheetReview() {
        bottomSheetReview.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay.setVisibility(View.GONE);
    }

    private void reviewValidation() {

        if (userId == 0) {
            showToast("Terjadi kesalahan");
            return;
        }

        if (binding.ratingBar.getRating() == 0) {
            showToast("Anda belum memasukkan bintang rating");
            return;
        }

        if ( uriReviewList.size() <= 0) {
            showToast("Anda belum memilih foto review");
            return;
        }

        if (binding.etReview.getText().toString().isEmpty()) {
            showToast("Harap mengisi ulasan");
            return;
        }

        storeReview();
    }

    private void storeReview() {
        binding.btnSubmitReview.setVisibility(View.GONE);
        binding.progressReview.setVisibility(View.VISIBLE);
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put("mobil_id", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(mobilId)));
        requestBodyMap.put("user_id", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userId)));
        requestBodyMap.put("review_text", RequestBody.create(MediaType.parse("text/plain"), binding.etReview.getText().toString()));
        requestBodyMap.put("star", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(binding.ratingBar.getRating())));

        List<MultipartBody.Part> partList = new ArrayList<>();

        for (int i = 0; i < uriReviewList.size(); i ++ ) {
            int totalPlus = i + 1;
            String formName = "image" + totalPlus;
            Log.d(TAG, "storeReview: " + formName);
            RequestBody requestBody  = RequestBody.create(MediaType.parse("image/*"), contentResolver(uriReviewList.get(i)));
            MultipartBody.Part part = MultipartBody.Part.createFormData(formName, getFileNameFromUri(uriReviewList.get(i)), requestBody);

            partList.add(part);
        }

        userReviewViewModel.storeTransaction(requestBodyMap, partList).observe(getViewLifecycleOwner(), new Observer<ResponseModel>() {
            @Override
            public void onChanged(ResponseModel responseModel) {
                binding.btnSubmitReview.setVisibility(View.VISIBLE);
                binding.progressReview.setVisibility(View.GONE);
                if (responseModel != null && responseModel.getState().equals(SuccessMsg.SUCCESS_STATE)) {
                    transactionHistoryAdapter.setSuccessReview(reviewPosition);
                    hideBottomSheetReview();
                    showToast(responseModel.getMessage());
                }else {
                    showToast(responseModel.getMessage());

                }
            }
        });



    }

    ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia =
            registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(4), uris -> {
                // Callback is invoked after the user selects media items or closes the
                // photo picker.
                if (!uris.isEmpty()) {
                    Log.d("PhotoPicker", "Number of items selected: " + uris.size());
                    if (uris.size() > 4) {
                        showToast("Gambar tidak boleh lebih dari 4");
                    }else {
                        uriReviewList = uris;
                        Log.d(TAG, "uris data: " + uriReviewList);
                        Log.d(TAG, "uris length: " + uriReviewList.size());
                    }
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });



    private String getFileNameFromUri(Uri uri) {
        String fileName = null;

        // Definisikan kolom yang ingin Anda baca (DISPLAY_NAME)
        String[] projection = { MediaStore.MediaColumns.DISPLAY_NAME };

        // Gunakan ContentResolver untuk melakukan query pada URI
        try (Cursor cursor = requireContext().getContentResolver().query(uri, projection, null, null, null)) {
            // Periksa apakah cursor tidak null dan ada data yang tersedia
            if (cursor != null && cursor.moveToFirst()) {
                // Dapatkan nilai dari kolom DISPLAY_NAME
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                fileName = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            // Tangani kesalahan jika terjadi
            Log.e("TAG", "Failed to get file name from URI", e);
        }

        return fileName;
    }

    public byte[] readAllBytesFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;

        // Baca data dari InputStream ke dalam buffer
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }


        // Kembalikan byte array dari ByteArrayOutputStream
        return byteArrayOutputStream.toByteArray();
    }

    private byte[] contentResolver(Uri uri) {
        byte[] imageData = null;
        InputStream inputStream = null;

        try {
            // Dapatkan ContentResolver dari context
            ContentResolver contentResolver = requireContext().getContentResolver();
            String filename = getFileNameFromUri(uri);

            // Baca gambar dari URI
            inputStream = contentResolver.openInputStream(uri);

            // Baca semua byte dari input stream
            imageData = readAllBytesFromInputStream(inputStream);
        } catch (IOException e) {
            // Log atau tangani kesalahan IO
            e.printStackTrace();

            return new byte[0];
        } finally {

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }

        return imageData;
    }



    @Override
    public void itemClickListener(String type, int position, Object object) {
        if (type != null && type.equals("status")) {
            Pair<String, String> statusList = (Pair<String, String>) object;
            stateStatus = Integer.parseInt(statusList.second);
            Log.d("TAG", "itemClickListener: " + stateStatus);

            getTransaction();
        } else if (type != null && type.equals("review") && object != null) {
            TransactionModel transactionModel = (TransactionModel) object;
            if (transactionModel.getMobil_id() != 0) {
                mobilId = transactionModel.getMobil_id();
                reviewPosition = position;
                Log.d(TAG, "setSuccessReview: " + reviewPosition);
                showBottomSheetReview();
            }else {
                showToast(ErrorMsg.SOMETHING_WENT_WRONG);
            }
        } else {
            showToast(ErrorMsg.SOMETHING_WENT_WRONG);
        }
    }
}