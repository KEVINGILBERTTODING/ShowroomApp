package com.example.rizkimotor.features.auth.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.rizkimotor.R;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.databinding.FragmentRegistersBinding;
import com.example.rizkimotor.features.auth.viewmodel.AuthViewModel;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterFragment extends Fragment {

    private FragmentRegistersBinding binding;

    private AuthViewModel authViewModel;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegistersBinding.inflate(inflater, container, false);
        listener();

        // Inflate the layout for this fragment
        return binding.getRoot();

    }

    private void init() {

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

    }


    private void listener() {
        binding.btnRegister.setOnClickListener(view -> {

            inputValidation();
        });

        binding.tvRegister.setOnClickListener(view -> {
            Fragment fragment = new LoginFragment();
            fragmentTransaction(fragment);
        });


    }

    private void inputValidation() {

        if (binding.etEmail.getText().toString().isEmpty() || binding.etEmail.getText().toString().equals("")) {
            setErrorInput( binding.tilEmail,"Email tidak boleh kosong");
            return;
        }

        if (binding.etFullName.getText().toString().isEmpty() || binding.etEmail.getText().toString().equals("")) {
            setErrorInput( binding.tilEmail,"Nama lengkap tidak boleh kosong");
            return;
        }

        if (binding.etPassword.getText().toString().isEmpty() || binding.etPassword.getText().toString().equals("")) {
            setErrorInput(binding.tilPassword,"Kata sandi tidak boleh kosong");
            return;
        }

        if (binding.etPasswordCofirm.getText().toString().isEmpty() || binding.etPassword.getText().toString().equals("")) {
            setErrorInput(binding.tilPasswordConfirm,"Kata sandi konfirmasi tidak boleh kosong");
            return;
        }

        if (binding.etPassword.getText().toString().length() < 8) {
            setErrorInput(binding.tilPassword,"Kata sandi tidak boleh kurang dari 8 karakter");
            return;
        }

        if (!binding.etPassword.getText().toString().equals(binding.etPasswordCofirm.getText().toString())) {
            setErrorInput(binding.tilPassword,"Kata sandi tidak sesuai");
            return;
        }

        register();


    }

    private void setErrorInput(TextInputLayout til, String message) {
        til.setError(message);
        return;
    }



    private void register() {
        binding.progressCircular.setVisibility(View.VISIBLE);
        binding.btnRegister.setVisibility(View.GONE);
        HashMap hashMap = new HashMap();
        hashMap.put("email", binding.etEmail.getText().toString());
        hashMap.put("nama_lengkap", binding.etFullName.getText().toString());
        hashMap.put("password", binding.etPassword.getText().toString());
        authViewModel.register(
                    hashMap
                ).observe(getViewLifecycleOwner(), new Observer<ResponseModel>() {
            @Override
            public void onChanged(ResponseModel userModelResponseModel) {
                binding.progressCircular.setVisibility(View.GONE);
                binding.btnRegister.setVisibility(View.VISIBLE);
                if (userModelResponseModel.getState().equals(SuccessMsg.SUCCESS_MSG)) {
                    showToast("Berhasil mendaftar");
                    fragmentTransaction(new LoginFragment());
                }else if (userModelResponseModel.getState().equals(ErrorMsg.ERR_STATE)){
                    showToast(userModelResponseModel.getMessage());
                }else {
                    showToast(userModelResponseModel.getMessage());

                }
            }
        });

    }

    private void fragmentTransaction(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAuth, fragment)
                .commit();
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

}