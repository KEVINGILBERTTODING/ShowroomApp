package com.example.rizkimotor.features.profile.user.ui.fragments;

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

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rizkimotor.R;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.services.UserService;
import com.example.rizkimotor.databinding.FragmentUserProfileBinding;
import com.example.rizkimotor.features.auth.model.user.UserModel;
import com.example.rizkimotor.features.auth.ui.activities.AuthActivity;
import com.example.rizkimotor.features.auth.viewmodel.AuthViewModel;
import com.example.rizkimotor.features.profile.user.viewmodel.UserProfileViewModel;
import com.example.rizkimotor.shared.SharedUserData;
import com.example.rizkimotor.util.contstans.Constants;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.aviran.cookiebar2.CookieBar;
import org.aviran.cookiebar2.OnActionClickListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@AndroidEntryPoint
public class UserProfileFragment extends Fragment {

    private FragmentUserProfileBinding binding;
    private Uri uriPhotoProfile;
    private UserService userService = new UserService();
    private UserProfileViewModel userProfileViewModel;
    private String TAG = Constants.LOG;
    private int userId = 0;
    private AuthViewModel authViewModel;
    private int role = 0; // 1 = user --- 2 == admin

    private UserModel userModel;

    private BottomSheetBehavior bottomSheetBio, bottomSheetPw;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);

        binding.tvName.setText(userService.loadString(SharedUserData.PREF_USERNAME, "Guest"));
        setUpBottomSheetBio();
        setUpBottomSheetPw();
        bottomSheetBio.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetPw.setState(BottomSheetBehavior.STATE_HIDDEN);

        if (userService.loadBool(SharedUserData.PREF_IS_LOGIN)) {
            getProfile();
            listener();
            binding.btnLogin.setVisibility(View.GONE);
            binding.lrMain.setVisibility(View.VISIBLE);

        }else {
            binding.btnLogin.setVisibility(View.VISIBLE);
            binding.lrMain.setVisibility(View.GONE);

            binding.btnLogin.setOnClickListener(view -> {
                startActivity(new Intent(requireActivity(), AuthActivity.class));
                requireActivity().finish();
            });

            showSnackbar("Notifikasi", "Anda perlu login terlebih dahulu");

        }


        Log.d(TAG, "ROLE: " + userService.loadInt(SharedUserData.PREF_ROLE));


        return binding.getRoot();
    }

    private void init() {
        userProfileViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        userService.initService(requireContext());
        userId = userService.loadInt(SharedUserData.PREF_USER_ID);
        role = userService.loadInt(SharedUserData.PREF_ROLE);


    }


    private void listener() {
        binding.ivProfile.setOnClickListener(view -> {
            if (userModel.getRole() == 1 && userModel.getSign_in() !=null ) {
                if (userModel.getSign_in().equals("email")) {
                    pickMedia.launch(new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build());
                }

            }else if (role == 2 || role == 3) {
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }

        });

        binding.tvSimpan.setOnClickListener(view -> {
            if (uriPhotoProfile != null && userId != 0) {
                savePhotoProfile();

            }
        });

        binding.vOverlay.setOnClickListener(view -> {
            hideBottomSheetBio();
            hideBottomSheetPw();
        });

        binding.rlAkun.setOnClickListener(view -> {
            setUserInfo();
            showBottomSheetBio();

        });

        binding.tvSaveBio.setOnClickListener(view -> {

            inputProfileValidation();
        });

        binding.rlPrivacy.setOnClickListener(view -> {
            showBottomSheetPw();
        });

        binding.tvSavePw.setOnClickListener(view -> {
            inputPwVal();
        });

        binding.rlInfo.setOnClickListener(view -> {
            showSnackbar("Info", "Versi aplikasi telah terbaru");
        });

        binding.rlLogOut.setOnClickListener(view -> {
            CookieBar.build(requireActivity())
                    .setTitle("Peringatan")
                    .setMessage("Apakah Anda yakin ingin keluar?")
                    .setCookiePosition(CookieBar.BOTTOM)
                    .setBackgroundColor(R.color.soft_red)
                    .setTitleColor(R.color.red)
                    .setMessageColor(R.color.black)
                    .setDuration(5000)
                    .setAction("Ya, keluar", new OnActionClickListener() {
                        @Override
                        public void onClick() {

                         logOut();
                        }
                    })
                    .show();

        });


    }

    private void logOut() {
        userService.destroy();

        if (userModel.getSign_in() != null && userModel.getSign_in().equals("google")) {
            authViewModel.signOutGoogle();
        }

        startActivity(new Intent(requireActivity(), AuthActivity.class));
        requireActivity().finish();
    }

    private void inputPwVal() {
        String pwOld = binding.etOldPw.getText().toString();
        String pwNew = binding.etNewPw.getText().toString();

        if(pwOld.isEmpty()) {
            binding.tilOldPw.setError("Kata sandi lama tidak boleh kosong");
            return;
        }

        if(pwNew.isEmpty()) {
            binding.tilNewPw.setError("Kata sandi baru tidak boleh kosong");
            return;
        }

        if (pwNew.length() < 8) {
            binding.tilNewPw.setError("Kata sandi baru tidak boleh kurang dari 8 karakter");
            return;
        }

        updatePw(pwOld, pwNew);
    }

    private void inputProfileValidation() {

        String fullname = binding.etFullName.getText().toString();
        String phoneNumber = binding.etPhoneNumber.getText().toString();
        String address = binding.etAddress.getText().toString();
        String city = binding.etCity.getText().toString();
        String province = binding.etProvince.getText().toString();
        String email = binding.etEmail.getText().toString();




        if (fullname.isEmpty()) {
            binding.tilFullname.setError("Nama lengkap tidak boleh kosong");
            return;
        }

       if (userModel.getSign_in() != null && userModel.getSign_in().equals("email")) {
           if (email.isEmpty()) {
               binding.tilEmail.setError("Email tidak boleh kosong");
               return;
           }
       }

        if (userId == 0) {
            showToast(ErrorMsg.SOMETHING_WENT_WRONG);
            return;
        }



        if (role == 1) { // jika user bukan admin
            if (phoneNumber.isEmpty()) {
                binding.tilPhoneNumber.setError("No handphone tidak boleh kosong");
                return;
            }

            if (address.isEmpty()) {
                binding.tilAddress.setError("Alamat tidak boleh kosong");
                return;
            }

            if (city.isEmpty()) {
                binding.tilCity.setError("Kota tidak boleh kosong");
                return;
            }

            if (province.isEmpty()) {
                binding.tilProvince.setError("Province tidak boleh kosong");
                return;
            }

            updateUserBio(fullname, phoneNumber, address, city, province, email);

        }else if (role == 2 || role == 3) { // admin

            updateUserBio(fullname, "", "", "","", email);
        }




    }



    private void  updateUserBio(String fullname, String phoneNumber, String address, String city, String province, String email) {
        binding.tvSaveBio.setVisibility(View.GONE);
        binding.progressSaveBio.setVisibility(View.VISIBLE);

        HashMap map = new HashMap();

        map.put("nama_lengkap", fullname);

        if (role == 1) {
            if (userModel.getSign_in().equals("email")) {
                map.put("sign_in", userModel.getSign_in());
                map.put("email", email);
            }else {
                map.put("sign_in", userModel.getSign_in());
            }
            map.put("no_hp", phoneNumber);
            map.put("alamat", address);
            map.put("kota", city);
            map.put("provinsi", province);
            map.put("user_id", userId);
            map.put("role", role);

        }else if (role == 2 || role == 3) {
            map.put("email", email);
            map.put("user_id", userId);
            map.put("role", role);

        }




        userProfileViewModel.updateProfile(map).observe(getViewLifecycleOwner(), new Observer<ResponseModel>() {

            @Override
            public void onChanged(ResponseModel responseModel) {
                binding.tvSaveBio.setVisibility(View.VISIBLE);
                binding.progressSaveBio.setVisibility(View.GONE);
                if (responseModel != null && responseModel.getState().equals(SuccessMsg.SUCCESS_STATE)) {
                    userModel.setNama_lengkap(fullname);
                    userModel.setEmail(email);

                    if (role == 1) { // user
                        userModel.setNo_hp(phoneNumber);
                        userModel.setAlamat(address);
                        userModel.setKota(city);
                        userModel.setProvinsi(province);
                    }

                    userService.saveString(SharedUserData.PREF_USERNAME, fullname);
                    binding.tvName.setText(fullname);
                    hideBottomSheetBio();

                    showSnackbar("Berhasil", responseModel.getMessage());

                }else {
                    showToast(responseModel.getMessage());
                }
            }
        });
    }

    private void  updatePw(String oldPw, String newPw) {
        binding.tvSavePw.setVisibility(View.GONE);
        binding.progressSavePw.setVisibility(View.VISIBLE);

        HashMap<String, String> map = new HashMap<>();
        map.put("old_password", oldPw);
        map.put("new_password", newPw);
        map.put("role", String.valueOf(role));
        map.put("user_id", String.valueOf(userId));

        userProfileViewModel.updatePassword(map).observe(getViewLifecycleOwner(), new Observer<ResponseModel>() {

            @Override
            public void onChanged(ResponseModel responseModel) {
                binding.tvSavePw.setVisibility(View.VISIBLE);
                binding.progressSavePw.setVisibility(View.GONE);
                if (responseModel != null && responseModel.getState().equals(SuccessMsg.SUCCESS_STATE)) {

                    hideBottomSheetPw();


                    showSnackbar("Berhasil", responseModel.getMessage());

                }else {
                    showToast(responseModel.getMessage());
                }
            }
        });
    }

    private void setUserInfo() {



        if (role == 1) { // user
            if (userModel.getEmail() != null && userModel.getSign_in().equals("email")) {
                binding.etEmail.setText(userModel.getEmail());

            }

            if (userModel.getKota() != null) {
                binding.etCity.setText(userModel.getKota());

            }

            if (userModel.getProvinsi() != null) {
                binding.etProvince.setText(userModel.getProvinsi());

            }


            if (userModel.getAlamat() != null) {
                binding.etAddress.setText(userModel.getAlamat());

            }
            if (userModel.getNo_hp() != null) {
                binding.etPhoneNumber.setText(userModel.getNo_hp());

            }

            binding.etFullName.setText(userModel.getNama_lengkap());


        }else if (role == 2 || role == 3) { // admin & owner


            if (userModel.getEmail() != null) {
                binding.etEmail.setText(userModel.getEmail());


            }

            if (userModel.getName() != null) {
                binding.etFullName.setText(userModel.getName());
            }

        }




    }


    private void getProfile() {



        if (userId != 0 && role  != 0) {
            userProfileViewModel.profile(String.valueOf(userId), role).observe(getViewLifecycleOwner(), new Observer<ResponseModel<UserModel>>() {
                @Override
                public void onChanged(ResponseModel<UserModel> userModelResponseModel) {
                    if (userModelResponseModel != null && userModelResponseModel.getState().equals(SuccessMsg.SUCCESS_STATE)) {

                        userModel = userModelResponseModel.getData();
                        setUser();


                    }else {
                        showToast(userModelResponseModel.getMessage());
                    }
                }
            });
        }else {
            showSnackbar("Error", "Gagal memuat data pengguna");
        }
    }

    private void setUser() {
        if (role == 1) { // user
            Log.d(TAG, "setUser: " + userModel.getProfile_photo());
            Glide.with(requireContext())

                    .load(userModel.getProfile_photo())
                    .override(70, 70)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.ivProfile);

            if (userModel.getSign_in().equals("google")) {
                binding.rlEmail.setVisibility(View.GONE);
                binding.tilEmail.setVisibility(View.GONE);

                binding.rlPrivacy.setVisibility(View.GONE);
                binding.tvDescPhoto.setVisibility(View.GONE);

            }
            binding.tilCity.setVisibility(View.VISIBLE);
            binding.tilProvince.setVisibility(View.VISIBLE);
            binding.tilAddress.setVisibility(View.VISIBLE);
            binding.tilPhoneNumber.setVisibility(View.VISIBLE);
        } else if (role == 2 || role == 3) { // admin
            Glide.with(requireContext())

                    .load(userModel.getPhoto_profile())
                    .override(70, 70)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.ivProfile);
        }



    }

    private void showSnackbar(String title, String message) {
        CookieBar.build(requireActivity())
                .setTitle(title)
                .setMessage(message)
                .setCookiePosition(CookieBar.BOTTOM)
                .setBackgroundColor(R.color.bg_second)
                .setTitleColor(R.color.primary)
                .setMessageColor(R.color.black)
                .show();
    }

    private void savePhotoProfile() {
        binding.tvSimpan.setVisibility(View.GONE);
        binding.progressPhotoProfile.setVisibility(View.VISIBLE);

        try {
            // Dapatkan ContentResolver dari context
            ContentResolver contentResolver = requireContext().getContentResolver();
            String filename = getFileNameFromUri(uriPhotoProfile);

            // Baca gambar dari URI
            InputStream inputStream = contentResolver.openInputStream(uriPhotoProfile);

            // Baca semua byte dari input stream
            byte[] imageData = readAllBytesFromInputStream(inputStream);

            // Pastikan untuk menutup InputStream setelah selesai
            if (inputStream != null) {
                inputStream.close();
            }

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageData);
            HashMap<String, RequestBody> map = new HashMap<>();
            map.put("user_id", RequestBody.create(MediaType.parse("text/plan"), String.valueOf(userId)));
            map.put("role", RequestBody.create(MediaType.parse("text/plan"), String.valueOf(role)));


            MultipartBody.Part part = MultipartBody.Part.createFormData("photo", filename, requestBody);

            userProfileViewModel.updatePhotoProfile(map, part).observe(getViewLifecycleOwner(), new Observer<ResponseModel>() {
                @Override
                public void onChanged(ResponseModel responseModel) {
                    binding.tvSimpan.setVisibility(View.GONE);
                    binding.progressPhotoProfile.setVisibility(View.GONE);
                    if (responseModel != null && responseModel.getState().equals(SuccessMsg.SUCCESS_STATE)) {
                        showToast("Berhasil mengubah foto profil");

                    }else {
                        showToast(responseModel.getMessage());
                    }
                }
            });


        } catch (IOException e) {
            binding.tvSimpan.setVisibility(View.GONE);
            binding.progressPhotoProfile.setVisibility(View.GONE);
            e.printStackTrace();
        }



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

    // get name from uri

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


    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    binding.ivProfile.setImageURI(uri);
                    uriPhotoProfile = uri;
                    binding.tvSimpan.setVisibility(View.VISIBLE);
                    Log.d("PhotoPicker", "Selected URI: " + uri);
                } else {
                    Log.d("PhotoPicker", "No media selected");
                    showToast("Anda belum memilih gambar");
                }
            });

    private void setUpBottomSheetBio() {


        bottomSheetBio = BottomSheetBehavior.from(binding.bottomSheetBio);
        bottomSheetBio.setHideable(true);

        bottomSheetBio.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    hideBottomSheetBio();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        // hide beberapa form jika bukan admin
        if (role == 1) { // user
            binding.etPhoneNumber.setVisibility(View.VISIBLE);
            binding.etAddress.setVisibility(View.VISIBLE);
            binding.etCity.setVisibility(View.VISIBLE);
            binding.etProvince.setVisibility(View.VISIBLE);
        }


    }

    private void showBottomSheetBio() {
        binding.vOverlay.setVisibility(View.VISIBLE);
        bottomSheetBio.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideBottomSheetBio() {
        bottomSheetBio.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay.setVisibility(View.GONE);
    }

    private void setUpBottomSheetPw() {


        bottomSheetPw = BottomSheetBehavior.from(binding.bottomSheetPw);
        bottomSheetPw.setHideable(true);

        bottomSheetPw.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    hideBottomSheetPw();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }



    private void showBottomSheetPw() {
        binding.vOverlay.setVisibility(View.VISIBLE);
        bottomSheetPw.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideBottomSheetPw() {
        binding.etOldPw.setText("");
        binding.etNewPw.setText("");
        bottomSheetPw.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay.setVisibility(View.GONE);
    }
    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}