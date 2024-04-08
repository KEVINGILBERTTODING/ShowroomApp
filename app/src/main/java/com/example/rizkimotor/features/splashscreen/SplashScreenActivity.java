package com.example.rizkimotor.features.splashscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.rizkimotor.R;
import com.example.rizkimotor.data.services.UserService;
import com.example.rizkimotor.features.auth.ui.activities.AuthActivity;
import com.example.rizkimotor.features.home.user.ui.activities.HomeActivity;
import com.example.rizkimotor.shared.SharedUserData;

public class SplashScreenActivity extends AppCompatActivity {
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initService();
            }
        }, 200L);
    }

    private void initService() {
        userService = new UserService();
        userService.initService(this);

        if (userService.loadBool(SharedUserData.PREF_IS_LOGIN)) {
            startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
            finish();

        }else {
            startActivity(new Intent(SplashScreenActivity.this, AuthActivity.class));
            finish();


        }
    }
}