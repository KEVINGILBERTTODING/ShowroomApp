package com.example.rizkimotor.features.home.admin.actvities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.rizkimotor.R;
import com.example.rizkimotor.databinding.ActivityAdminHomeBinding;
import com.example.rizkimotor.features.home.admin.fragments.AdminHomeFragment;
import com.example.rizkimotor.features.home.user.ui.fragments.HomeFragment;
import com.example.rizkimotor.features.profile.user.ui.fragments.UserProfileFragment;
import com.example.rizkimotor.features.search.user.ui.fragments.SearchFragment;
import com.example.rizkimotor.features.transactions.user.ui.fragments.UserHistoryTransaction;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class AdminHomeActivity extends AppCompatActivity {
    private ActivityAdminHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
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
                    fragmentTransaction(new HomeFragment());
                } else if (i1 == 2) {
                    fragmentTransaction(new UserHistoryTransaction());
                } else if (i1 == 1) {
                    fragmentTransaction(new SearchFragment());
                } else if (i1 == 3) {
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