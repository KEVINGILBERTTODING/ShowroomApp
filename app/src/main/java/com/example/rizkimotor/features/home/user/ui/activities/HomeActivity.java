package com.example.rizkimotor.features.home.user.ui.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.rizkimotor.R;
import com.example.rizkimotor.databinding.ActivityHomeBinding;
import com.example.rizkimotor.features.home.user.ui.fragments.HomeFragment;
import com.example.rizkimotor.features.profile.user.ui.fragments.UserProfileFragment;
import com.example.rizkimotor.features.search.user.ui.fragments.SearchFragment;
import com.example.rizkimotor.features.transactions.user.ui.fragments.UserHistoryTransaction;

import dagger.hilt.android.AndroidEntryPoint;
import me.ibrahimsn.lib.OnItemSelectedListener;
import nl.joery.animatedbottombar.AnimatedBottomBar;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        fragmentTransaction(new HomeFragment());
        listener();


        setContentView(binding.getRoot());







    }

    private void listener() {
        binding.bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {

                if (i == 0) {
                    fragmentTransaction(new HomeFragment());
                } else if (i == 2) {
                    fragmentTransaction(new UserHistoryTransaction());
                } else if (i == 1) {
                    fragmentTransaction(new SearchFragment());
                } else if (i == 3) {
                    fragmentTransaction(new UserProfileFragment());
                }
                return false;
            }
        });
    }

    private void fragmentTransaction(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameHome, fragment).commit();
    }
}