package com.example.rizkimotor.features.car.user.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rizkimotor.databinding.FragmentPhotoPreviewBinding;

public class PhotoPreviewFragment extends Fragment {
    private FragmentPhotoPreviewBinding binding;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPhotoPreviewBinding.inflate(inflater, container, false);

        if (getArguments().getString("url") != null) {
            Glide.with(requireContext())
                    .load(getArguments().getString("url"))

                    .into(binding.photoView);
        }else {
            Toast.makeText(requireContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
        }
        return binding.getRoot();
    }
}