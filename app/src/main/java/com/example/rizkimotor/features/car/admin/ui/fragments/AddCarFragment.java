package com.example.rizkimotor.features.car.admin.ui.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rizkimotor.R;
import com.example.rizkimotor.data.adapter.SpinnerAdapter;
import com.example.rizkimotor.data.model.BahanBakarModel;
import com.example.rizkimotor.data.model.BodyModel;
import com.example.rizkimotor.data.model.ColorModel;
import com.example.rizkimotor.data.model.KapasitaMesinModel;
import com.example.rizkimotor.data.model.KapasitasPenumpangModel;
import com.example.rizkimotor.data.model.MerkModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.model.TankModel;
import com.example.rizkimotor.data.model.TransmisiModel;
import com.example.rizkimotor.data.services.UserService;
import com.example.rizkimotor.databinding.FragmentAddCarBinding;
import com.example.rizkimotor.features.auth.ui.activities.AuthActivity;
import com.example.rizkimotor.features.car.admin.model.CarComponentModel;
import com.example.rizkimotor.features.car.admin.viewmodel.AdminCarViewModel;
import com.example.rizkimotor.features.car.viewModel.CarViewModel;
import com.example.rizkimotor.shared.SharedUserData;
import com.example.rizkimotor.util.contstans.Constants;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;

import org.aviran.cookiebar2.CookieBar;
import org.aviran.cookiebar2.OnActionClickListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@AndroidEntryPoint
public class AddCarFragment extends Fragment {
    private FragmentAddCarBinding binding;
    private String TAG = Constants.LOG;
    private CarViewModel carViewModel;
    private UserService userService;
    private AdminCarViewModel adminCarViewModel;
    private int bahanBakarId, bodyId, kpmId, merkId, tangkiId, transId, colorId, kpmpId;



    private boolean spinnerIsComplete;
    private Uri uriFrontImg, uriBackImg, uriLeftImg, uriRightImg, uriDet1Img, uriDet2Img;
    private String stateImgPicker;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkAndRequestStoragePermission();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddCarBinding.inflate(inflater, container, false);
        initService();



        return binding.getRoot();
    }

    private void initService() {
        userService = new UserService();
        userService.initService(requireContext());

        if (userService != null && userService.loadBool(SharedUserData.PREF_IS_LOGIN) == true) {
            init();
            getCarComponent();
            listener();

        }else {
            showToast("Anda tidak memiliki akses");
            startActivity(new Intent(requireContext(), AuthActivity.class));
            requireActivity().finish();

        }
    }

    private void init(){
        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        adminCarViewModel = new ViewModelProvider(this).get(AdminCarViewModel.class);

        disabledEdittext();



    }

    private void disabledEdittext() {
        binding.etTanggalMasuk.setEnabled(true);
        binding.etTanggalMasuk.setFocusable(false);


        binding.etYear.setEnabled(true);
        binding.etYear.setFocusable(false);
    }

    private void getCarComponent() {
        binding.progressLoad.setVisibility(View.VISIBLE);
        binding.lrEmpty.setVisibility(View.GONE);
        binding.mainScrollss.setVisibility(View.GONE);
        carViewModel.getCarComponent().observe(getViewLifecycleOwner(), new Observer<ResponseModel<CarComponentModel>>() {
            @Override
            public void onChanged(ResponseModel<CarComponentModel> carComponentModelResponseModel) {
                binding.progressLoad.setVisibility(View.GONE);
                if (carComponentModelResponseModel.getState().equals(SuccessMsg.SUCCESS_STATE)
                && carComponentModelResponseModel.getData() != null) {
                    binding.mainScrollss.setVisibility(View.VISIBLE);
                    setUpSpinner(carComponentModelResponseModel.getData());

                }else {
                    binding.mainScrollss.setVisibility(View.GONE);
                    binding.lrEmpty.setVisibility(View.VISIBLE);

                }
            }
        });


    }

    private void setUpSpinner(CarComponentModel carComponentModel) {


        if (
                carComponentModel != null
                && carComponentModel.getDataBahanBakar() != null
                && carComponentModel.getDataBody() != null
                && carComponentModel.getDataKapasitasMesin() != null
                && carComponentModel.getDataMerk() != null
                && carComponentModel.getDataTangki() != null
                && carComponentModel.getDataTransmisi() != null
                && carComponentModel.getDataWarna() != null
                && carComponentModel.getDataKapasitasPenumpang() != null
        ) {
            spinnerIsComplete = true;

//
            SpinnerAdapter<BahanBakarModel> spinnerBahanBakar = new SpinnerAdapter<>(requireContext(), carComponentModel.getDataBahanBakar());
            binding.spinnerBahanBakar.setAdapter(spinnerBahanBakar);

            SpinnerAdapter<BodyModel> spinnerBody = new SpinnerAdapter<>(requireContext(), carComponentModel.getDataBody());
            binding.spinnerBody.setAdapter(spinnerBody);

            SpinnerAdapter<KapasitasPenumpangModel> kpmpSpinner  = new SpinnerAdapter<>(requireContext(), carComponentModel.getDataKapasitasPenumpang());
            binding.spinnerPassengerCapacity.setAdapter(kpmpSpinner);

            SpinnerAdapter<MerkModel> spinnerMerk  = new SpinnerAdapter<>(requireContext(), carComponentModel.getDataMerk());
            binding.spinnerMerk.setAdapter(spinnerMerk);

            SpinnerAdapter<TankModel> tankSpinner = new SpinnerAdapter<>(requireContext(), carComponentModel.getDataTangki());
            binding.spinnerTankCapacity.setAdapter(tankSpinner);

            SpinnerAdapter<TransmisiModel> spinnerTransmistion = new SpinnerAdapter<>(requireContext(), carComponentModel.getDataTransmisi());
            binding.spinnerTransmisi.setAdapter(spinnerTransmistion);

            SpinnerAdapter<ColorModel> spinnerColor = new SpinnerAdapter<>(requireContext(), carComponentModel.getDataWarna());
            binding.spinnerColor.setAdapter(spinnerColor);


            SpinnerAdapter<KapasitaMesinModel> spinnerKpm= new SpinnerAdapter<>(requireContext(), carComponentModel.getDataKapasitasMesin());
            binding.spinnerEngineCapacity.setAdapter(spinnerKpm);





            binding.spinnerBahanBakar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    bahanBakarId = carComponentModel.getDataBahanBakar().get(position).getBahan_bakar_id();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            binding.spinnerBody.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    bodyId = carComponentModel.getDataBody().get(position).getBody_id();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            binding.spinnerMerk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    merkId = carComponentModel.getDataMerk().get(position).getMerk_id();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            binding.spinnerPassengerCapacity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    kpmpId = carComponentModel.getDataKapasitasPenumpang().get(position).getKp_id();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            binding.spinnerTankCapacity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    tangkiId = carComponentModel.getDataTangki().get(position).getTangki_id();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            binding.spinnerTankCapacity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    tangkiId = carComponentModel.getDataTangki().get(position).getTangki_id();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            binding.spinnerTransmisi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    transId = carComponentModel.getDataTransmisi().get(position).getTransmisi_id();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            binding.spinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   colorId = carComponentModel.getDataWarna().get(position).getWarna_id();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            binding.spinnerEngineCapacity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    kpmId = carComponentModel.getDataKapasitasMesin().get(position).getKm_id();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });







        }else {
            spinnerIsComplete = false;
            showToast(ErrorMsg.SOMETHING_WENT_WRONG);
        }
    }


    private void listener() {
        binding.btnBack.setOnClickListener(view -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        binding.btnSubmit.setOnClickListener(view -> {
            if (spinnerIsComplete) {
                validateInput();
            }else {
                showToast("Terjadi kesalahan saat memuat data komponen mobil");
            }
        });

        binding.ivAddFrontImg.setOnClickListener(view -> {
            stateImgPicker = "front";
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        binding.ivAddEndImg.setOnClickListener(view -> {
            stateImgPicker = "back";
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        binding.ivAddLeftImg.setOnClickListener(view -> {
            if (checkPermissionUploadImage()) {
                stateImgPicker = "left";
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });


        binding.ivAddRightImg.setOnClickListener(view -> {
            stateImgPicker = "right";
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());

        });

        binding.ivAddDet1Img.setOnClickListener(view -> {
            stateImgPicker = "det1";
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        binding.ivAddDet2Img.setOnClickListener(view -> {
            stateImgPicker = "det2";
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        binding.cvDeleteFrontImg.setOnClickListener(view -> {
            deleteImage(uriFrontImg, binding.ivAddFrontImg, binding.rlFrontImg);
        });

        binding.cvDeleteEndImg.setOnClickListener(view -> {
            deleteImage(uriBackImg, binding.ivAddEndImg, binding.rlBackImg);
        });

        binding.cvDeleteLeftImg.setOnClickListener(view -> {
            deleteImage(uriLeftImg, binding.ivAddLeftImg, binding.rlLeftImg);
        });

        binding.cvDeleteRightImg.setOnClickListener(view -> {
            deleteImage(uriRightImg, binding.ivAddRightImg, binding.rlRightImg);
        });

        binding.cvDeleteDet1Img.setOnClickListener(view -> {
            deleteImage(uriDet1Img, binding.ivAddDet1Img, binding.rlDet1);
        });

        binding.cvDeleteDet2Img.setOnClickListener(view -> {
            deleteImage(uriDet2Img, binding.ivAddDet2Img, binding.rlDet2);
        });


        binding.etHargaBeli.addTextChangedListener(new TextWatcher() {
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
                    binding.etHargaBeli.removeTextChangedListener(this);

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
                    binding.etHargaBeli.setText(formatted);
                    binding.etHargaBeli.setSelection(formatted.length());
                    binding.etHargaBeli.addTextChangedListener(this);
                }
            }
        });

        binding.etBiayaPerbaikan.addTextChangedListener(new TextWatcher() {
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
                    binding.etBiayaPerbaikan.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[Rp,.]", "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }

                    String formatted = formatRupiah.format(parsed);

                    current = formatted;
                    binding.etBiayaPerbaikan.setText(formatted);
                    binding.etBiayaPerbaikan.setSelection(formatted.length());
                    binding.etBiayaPerbaikan.addTextChangedListener(this);
                }
            }
        });


        binding.etHargaJual.addTextChangedListener(new TextWatcher() {
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
                    binding.etHargaJual.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[Rp,.]", "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }

                    String formatted = formatRupiah.format(parsed);

                    current = formatted;
                    binding.etHargaJual.setText(formatted);
                    binding.etHargaJual.setSelection(formatted.length());
                    binding.etHargaJual.addTextChangedListener(this);
                }
            }
        });

        binding.etDiskon.addTextChangedListener(new TextWatcher() {
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
                    binding.etDiskon.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[Rp,.]", "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }

                    String formatted = formatRupiah.format(parsed);

                    current = formatted;
                    binding.etDiskon.setText(formatted);
                    binding.etDiskon.setSelection(formatted.length());
                    binding.etDiskon.addTextChangedListener(this);
                }
            }
        });

        binding.etKm.addTextChangedListener(new TextWatcher() {
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
                    binding.etKm.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[Rp,.]", "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }

                    String formatted = formatRupiah.format(parsed);

                    current = formatted;
                    binding.etKm.setText(formatted);
                    binding.etKm.setSelection(formatted.length());
                    binding.etKm.addTextChangedListener(this);
                }
            }
        });



        binding.etTanggalMasuk.setOnClickListener(view -> {
            getDatePicker(binding.etTanggalMasuk);
        });

        binding.etYear.setOnClickListener(view -> {
            getYearPicker(binding.etYear);
        });

        binding.btnClosePreviewPhoto.setOnClickListener(view -> {
            hideOverlay();
        });

        binding.ivFrontCar.setOnClickListener(view -> {
            previewPhoto(uriFrontImg);
        });


        binding.ivEndCar.setOnClickListener(view -> {
            previewPhoto(uriBackImg);
        });


        binding.ivLeftCar.setOnClickListener(view -> {
            previewPhoto(uriLeftImg);
        });


        binding.ivRightCar.setOnClickListener(view -> {
            previewPhoto(uriRightImg);
        });


        binding.ivDet1Car.setOnClickListener(view -> {
            previewPhoto(uriDet1Img);
        });

        binding.ivDet2Car.setOnClickListener(view -> {
            previewPhoto(uriDet2Img);
        });






    }

    private void validateInput() {
        if (merkId == 0) {
            binding.spinnerMerk.setErrorText("Tidak boleh kosong");
            return;
        }

        if (bodyId == 0 ) {
            binding.spinnerBody.setErrorText("Tidak boleh kosong");
            return;
        }

        if (binding.etModel.getText().toString().isEmpty()) {
            binding.tilModel.setError("Tidak boleh kosong");
            return;
        }

        if (binding.etPlatNumber.getText().toString().isEmpty()) {
            binding.tilPlatNumber.setError("Tidak boleh kosong");
            return;
        }

        if (binding.etEngineNumber.getText().toString().isEmpty()) {
            binding.tilEngineNumber.setError("Tidak boleh kosong");
            return;
        }

        if (binding.etNoRangka.getText().toString().isEmpty()) {
            binding.tilNoRangka.setError("Tidak boleh kosong");
            return;
        }

        if (binding.etYear.getText().toString().isEmpty()) {
            binding.tilYear.setError("Tidak boleh kosong");
            return;
        }

        if (binding.etPlatNumber.getText().toString().isEmpty()) {
            binding.tilPlatNumber.setError("Tidak boleh kosong");
            return;
        }

        if (colorId == 0 ) {
            binding.spinnerColor.setErrorText("Tidak boleh kosong");
            return;
        }

        if (kpmId == 0 ) {
            binding.spinnerEngineCapacity.setErrorText("Tidak boleh kosong");
            return;
        }

        if (bahanBakarId == 0 ) {
            binding.spinnerBahanBakar.setErrorText("Tidak boleh kosong");
            return;
        }

        if (transId == 0 ) {
            binding.spinnerTransmisi.setErrorText("Tidak boleh kosong");
            return;
        }

        if (kpmpId == 0 ) {
            binding.spinnerPassengerCapacity.setErrorText("Tidak boleh kosong");
            return;
        }

        if (binding.etKm.getText().toString().isEmpty()) {
            binding.tilKm.setError("Tidak boleh kosong");
            return;
        }

        if (tangkiId == 0 ) {
            binding.spinnerTankCapacity.setErrorText("Tidak boleh kosong");
            return;
        }

        if (binding.etHargaBeli.getText().toString().isEmpty()) {
            binding.tilHargaBeli.setError("Tidak boleh kosong");
            return;
        }

        if (binding.etHargaJual.getText().toString().isEmpty()) {
            binding.tilHargaJual.setError("Tidak boleh kosong");
            return;
        }

        if (binding.etBiayaPerbaikan.getText().toString().isEmpty()) {
            binding.tilBiayaPerbaikan.setError("Tidak boleh kosong");
            return;
        }

        if (binding.etTanggalMasuk.getText().toString().isEmpty()) {
            binding.tilTglMasuk.setError("Tidak boleh kosong");
            return;
        }

        if (binding.etDiskon.getText().toString().isEmpty()) {
            binding.tilDiskon.setError("Tidak boleh kosong");
            return;
        }

        if (binding.etDesc.getText().toString().isEmpty()) {
            binding.tilLinkDesc.setError("Tidak boleh kosong");
            return;
        }

        if (uriFrontImg == null) {
            showToast("Anda belum memilih gambar mobil depan");
            return;
        }


        if (uriBackImg == null) {
            showToast("Anda belum memilih gambar mobil belakang");
            return;
        }

        if (uriLeftImg == null) {
            showToast("Anda belum memilih gambar mobil kiri");
            return;
        }

        if (uriRightImg == null) {
            showToast("Anda belum memilih gambar mobil kanan");
            return;
        }

        if (uriDet1Img == null) {
            showToast("Anda belum memilih gambar detail mobil 1");
            return;
        }

        if (uriDet2Img == null) {
            showToast("Anda belum memilih gambar detail mobil 2");
            return;
        }

        collectDataInput();





    }

    private void collectDataInput() {
        HashMap map = new HashMap();
        map.put("merk_id", RequestBody.create(MediaType.parse("text/plain"),  String.valueOf(merkId)));
        map.put("body_id", RequestBody.create(MediaType.parse("text/plain"),  String.valueOf(bodyId)));
        map.put("nama_model", RequestBody.create(MediaType.parse("text/plain"),  binding.etModel.getText().toString()));
        map.put("no_plat", RequestBody.create(MediaType.parse("text/plain"),  binding.etPlatNumber.getText().toString()));
        map.put("no_mesin", RequestBody.create(MediaType.parse("text/plain"),  binding.etEngineNumber.getText().toString()));
        map.put("no_rangka", RequestBody.create(MediaType.parse("text/plain"),  binding.etNoRangka.getText().toString()));
        map.put("tahun", RequestBody.create(MediaType.parse("text/plain"),  binding.etYear.getText().toString()));
        map.put("warna_id", RequestBody.create(MediaType.parse("text/plain"),  String.valueOf(colorId)));
        map.put("km", RequestBody.create(MediaType.parse("text/plain"), binding.etKm.getText().toString()));
        map.put("km_id", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(kpmId)));
        map.put("bahan_bakar_id", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(bahanBakarId)));
        map.put("transmisi_id", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(transId)));
        map.put("kp_id", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(kpmpId)));
        map.put("tangki_id", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(tangkiId)));
        map.put("harga_beli", RequestBody.create(MediaType.parse("text/plain"), binding.etHargaBeli.getText().toString()));
        map.put("biaya_perbaikan", RequestBody.create(MediaType.parse("text/plain"), binding.etBiayaPerbaikan.getText().toString()));
        map.put("harga_jual", RequestBody.create(MediaType.parse("text/plain"), binding.etHargaJual.getText().toString()));
        map.put("diskon", RequestBody.create(MediaType.parse("text/plain"), binding.etDiskon.getText().toString()));
        map.put("tgl_masuk", RequestBody.create(MediaType.parse("text/plain"), binding.etTanggalMasuk.getText().toString()));
        map.put("deskripsi", RequestBody.create(MediaType.parse("text/plain"), binding.etDesc.getText().toString()));

        if (!binding.etLinkFb.getText().toString().isEmpty()) {
            map.put("url_facebook", RequestBody.create(MediaType.parse("text/plain"), binding.etLinkFb.getText().toString()));
        }

        if (!binding.etLinkIg.getText().toString().isEmpty()) {
            map.put("url_instagram", RequestBody.create(MediaType.parse("text/plain"), binding.etLinkIg.getText().toString()));
        }

        if (!binding.etLinYt.getText().toString().isEmpty()) {
            map.put("url_youtube", RequestBody.create(MediaType.parse("text/plain"), binding.etLinYt.getText().toString()));
        }

        if (!binding.etOwner.getText().toString().isEmpty()) {
            map.put("nama_pemilik", RequestBody.create(MediaType.parse("text/plain"), binding.etOwner.getText().toString()));
        }

        List<MultipartBody.Part> partList = new ArrayList<>();
        RequestBody requestBodyFrontImg = RequestBody.create(MediaType.parse("image/*"), contentResolver(uriFrontImg));
        RequestBody requestBodyBackImg = RequestBody.create(MediaType.parse("image/*"), contentResolver(uriBackImg));
        RequestBody requestBodyLeftImg = RequestBody.create(MediaType.parse("image/*"), contentResolver(uriLeftImg));
        RequestBody requestBodyRightImg = RequestBody.create(MediaType.parse("image/*"), contentResolver(uriRightImg));
        RequestBody requestBodyDet1Img = RequestBody.create(MediaType.parse("image/*"), contentResolver(uriDet1Img));
        RequestBody requestBodyDet2Img = RequestBody.create(MediaType.parse("image/*"), contentResolver(uriDet2Img));


        MultipartBody.Part partFront = MultipartBody.Part.createFormData("gambar1", getFileNameFromUri(uriFrontImg), requestBodyFrontImg);
        MultipartBody.Part partRight = MultipartBody.Part.createFormData("gambar2", getFileNameFromUri(uriRightImg), requestBodyRightImg);
        MultipartBody.Part partBack = MultipartBody.Part.createFormData("gambar3", getFileNameFromUri(uriBackImg), requestBodyBackImg);
        MultipartBody.Part partLeft = MultipartBody.Part.createFormData("gambar4", getFileNameFromUri(uriLeftImg), requestBodyLeftImg);
        MultipartBody.Part partDet1 = MultipartBody.Part.createFormData("gambar5", getFileNameFromUri(uriDet1Img), requestBodyDet1Img);
        MultipartBody.Part partDet2 = MultipartBody.Part.createFormData("gambar6", getFileNameFromUri(uriDet2Img), requestBodyDet2Img);

        partList.add(partFront);
        partList.add(partBack);
        partList.add(partLeft);
        partList.add(partRight);
        partList.add(partDet1);
        partList.add(partDet2);

        storeCar(map, partList);

    }

    private void storeCar(Map<String, RequestBody> map, List<MultipartBody.Part> partList) {
        binding.progressSubmit.setVisibility(View.VISIBLE);
        binding.btnSubmit.setVisibility(View.GONE);
        adminCarViewModel.storeCar(map, partList).observe(getViewLifecycleOwner(), new Observer<ResponseModel>() {
            @Override
            public void onChanged(ResponseModel responseModel) {
                binding.btnSubmit.setVisibility(View.VISIBLE);
                binding.progressSubmit.setVisibility(View.GONE);
                if (responseModel != null && responseModel.getState().equals(SuccessMsg.SUCCESS_STATE)) {

                    fragmentTransaction(new CarFragment());
                    CookieBar.build(requireActivity())
                            .setTitle("Berhasil")
                            .setMessage("Berhasil menambahkan mobil baru")
                            .setCookiePosition(CookieBar.BOTTOM)
                            .setDuration(3000)

                            .show();



                }else {
                    showToast(responseModel.getMessage());
                }
            }
        });

    }


    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    switch (stateImgPicker) {
                        case "front":
                            uriFrontImg = uri;
                            setImgUri(uri, binding.ivFrontCar, binding.rlFrontImg, binding.ivAddFrontImg);
                            break;
                        case "back" :
                            uriBackImg = uri;
                            setImgUri(uri, binding.ivEndCar, binding.rlBackImg, binding.ivAddEndImg);

                            break;
                        case "left" :
                            uriLeftImg = uri;
                            setImgUri(uri, binding.ivLeftCar,binding.rlLeftImg, binding.ivAddLeftImg);

                            break;

                        case "right" :
                            uriRightImg = uri;
                            setImgUri(uri, binding.ivRightCar, binding.rlRightImg, binding.ivAddRightImg);

                            break;

                        case "det1" :
                            uriDet1Img = uri;
                            setImgUri(uri, binding.ivDet1Car, binding.rlDet1, binding.ivAddDet1Img);

                            break;

                        case "det2" :
                            uriDet2Img = uri;
                            setImgUri(uri, binding.ivDet2Car, binding.rlDet2, binding.ivAddDet2Img);

                            break;
                        default:
                            break;
                    }

                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });

    private void checkAndRequestStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Minta izin secara runtime
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1002);

        }
    }

    private void deleteImage(Uri uri,  ImageView ivAdd, RelativeLayout rlAdd) {
        uri = null;
        ivAdd.setVisibility(View.VISIBLE);
        rlAdd.setVisibility(View.GONE);

    }

    private void setImgUri(Uri uri, ImageView iv, RelativeLayout rlAdd, ImageView ivAdd) {
        iv.setImageURI(uri);
        ivAdd.setVisibility(View.GONE);
        rlAdd.setVisibility(View.VISIBLE);
    }

    private void getDatePicker(TextView tvDate) {
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


    private void getYearPicker(TextView tvDate) {
        // Mendapatkan tahun sekarang
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        // Membuat dialog penampilan tahun
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Pilih Tahun");


        NumberPicker yearPicker = new NumberPicker(getContext());
        yearPicker.setMinValue(1900);
        yearPicker.setMaxValue(2100);
        yearPicker.setValue(currentYear);

        // Mengatur aksi ketika nilai diubah
        yearPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            // Mengupdate TextView dengan tahun yang dipilih
            tvDate.setText(String.valueOf(newVal));
        });


        builder.setPositiveButton("OK", (dialog, which) -> {

        });


        builder.setView(yearPicker);


        // Menampilkan dialog
        builder.show();
    }


    private void previewPhoto(Uri uri) {
        binding.photoView.setImageURI(uri);
        showOverlay();

    }

    private void showOverlay() {
        binding.rlPhotoView.setVisibility(View.VISIBLE);
        binding.vOverlay.setVisibility(View.VISIBLE);
    }

    private void hideOverlay() {
        binding.rlPhotoView.setVisibility(View.GONE);
        binding.vOverlay.setVisibility(View.GONE);
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

    private boolean checkPermissionUploadImage() {
        // check permission valid
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;

        } else {
            // show notif
            CookieBar.build(requireActivity())
                    .setTitle("Akses ditolak")
                    .setMessage("Silahkan berikan izin akses aplikasi ini")
                    .setCookiePosition(CookieBar.BOTTOM)
                    .setAction("Pengaturan", new OnActionClickListener() {
                        @Override
                        public void onClick() { // buka halaaman setting
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    })
                    .setDuration(3000)

                    .show();
            return false;

        }
    }

//    private void resetInput() {
//        merkId = 0;
//        bodyId = 0;
//        binding.etModel.setText("");
//        binding.etNoRangka.setText("");
//        binding.etPlatNumber.setText("");
//        binding.etEngineNumber.setText("");
//        binding.etYear.setText("");
//        colorId = 0;
//        kpmpId = 0;
//        kpmId = 0;
//        bahanBakarId = 0;
//        transId = 0;
//        tangkiId = 0;
//        binding.etKm.setText("0");
//        binding.etHargaJual.setText("0");
//        binding.etHargaBeli.setText("0");
//        binding.etBiayaPerbaikan.setText("0");
//        binding.etDiskon.setText("0");
//        binding.etOwner.setText("");
//        binding.etTanggalMasuk.setText("");
//        binding.etDesc.setText("");
//        binding.etLinYt.setText("");
//        binding.etLinkIg.setText("");
//        binding.etLinkFb.setText("");
//
//        binding.spinnerMerk.clearSelection();
//        binding.spinnerBody.clearSelection();
//        binding.spinnerColor.clearSelection();
//        binding.spinnerEngineCapacity.clearSelection();
//        binding.spinnerBahanBakar.clearSelection();
//        binding.spinnerTransmisi.clearSelection();
//        binding.spinnerPassengerCapacity.clearSelection();
//        binding.spinnerTankCapacity.clearSelection();
//
//        deleteImage(uriFrontImg, binding.ivAddFrontImg, binding.rlFrontImg);
//        deleteImage(uriBackImg, binding.ivAddEndImg, binding.rlBackImg);
//        deleteImage(uriLeftImg, binding.ivAddLeftImg, binding.rlLeftImg);
//        deleteImage(uriRightImg, binding.ivAddRightImg, binding.rlRightImg);
//        deleteImage(uriDet1Img, binding.ivAddDet1Img, binding.rlDet1);
//        deleteImage(uriDet2Img, binding.ivAddDet2Img, binding.rlDet2);
//    }

    private void fragmentTransaction(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameHomeAdmin, fragment)
                .commit();
    }



    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}