package com.example.rizkimotor.features.home.admin.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rizkimotor.R;
import com.example.rizkimotor.data.services.UserService;
import com.example.rizkimotor.databinding.FragmentAdminHomeBinding;
import com.example.rizkimotor.features.auth.ui.activities.AuthActivity;
import com.example.rizkimotor.shared.SharedUserData;

public class AdminHomeFragment extends Fragment {
    private FragmentAdminHomeBinding binding;
    private UserService userService = new UserService();
    private String photoProfile;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userServiceInit();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminHomeBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void userServiceInit() {
        try {
            userService.initService(requireContext());
            photoProfile = userService.loadString(SharedUserData.PREF_PHOTO_PROFILE, "default.png");

        }catch (Throwable t) {
            startActivity(new Intent(requireActivity(), AuthActivity.class));
            requireActivity().finish();
            showToast("Anda tidak memiliki akses");
        }

    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}