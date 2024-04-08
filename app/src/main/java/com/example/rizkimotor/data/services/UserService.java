package com.example.rizkimotor.data.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.rizkimotor.shared.SharedUserData;
import com.example.rizkimotor.util.contstans.Constants;

public class UserService extends Service {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String TAG = Constants.LOG;



    @Override
    public void onCreate() {
        super.onCreate();



    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void initService(Context context) {
        sharedPreferences = context.getSharedPreferences(SharedUserData.SHARED_PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveBool(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void saveString(String key, String value) {
        editor.putString(key, value);
        editor.apply();

    }


    public void saveInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();

    }

    public boolean loadBool(String key) {
        return sharedPreferences.getBoolean(key, false);

    }

    public String loadString(String key, String value) {
        return sharedPreferences.getString(key, value);

    }

    public int loadInt(String key) {
        return sharedPreferences.getInt(key, 0);



    }

    public void destroy() {
        editor.clear();
        editor.apply();


    }


}
