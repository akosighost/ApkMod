package com.apk.mod.io.Home.Intro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.apk.mod.io.Home.Extension.SystemUI;
import com.apk.mod.io.Home.Home.HomeActivity;
import com.apk.mod.io.Home.Permission.PermissionActivity;
import com.apk.mod.io.R;

public class IntroActivity extends AppCompatActivity {

    private static final long INTRO_DELAY = 3000;
    private final Intent intent = new Intent();
    private Handler handler;
    private int dotCount = 0;
    private TextView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        SystemUI.customizeSystemUI(this);
        handler = new Handler();
        startDotAnimation();
        loading = findViewById(R.id.loading);
    }
    @Override
    protected void onStart() {
        super.onStart();
        new Thread(() -> {
            SystemClock.sleep(INTRO_DELAY);
            runOnUiThread(() -> {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    intent.setClass(getApplicationContext(), PermissionActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                }
            });
        }).start();
    }
    private void startDotAnimation() {
        handler.postDelayed(() -> {
            dotCount = (dotCount + 1) % 4;
            updateText(dotCount);
            startDotAnimation();
        }, 500);
    }
    private void updateText(int dotCount) {
        StringBuilder dotsBuilder = new StringBuilder();
        for (int i = 0; i < dotCount; i++) {
            dotsBuilder.append(".");
        }
        loading.setText(String.valueOf(dotsBuilder));
    }
}