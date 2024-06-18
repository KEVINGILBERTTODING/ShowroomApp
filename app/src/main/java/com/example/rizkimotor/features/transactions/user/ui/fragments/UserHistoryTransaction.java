package com.example.rizkimotor.features.transactions.user.ui.fragments;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

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

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rizkimotor.R;
import com.example.rizkimotor.data.model.ResponseDownloaModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.model.TransactionModel;
import com.example.rizkimotor.data.services.UserService;
import com.example.rizkimotor.databinding.FragmentUserHistoryTransactionBinding;
import com.example.rizkimotor.features.auth.ui.activities.AuthActivity;
import com.example.rizkimotor.features.car.user.ui.adapters.PhotoReviewAdapter;
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
public class UserHistoryTransaction extends Fragment implements ItemClickListener {
    private UserService userService = new UserService();
    private FragmentUserHistoryTransactionBinding binding;
    private UserTransactionViewModel userTransactionViewModel;
    private UserReviewViewModel userReviewViewModel;
    private ImageReviewAdapter imageReviewAdapter;
    private PhotoReviewAdapter photoReviewAdapter;
    private String transactionId;

    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;

    private int userId = 0, stateStatus = 2, mobilId = 0, reviewPosition;
    private String TAG = Constants.LOG;
    private List<TransactionModel> transactionModels;
    private BottomSheetBehavior bottomSheetReview, bottomSheetDetailTransaction;
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
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Minta izin secara runtime
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        }
        return binding.getRoot();
    }

    private void init() {

        setUpBottomSheetReview();
        bottomSheetReview.setState(BottomSheetBehavior.STATE_HIDDEN);
        setUpBottomSheetDetailTrans();
        bottomSheetDetailTransaction.setState(BottomSheetBehavior.STATE_HIDDEN);


        if (userService.loadBool(SharedUserData.PREF_IS_LOGIN)) {
            checkAndRequestStoragePermission();

            binding.lrErrorLogin.setVisibility(View.GONE);
            userTransactionViewModel = new ViewModelProvider(this).get(UserTransactionViewModel.class);
            userReviewViewModel = new ViewModelProvider(this).get(UserReviewViewModel.class);
            userId = userService.loadInt(SharedUserData.PREF_USER_ID);

            initStatusCategories();
            getTransaction();



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
            hideBottomSheetDetailTrans();

        });

        binding.cvReviewImagePicker.setOnClickListener(view -> {
            if (uriReviewList.size() >= 4) {
                showToast("Anda telah memilih 4 gambar");
                return;
            }

            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        binding.btnSubmitReview.setOnClickListener(view -> {
            reviewValidation();
        });

        binding.btnClosePreviewPhoto.setOnClickListener(view -> {
            binding.overlayPhoto.setVisibility(View.GONE);
            binding.rlPhotoView.setVisibility(View.GONE);
        });

        binding.btnDownloadInvoice.setOnClickListener(view -> {
            downloadInvoice();
        });
    }

    private void initStatusCategories() {
        List<Pair<String, String>> statusList = new ArrayList<>();
        statusList.add(new Pair<>("Proses", "2"));
        statusList.add(new Pair<>("Selesai", "1"));
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
        clearInputReview();
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


    private void setUpBottomSheetDetailTrans() {


        bottomSheetDetailTransaction = BottomSheetBehavior.from(binding.bottomSheetDetailTransaction);
        bottomSheetDetailTransaction.setHideable(true);

        bottomSheetDetailTransaction.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
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

    private void showBottomSheetDetailTrans() {
        binding.vOverlay.setVisibility(View.VISIBLE);
        bottomSheetDetailTransaction.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
    }

    private void hideBottomSheetDetailTrans() {
        bottomSheetDetailTransaction.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay.setVisibility(View.GONE);
      
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

                    clearInputReview();
                    showCookieBar("Notifikasi", "Berhasil menambahkan penilaian baru");
                }else {
                    showToast(responseModel.getMessage());

                }
            }
        });



    }

    private void clearInputReview() {
        binding.etReview.setText("");
        binding.ratingBar.setRating(0);
        binding.rvImageReview.setAdapter(null);
        uriReviewList.clear();
    }

    private void removeListImageReview(int position) {
        if (uriReviewList.size() > 0) {
            uriReviewList.remove(position);
            imageReviewAdapter.removeItem(position);
            Log.d(TAG, "removeListImageReview: " + uriReviewList);
            return;
        }

        Log.d(TAG, "removeListImageReview: data suda tidak ada");


    }

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    if (uriReviewList.size() <= 4) {
                       uriReviewList.add(uri);
                       setListPhotoReview();
                    }
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });

    private void setListPhotoReview() {
        binding.rvImageReview.setAdapter(null);
        imageReviewAdapter = new ImageReviewAdapter(requireContext(), uriReviewList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvImageReview.setAdapter(imageReviewAdapter);
        binding.rvImageReview.setLayoutManager(linearLayoutManager);
        binding.rvImageReview.setHasFixedSize(true);
        imageReviewAdapter.setItemClickListener(UserHistoryTransaction.this);
    }

    private void getDetailTransaction(String transactionId) {
        if (transactionId != null) {
            binding.progressLoadTrans.setVisibility(View.VISIBLE);
            binding.scrollDetailTrans.setVisibility(View.GONE);
            userTransactionViewModel.getTransactionDetail(transactionId).observe(getViewLifecycleOwner(), new Observer<ResponseModel<TransactionModel>>() {
                @Override
                public void onChanged(ResponseModel<TransactionModel> transactionModelResponseModel) {
                    binding.progressLoadTrans.setVisibility(View.GONE);
                    if (transactionModelResponseModel != null
                            && transactionModelResponseModel.getState().equals(SuccessMsg.SUCCESS_STATE)
                            &&  transactionModelResponseModel.getData() != null) {


                        binding.scrollDetailTrans.setVisibility(View.VISIBLE);

                        setupDetailTrans(transactionModelResponseModel.getData());




                    }else {

                        showCookieBar("Error", "Gagal memuat data transaksi");

                    }
                }
            });
        }else {
            showCookieBar("Error", "Gagal memuat data transaksi");
        }
    }

    private void downloadInvoice() {
       if (transactionId != null) {
           binding.progressDownloadInvoice.setVisibility(View.VISIBLE);
           binding.btnDownloadInvoice.setVisibility(View.GONE);
           userTransactionViewModel.downloadInvoice(transactionId).observe(getViewLifecycleOwner()
                   , new Observer<ResponseDownloaModel>() {
                       @Override
                       public void onChanged(ResponseDownloaModel responseModel) {
                           binding.progressDownloadInvoice.setVisibility(View.GONE);
                           binding.btnDownloadInvoice.setVisibility(View.VISIBLE);
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
        File pdfFile = new File(downloadDir, transactionId + ".pdf");

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
                    .setMessage("Berhasil mengunduh invoice")
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

    private void setupDetailTrans(TransactionModel data) {
        binding.btnDownloadInvoice.setVisibility(View.GONE);
        binding.tvTransactionCode.setText(data.getTransaksi_id());
        setStatusTrans(data.getStatus());
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


        if (data.getPayment_method().equals("1")) {
            binding.tvPaymentmethod.setText("Cash");
        }else if (binding.tvPaymentmethod.equals("2")) {
            binding.tvPaymentmethod.setText("Kredit");
        }else if (binding.tvPaymentmethod.equals("3")) {
            binding.tvPaymentmethod.setText("Transfer");
        }else {
            binding.tvPaymentmethod.setText("-");
        }

        if (data.getStatus() == 1) {
            binding.btnDownloadInvoice.setVisibility(View.VISIBLE);

        }


    }

    private void setTransFile(TransactionModel data) {

        binding.lrFileCredit.setVisibility(View.GONE);
        binding.lrEvidence.setVisibility(View.GONE);

        if (data.getPayment_method().equals("2")) {
            List<String> fileImgCreditList = new ArrayList<>();
            String kk = data.getKk();

            if (kk != null) {
                fileImgCreditList.add(kk);
            }

            if (data.getKtp_istri()!= null) {
                fileImgCreditList.add(data.getKtp_istri().toString());
            }

            if (data.getKtp_suami()!= null) {
                fileImgCreditList.add(data.getKtp_suami().toString());
            }


            photoReviewAdapter = new PhotoReviewAdapter(requireContext(), fileImgCreditList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
            binding.rvCreditFile.setAdapter(photoReviewAdapter);
            binding.rvCreditFile.setLayoutManager(linearLayoutManager);
            binding.rvCreditFile.setHasFixedSize(true);
            photoReviewAdapter.setItemClickListener(UserHistoryTransaction.this);

            binding.lrFileCredit.setVisibility(View.VISIBLE);
        }else {
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

    private void setStatusTrans(int status) {
        if (status == 1) {
            binding.tvStatusTrans.setText("Selesai");
            binding.tvStatusTrans.setTextColor(requireContext().getColor(R.color.green));
            binding.cvStatusTrans.setCardBackgroundColor(requireContext().getColor(R.color.soft_green));
            return;
        }

        if (status == 2) {
            binding.tvStatusTrans.setText("Proses");
            binding.tvStatusTrans.setTextColor(requireContext().getColor(R.color.primary));
            binding.cvStatusTrans.setCardBackgroundColor(requireContext().getColor(R.color.bg_second));
            return;
        }
        if (status == 3) {
            binding.tvStatusTrans.setText("Proses Finance");
            binding.tvStatusTrans.setTextColor(requireContext().getColor(R.color.blue));
            binding.cvStatusTrans.setCardBackgroundColor(requireContext().getColor(R.color.soft_blue));
            return;
        }

        if (status == 0) {
            binding.tvStatusTrans.setText("Tidak Valid");
            binding.tvStatusTrans.setTextColor(requireContext().getColor(R.color.red));
            binding.cvStatusTrans.setCardBackgroundColor(requireContext().getColor(R.color.soft_red));
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

    private void checkAndRequestStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Minta izin secara runtime
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        }
    }


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
        } else if (type != null && type.equals("history") && object != null) {
            TransactionModel transactionModel = (TransactionModel) object;
            transactionId = transactionModel.getTransaksi_id();
            getDetailTransaction(transactionModel.getTransaksi_id());
            showBottomSheetDetailTrans();

        } else if (type != null && type.equals(Constants.REVIEW_IMAGE) && object != null) {
            String berkasKredit = (String) object;
            showPhoto(berkasKredit);
        } else if (type != null && type.equals("delete_review")) {
            removeListImageReview(position);
        } else {
            showToast(ErrorMsg.SOMETHING_WENT_WRONG);
        }
    }
}