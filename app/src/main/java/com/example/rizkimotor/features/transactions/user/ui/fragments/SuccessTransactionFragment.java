package com.example.rizkimotor.features.transactions.user.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rizkimotor.R;
import com.example.rizkimotor.databinding.FragmentSuccessCreditBinding;

public class SuccessTransactionFragment extends Fragment {
    private FragmentSuccessCreditBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSuccessCreditBinding.inflate(inflater, container, false);

        if (getArguments() != null) {

            binding.tvTitle.setText(getArguments().getString("title"));
            binding.tvDesc.setText(getArguments().getString("message"));

            binding.btnGotIt.setOnClickListener(view -> {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameHome, new UserHistoryTransaction())
                        .commit();


            });
        }


        return binding.getRoot();
    }
}