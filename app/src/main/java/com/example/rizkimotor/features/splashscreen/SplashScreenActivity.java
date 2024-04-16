package com.example.rizkimotor.features.splashscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rizkimotor.data.services.UserService;
import com.example.rizkimotor.features.home.admin.actvities.AdminHomeActivity;
import com.example.rizkimotor.features.home.user.ui.activities.HomeActivity;
import com.example.rizkimotor.shared.SharedUserData;

public class SplashScreenActivity extends AppCompatActivity {
    private UserService userService;
    private int role = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        initUserService();

    }

    private void initUserService() {
        try {
            userService = new UserService();
            userService.initService(this);
            role = userService.loadInt(SharedUserData.PREF_ROLE);
            validateRole(role);

        }catch (Throwable t) {
            activityTransaction(new HomeActivity());
        }
    }

    private void validateRole(int role) {
        if (role == 2) {
            activityTransaction(new AdminHomeActivity());
            return;
        }else if (role == 1) {
            activityTransaction(new HomeActivity());
        }else {
            activityTransaction(new HomeActivity());
        }
    }

    private void activityTransaction(Activity activity) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, activity.getClass()));
                finish();
            }
        }, 200L);

    }


}