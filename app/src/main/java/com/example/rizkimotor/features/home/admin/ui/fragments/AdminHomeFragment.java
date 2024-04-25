package com.example.rizkimotor.features.home.admin.ui.fragments;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rizkimotor.R;
import com.example.rizkimotor.data.model.ResponseDownloaModel;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.services.UserService;
import com.example.rizkimotor.databinding.FragmentAdminHomeBinding;
import com.example.rizkimotor.features.auth.ui.activities.AuthActivity;
import com.example.rizkimotor.features.home.admin.model.ChartModel;
import com.example.rizkimotor.features.home.admin.model.MonthModel;
import com.example.rizkimotor.features.home.admin.viewmodel.ChartViewModel;
import com.example.rizkimotor.features.home.admin.viewmodel.TransactionViewModel;
import com.example.rizkimotor.shared.SharedUserData;
import com.example.rizkimotor.util.contstans.Constants;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.aviran.cookiebar2.CookieBar;
import org.aviran.cookiebar2.OnActionClickListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AdminHomeFragment extends Fragment {
    private FragmentAdminHomeBinding binding;
    private UserService userService = new UserService();
    private ChartViewModel chartViewModel;
    private String TAG, photoProfile;
    private int adminId, REQUEST_CODE_WRITE_EXTERNAL_STORAGE;
    private TransactionViewModel transactionViewModel;
    private List<String> monthList;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminHomeBinding.inflate(inflater, container, false);
        userServiceInit();
        init();
        setPhotoProfile();
        fetchDataChart();
        listener();
        return binding.getRoot();
    }

    private void listener() {
        binding.btnDownloadPemasukanReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadProfitPdf();
            }
        });

        binding.btnRefresh.setOnClickListener(view -> {
            fetchDataChart();
        });
    }

    private void init() {
        chartViewModel = new ViewModelProvider(this).get(ChartViewModel.class);
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        checkAndRequestStoragePermission();
    }

    private void fetchDataChart() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.chatProfit.setVisibility(View.GONE);
        binding.lrError.setVisibility(View.GONE);
        try {
            chartViewModel.getChart().observe(getViewLifecycleOwner(), new Observer<ResponseModel<ChartModel>>() {
                @Override
                public void onChanged(ResponseModel<ChartModel> chartModelResponseModel) {
                    binding.progressBar.setVisibility(View.GONE);
                    if (chartModelResponseModel != null && chartModelResponseModel.getState().equals(SuccessMsg.SUCCESS_STATE)
                    && chartModelResponseModel.getData() != null) {
                        ChartModel chartModel = chartModelResponseModel.getData();
                        setUpProfitChart(chartModel);
                        binding.chatProfit.setVisibility(View.VISIBLE);
                        binding.lrError.setVisibility(View.GONE);

                    }else {
                        binding.lrError.setVisibility(View.VISIBLE);

                        showToast(ErrorMsg.SOMETHING_WENT_WRONG);
                    }
                }
            });
        }catch (Throwable t) {
            showToast(ErrorMsg.SOMETHING_WENT_WRONG);
        }
    }

    private void setUpProfitChart(ChartModel chartModel) {
        Description description = new Description();
        description.setText("Grafik Total Pemasukan & Keuntungan");
        binding.chatProfit.setDescription(description);

        binding.chatProfit.getAxisRight().setDrawLabels(true);

        monthList = Arrays.asList(
                "Jan",
                "Feb",
                "Mar",
                "Apr",
                "Mei",
                "Jun",
                "Jul",
                "Agu",
                "Sep",
                "Okt",
                "Nov",
                "Des"
        );
        XAxis xAxis = binding.chatProfit.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(monthList));
        xAxis.setLabelCount(4);
        xAxis.setGranularity(1f);

        YAxis yAxis = new YAxis();
        yAxis.setAxisMaximum(0f);

        yAxis.setAxisLineWidth(4f);
        yAxis.setAxisLineColor(requireContext().getColor(R.color.black));
        yAxis.setLabelCount(10);



        List<Entry> dataPemasukan = new ArrayList<>();
        List<Entry> dataKeuntungan = new ArrayList<>();

        for (int i = 1; i < 12; i++) {
            dataPemasukan.add(new Entry(i -1, chartModel.getData_pemasukan().getMonthValue(i)));
            dataKeuntungan.add(new Entry(i -1, chartModel.getData_keuntungan().getMonthValue(i)));
        }


        LineDataSet lineDataSetPemasukan = new LineDataSet(dataPemasukan, "Data Pemasukan");
        lineDataSetPemasukan.setColor(requireContext().getColor(R.color.blue));


        LineDataSet lineDataSetKeuntungan = new LineDataSet(dataKeuntungan, "Data Keuntungan");
        lineDataSetKeuntungan.setColor(requireContext().getColor(R.color.green));

        LineData lineData = new LineData(lineDataSetPemasukan, lineDataSetKeuntungan);
        binding.chatProfit.setData(lineData);

        // Refresh chart
        binding.chatProfit.invalidate();


    }

    private void userServiceInit() {
        try {
            userService.initService(requireContext());
            adminId = userService.loadInt(SharedUserData.PREF_USER_ID);

            if (userService.loadBool(SharedUserData.PREF_IS_LOGIN) != true) {
                startActivity(new Intent(requireActivity(), AuthActivity.class));
                requireActivity().finish();


                showToast("Anda tidak memiliki akses");
            }

        }catch (Throwable t) {
            startActivity(new Intent(requireActivity(), AuthActivity.class));
            requireActivity().finish();


            showToast("Anda tidak memiliki akses");
        }

    }

    private void downloadProfitPdf() {
        if (adminId != 0) {
            binding.progressDownlloadProfit.setVisibility(View.VISIBLE);
            binding.btnDownloadPemasukanReport.setVisibility(View.GONE);
            transactionViewModel.downloadProfit(adminId).observe(getViewLifecycleOwner()
                    , new Observer<ResponseDownloaModel>() {
                        @Override
                        public void onChanged(ResponseDownloaModel responseModel) {
                            binding.progressDownlloadProfit.setVisibility(View.GONE);
                            binding.btnDownloadPemasukanReport.setVisibility(View.VISIBLE);
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

    private void setPhotoProfile() {
        Glide.with(requireContext())
                .load(userService.loadString(SharedUserData.PREF_PHOTO_PROFILE, "default.png"))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(70, 70)
                .into(binding.ivProfile);
    }

    private void checkAndRequestStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Minta izin secara runtime
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        }
    }


    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }



    public void savePdfToDownloadFolder(byte[] pdfData) {
        // Mendapatkan direktori folder Download
        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        // Membuat file PDF baru di folder Download
        File pdfFile = new File(downloadDir, "profit_report.pdf");

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



}