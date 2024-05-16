package com.example.rizkimotor.features.auth.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rizkimotor.features.home.user.ui.fragments.HomeFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.example.rizkimotor.R;
import com.example.rizkimotor.data.model.ResponseModel;
import com.example.rizkimotor.data.services.UserService;
import com.example.rizkimotor.databinding.FragmentLoginBinding;
import com.example.rizkimotor.features.auth.model.user.UserModel;
import com.example.rizkimotor.features.auth.viewmodel.AuthViewModel;
import com.example.rizkimotor.features.home.admin.ui.actvities.AdminHomeActivity;
import com.example.rizkimotor.features.home.user.ui.activities.HomeActivity;
import com.example.rizkimotor.shared.SharedUserData;
import com.example.rizkimotor.util.contstans.Constants;
import com.example.rizkimotor.util.contstans.err.ErrorMsg;
import com.example.rizkimotor.util.contstans.success.SuccessMsg;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.aviran.cookiebar2.CookieBar;
import org.aviran.cookiebar2.OnActionClickListener;

import java.util.HashMap;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private AuthViewModel authViewModel;
    private UserService userService;
    private String TAG = Constants.LOG;
    private UserModel userModel;



    private ActivityResultLauncher<Intent> signInLauncher;





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
        initFirebase();


        // Inflate the layout for this fragment
        return binding.getRoot();

    }

    private void init() {



        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

    }

    private void initFirebase() {
        // Inisialisasi Activity Result Launcher
        signInLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent data = result.getData();
                    handleGoogleSignInResult(data);
                });
    }


    private void listener() {
        binding.btnLogin.setOnClickListener(view -> {

            inputValidation();
        });

        binding.tvRegister.setOnClickListener(view -> {
            Fragment fragment = new RegisterFragment();
            fragmentTransaction(fragment);
        });

        binding.btnLoginGoogle.setOnClickListener(view -> {
            signInWithGoogle();
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

    private void authGoogle(HashMap<String, String> map)  {
        binding.btnLoginGoogle.setVisibility(View.GONE);
        binding.loadingGoogle.setVisibility(View.VISIBLE);
        authViewModel.authGoogle(map).observe(getViewLifecycleOwner(), new Observer<ResponseModel<UserModel>>() {
            @Override
            public void onChanged(ResponseModel<UserModel> userModelResponseModel) {
                binding.btnLoginGoogle.setVisibility(View.VISIBLE);
                binding.loadingGoogle.setVisibility(View.GONE);
                if (userModelResponseModel != null && userModelResponseModel.getState().equals(SuccessMsg.SUCCESS_STATE)
                && userModelResponseModel.getData() != null) {
                    UserModel userModel1 = userModelResponseModel.getData();
                    saveUserInfo(userModel1);

                    startActivity(new Intent(requireActivity(), HomeActivity.class));
                    requireActivity().finish();
                    showToast("Selamat datang " + userModel1.getNama_lengkap());


                }else {
                    showToast(userModelResponseModel.getMessage());
                }
            }
        });

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


                        userModel = userModelResponseModel.getData();

                        saveUserInfo(userModel);




                        validateRole(userModelResponseModel.getData().getRole());


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



    private void validateRole(int role) {
        if (role == 0) {
            showToast(ErrorMsg.SOMETHING_WENT_WRONG);
            return;
        }

        if (role == 1) { // user
            activityTransaction(new HomeActivity());

            return;
        }

        if (role == 2) { // admin
            activityTransaction(new AdminHomeActivity());
            return;
        }

    }

    private void saveUserInfo(UserModel userModel) {
        userService.saveInt(SharedUserData.PREF_USER_ID, userModel.getUser_id());
        userService.saveString(SharedUserData.PREF_USERNAME, userModel.getNama_lengkap());
        userService.saveString(SharedUserData.PREF_EMAIL, userModel.getEmail());
        userService.saveString(SharedUserData.PREF_PHOTO_PROFILE, userModel.getProfile_photo());
        userService.saveBool(SharedUserData.PREF_IS_LOGIN, true);
        userService.saveInt(SharedUserData.PREF_ROLE, userModel.getRole());
    }

    private void activityTransaction(Activity activity) {
        startActivity(new Intent(requireActivity(), activity.getClass()));
        requireActivity().finish();
    }

    private void fragmentTransaction(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAuth, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void signInWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();


        signInLauncher.launch(signInIntent);
    }

    private void handleGoogleSignInResult(Intent data) {

        try {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = task.getResult(ApiException.class);
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            authViewModel.signInWithGoogle(credential);

            HashMap<String, String> map = new HashMap<>();
            map.put("nama_lengkap", account.getDisplayName());
            map.put("email", account.getEmail());
            map.put("profile_photo", account.getPhotoUrl().toString());
            Log.d(TAG, "handleGoogleSignInResult: " + map);

            authGoogle(map);

        } catch (ApiException e) {

            Log.d(TAG, "handleGoogleSignInResult: " + e);
            showToast(e.getMessage().toString());


        }
    }


    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

}