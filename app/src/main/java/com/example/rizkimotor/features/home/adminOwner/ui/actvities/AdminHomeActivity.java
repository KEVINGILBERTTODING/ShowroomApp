package com.example.rizkimotor.features.home.adminOwner.ui.actvities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.rizkimotor.R;
import com.example.rizkimotor.databinding.ActivityAdminHomeBinding;
import com.example.rizkimotor.features.car.admin.ui.fragments.CarFragment;
import com.example.rizkimotor.features.home.adminOwner.ui.fragments.AdminHomeFragment;
import com.example.rizkimotor.features.profile.user.ui.fragments.UserProfileFragment;
import com.example.rizkimotor.features.transactions.admin.ui.AdminTransactionFragment;

import dagger.hilt.android.AndroidEntryPoint;
import nl.joery.animatedbottombar.AnimatedBottomBar;
@AndroidEntryPoint
public class AdminHomeActivity extends AppCompatActivity {
    private ActivityAdminHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listener();
        fragmentTransaction(new AdminHomeFragment());



    }

    private void listener() {
        binding.bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NonNull AnimatedBottomBar.Tab tab1) {

                if (i1 == 0) {
                    fragmentTransaction(new AdminHomeFragment());
                }  else if (i1 == 1) {
                    fragmentTransaction(new CarFragment());
                }else if (i1 == 2) {
                    fragmentTransaction(new AdminTransactionFragment());
                }
                else if (i1 == 3) {
                    fragmentTransaction(new UserProfileFragment());
                }
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });
    }

    private void fragmentTransaction(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameHomeAdmin, fragment).commit();
    }
}