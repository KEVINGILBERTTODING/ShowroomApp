package com.example.rizkimotor.features.auth.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rizkimotor.R;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.services.UserService;
import com.example.rizkimotor.databinding.FragmentLoginBinding;
import com.example.rizkimotor.features.auth.model.user.UserModel;
import com.example.rizkimotor.features.auth.viewmodel.AuthViewModel;
import com.example.rizkimotor.features.home.user.ui.fragments.HomeFragment;
import com.example.rizkimotor.shared.SharedUserData;
import com.example.rizkimotor.util.contstans.Constants;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;
import com.google.android.material.textfield.TextInputLayout;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private AuthViewModel authViewModel;
    private UserService userService;
    private String TAG = Constants.LOG;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceInit();
        init();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        listener();

        // Inflate the layout for this fragment
        return binding.getRoot();

    }

    private void init() {



        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

    }


    private void listener() {
        binding.btnLogin.setOnClickListener(view -> {

            inputValidation();
        });

        binding.tvRegister.setOnClickListener(view -> {
            Fragment fragment = new RegisterFragment();
            fragmentTransaction(fragment);
        });
    }

    private void inputValidation() {
        if (binding.etEmail.getText().toString().isEmpty() || binding.etEmail.getText().toString().equals("")) {
            setErrorInput( binding.tilEmail,"Email tidak boleh kosong");
            return;
        }

        if (binding.etPassword.getText().toString().isEmpty() || binding.etPassword.getText().toString().equals("")) {
            setErrorInput(binding.tilPassword,"Kata sandi tidak boleh kosong");
            return;
        }

        if (binding.etPassword.getText().toString().length() < 8) {
            setErrorInput(binding.tilPassword,"Kata sandi tidak boleh kurang dari 8 karakter");
            return;
        }

        login();


    }

    private void setErrorInput(TextInputLayout til, String message) {
        til.setError(message);
        return;
    }

    private void serviceInit() {
        userService = new UserService();
        userService.initService(requireContext());
    }



    private void login() {
        binding.progressCircular.setVisibility(View.VISIBLE);
        binding.btnLogin.setVisibility(View.GONE);
        authViewModel.login(
                binding.etEmail.getText().toString(),
                binding.etPassword.getText().toString()).observe(getViewLifecycleOwner(), new Observer<ResponseModel<UserModel>>() {
            @Override
            public void onChanged(ResponseModel<UserModel> userModelResponseModel) {
                binding.progressCircular.setVisibility(View.GONE);
                binding.btnLogin.setVisibility(View.VISIBLE);
                if (userModelResponseModel.getState().equals(SuccessMsg.SUCCESS_MSG) && userModelResponseModel.getData() != null) {
                    if (userModelResponseModel.getData().getUser_id() != 0 && userModelResponseModel.getData().getNama_lengkap() != null) {


                        userService.saveInt(SharedUserData.PREF_USER_ID, userModelResponseModel.getData().getUser_id());
                        userService.saveString(SharedUserData.PREF_USERNAME, userModelResponseModel.getData().getNama_lengkap());
                        userService.saveString(SharedUserData.PREF_EMAIL, userModelResponseModel.getData().getEmail());
                        userService.saveBool(SharedUserData.PREF_IS_LOGIN, true);

                        fragmentTransaction(new HomeFragment());

                    }else {
                        showToast(ErrorMsg.SOMETHING_WENT_WRONG);
                    }
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