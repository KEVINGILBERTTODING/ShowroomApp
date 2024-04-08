package com.example.rizkimotor.features.home.user.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rizkimotor.R;
import com.example.rizkimotor.data.services.UserService;
import com.example.rizkimotor.databinding.FragmentHomeBinding;
import com.example.rizkimotor.shared.SharedUserData;

public class HomeFragment extends Fragment {

    private UserService userService;
    private FragmentHomeBinding binding;
    private String username;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceInit();
        init();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment

        binding.tvFullname.setText(username);
        return binding.getRoot();
    }

    private void init() {
        username = userService.loadString(SharedUserData.PREF_USERNAME);
    }

    private void serviceInit() {
        userService =  new UserService();
        userService.initService(requireContext());
    }
}