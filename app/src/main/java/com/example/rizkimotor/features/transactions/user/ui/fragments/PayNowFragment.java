package com.example.rizkimotor.features.transactions.user.ui.fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rizkimotor.R;
import com.example.rizkimotor.data.model.BankAccountModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.services.UserService;
import com.example.rizkimotor.data.viewmodel.bankaccount.BankAccountViewModel;
import com.example.rizkimotor.databinding.FragmentPayNowBinding;
import com.example.rizkimotor.features.auth.model.user.UserModel;
import com.example.rizkimotor.features.profile.user.ui.fragments.UserProfileFragment;
import com.example.rizkimotor.features.profile.user.viewmodel.UserProfileViewModel;
import com.example.rizkimotor.features.transactions.user.adapters.BankAccountAdapter;
import com.example.rizkimotor.features.transactions.user.viewmodel.UserTransactionViewModel;
import com.example.rizkimotor.shared.SharedUserData;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;

import org.aviran.cookiebar2.CookieBar;
import org.aviran.cookiebar2.OnActionClickListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@AndroidEntryPoint
public class PayNowFragment extends Fragment {
    private FragmentPayNowBinding binding;
    private UserProfileViewModel userProfileViewModel;
    private BankAccountViewModel bankAccountViewModel;
    private UserTransactionViewModel userTransactionViewModel;
    private UserModel userModel;
    private String  carName,  tag = "TAG";
    private int role;


    private int carId,  userId, carPrice;
    private UserService userService = new UserService();

    private boolean isValid = false;
    private Uri evidenceUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initService();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       binding = FragmentPayNowBinding.inflate(inflater, container, false);
       init();
       listener();
       return binding.getRoot();
    }

    private void initService() {
        userService.initService(requireContext());
        userId = userService.loadInt(SharedUserData.PREF_USER_ID);
        role = userService.loadInt(SharedUserData.PREF_ROLE);

    }


    private void init() {

        userProfileViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        bankAccountViewModel = new ViewModelProvider(this).get(BankAccountViewModel.class);
        userTransactionViewModel = new ViewModelProvider(this).get(UserTransactionViewModel.class);
        binding.etAddress.setEnabled(false);
        binding.etFullName.setEnabled(false);
        binding.etPhoneNumber.setEnabled(false);

        if (getArguments() != null) {


            carId = getArguments().getInt("car_id");
            carPrice = getArguments().getInt("car_price", 0);
            carName = getArguments().getString("car_name");
            getUser();
            getBankAccount();

            binding.tvCarName.setText(carName);

        }else {
            showToast(ErrorMsg.SOMETHING_WENT_WRONG);
        }
    }

    private void listener() {
        binding.btnRefresh.setOnClickListener(view -> {
            getUser();
        });

        binding.btnBack.setOnClickListener(view -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        binding.btnEvidence.setOnClickListener(view -> {

            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });



        binding.btnSubmit.setOnClickListener(view -> {
            if (isValid) {
                validation();
            }else {
                showCookieBar("Data diri belum lengkap", "Harap lengkapi data diri Anda pada halaman profil", "Lengkapi data");

            }
        });
    }

    private void getUser() {
        binding.progressLoading.setVisibility(View.VISIBLE);
        binding.mainScroll.setVisibility(View.GONE);
        binding.lrError.setVisibility(View.GONE);




        userProfileViewModel.profile(String.valueOf(userId), role).observe(getViewLifecycleOwner(), new Observer<ResponseModel<UserModel>>() {
            @Override
            public void onChanged(ResponseModel<UserModel> userModelResponseModel) {
                binding.progressLoading.setVisibility(View.GONE);
                if (userModelResponseModel != null && userModelResponseModel.getState().equals(SuccessMsg.SUCCESS_STATE)) {
                    binding.mainScroll.setVisibility(View.VISIBLE);
                    userModel = userModelResponseModel.getData();

                    if (userModelResponseModel.getData().getUser_id() == 0 || userModelResponseModel.getData().getAlamat() == null
                    || userModelResponseModel.getData().getNo_hp() == null || userModelResponseModel.getData().getNama_lengkap() == null) {

                        isValid = false;
                        if (userModel.getNama_lengkap() != null) {
                            binding.etFullName.setText(userModel.getNama_lengkap());
                        }

                        if (userModel.getAlamat() != null) {
                            binding.etAddress.setText(userModel.getAlamat());
                        }

                        if (userModel.getNo_hp() != null) {
                            binding.etPhoneNumber.setText(userModel.getNo_hp());
                        }

                        showCookieBar("Data diri belum lengkap", "Harap lengkapi data diri Anda pada halaman profil", "Lengkapi data");

                    }else {
                        isValid = true;


                        if (userModel.getNama_lengkap() != null) {
                            binding.etFullName.setText(userModel.getNama_lengkap());
                        }

                        if (userModel.getAlamat() != null) {
                            binding.etAddress.setText(userModel.getAlamat());
                        }

                        if (userModel.getNo_hp() != null) {
                            binding.etPhoneNumber.setText(userModel.getNo_hp());
                        }



                    }

                }else {
                    binding.lrError.setVisibility(View.VISIBLE);
                    Toast.makeText(requireContext(), userModelResponseModel.getMessage(), Toast.LENGTH_SHORT).show();


                }
            }
        });
    }

    private void validation() {

        if (binding.etFullName.getText().toString().isEmpty()) {
            binding.tilFullname.setError("Nama lengkap tidak boleh kosong");
            return;
        }

        if (binding.etPhoneNumber.getText().toString().isEmpty()) {
            binding.tilPhoneNumber.setError("No Handphone tidak boleh kosong");
            return;
        }

        if (binding.etAddress.getText().toString().isEmpty()) {
            binding.tilAddress.setError("Alamat tidak boleh kosong");
            return;
        }

        if (evidenceUri == null ) {
            showToast("Anda belum mengunggah bukti pembayaran");
            return;
        }


        if (userId == 0 || carId == 0 || carPrice == 0) {
            showToast(ErrorMsg.SOMETHING_WENT_WRONG);
            return;
        }

        sendData();
    }


    private void sendData() {

        binding.progressSubmit.setVisibility(View.VISIBLE);
        binding.btnSubmit.setVisibility(View.GONE);

        try {

            Map<String, RequestBody> map = new HashMap<>();
            map.put("user_id", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userId)));
            map.put("mobil_id", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(carId)));
            map.put("total_pembayaran",RequestBody.create(MediaType.parse("text/plain"),  String.valueOf(carPrice)));


            RequestBody fileEvidence = RequestBody.create(MediaType.parse("image/*"), contentResolver(evidenceUri));
            MultipartBody.Part partEvidence = MultipartBody.Part.createFormData("evidence", getFileNameFromUri(evidenceUri), fileEvidence);






            userTransactionViewModel.storeTransaction(map, partEvidence).observe(getViewLifecycleOwner(), new Observer<ResponseModel>() {
                @Override
                public void onChanged(ResponseModel responseModel) {
                    binding.progressSubmit.setVisibility(View.GONE);
                    binding.btnSubmit.setVisibility(View.VISIBLE);
                    if (responseModel != null && responseModel.getState().equals(SuccessMsg.SUCCESS_STATE)) {
                        Fragment fragment = new SuccessTransactionFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("title", "Selamat Transaksi Anda Berhasil");
                        bundle.putString("message", requireContext().getString(R.string.message_transaction_success));
                        fragment.setArguments(bundle);
                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frameHome, fragment)
                                .commit();


                    }else {
                        showToast(responseModel.getMessage());
                    }
                }
            });





        }catch (Throwable t) {
            Log.d(tag, "sendData: " + t.getMessage());
            binding.progressSubmit.setVisibility(View.GONE);
            binding.btnSubmit.setVisibility(View.VISIBLE);

        }



    }

    private void getBankAccount() {
        bankAccountViewModel.getBankAccount().observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<BankAccountModel>>>() {
            @Override
            public void onChanged(ResponseModel<List<BankAccountModel>> bankAccountModelResponseModel) {
                if (bankAccountModelResponseModel != null && bankAccountModelResponseModel.getState().equals(SuccessMsg.SUCCESS_STATE)) {
                    BankAccountAdapter bankAccountAdapter = new BankAccountAdapter(requireContext(), bankAccountModelResponseModel.getData());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
                    binding.rvBankAcc.setAdapter(bankAccountAdapter);
                    binding.rvBankAcc.setLayoutManager(linearLayoutManager);
                    binding.rvBankAcc.setHasFixedSize(true);
                }else {
                    showToast("Gagal memuat data bank");
                }
            }
        });
    }
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {

                    evidenceUri = uri;
                    binding.tvEvidence.setText(getFileNameFromUri(evidenceUri));
                    binding.tvEvidence.setVisibility(View.VISIBLE);
                    binding.btnEvidence.setBackgroundColor(requireContext().getColor(R.color.green));
                } else {
                    Log.d("PhotoPicker", "No media selected");
                    showToast("Anda belum memilih bukti pembayaran");
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



    private void fragmentTransaction(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameHome, fragment).addToBackStack(null)
                .commit();
    }

    private void showCookieBar(String title, String message, String action) {
        CookieBar.build(requireActivity())
                .setTitle(title)
                .setMessage(message)
                .setCookiePosition(CookieBar.BOTTOM)
                .setBackgroundColor(R.color.bg_second)
                .setMessageColor(R.color.black)
                .setTitleColor(R.color.primary)
                .setDuration(5000)
                .setAction(action, new OnActionClickListener() {
                    @Override
                    public void onClick() {
                        fragmentTransaction(new UserProfileFragment());
                    }
                })
                .show();

    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

}