package com.apk.mod.io.Home.Permission;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.apk.mod.io.Home.Extension.FileExtension;
import com.apk.mod.io.Home.Extension.SystemUI;
import com.apk.mod.io.Home.Home.HomeActivity;
import com.apk.mod.io.R;

public class PermissionActivity extends AppCompatActivity {
    private final Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        SystemUI.customizeSystemUI(this);
        LinearLayout button1 = findViewById(R.id.button1);
        LinearLayout button2 = findViewById(R.id.button2);
        TextView permission_text = findViewById(R.id.permission_text);
        String appName = getString(R.string.app_name);
        String permissionMessage = getString(R.string.permission_message);
        String combinedText = String.format("%s %s", appName, permissionMessage);
        permission_text.setText(combinedText);
        SystemUI.setCornerRadius(this, button2, ColorStateList.valueOf(Color.TRANSPARENT), 0, ColorStateList.valueOf(0xFF2A2B2F), 3, 0, false);
        button1.setOnClickListener(view -> {
            String[] permissions = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};
            if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(permissions[1]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(permissions, 1000);
            } else {
                make();
                intent.setClass(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(view -> finishAffinity());
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000 && grantResults.length == 2
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            make();
            intent.setClass(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }
    }
    private void make() {
        if (!FileExtension.isExistFile(FileExtension.defaultApkDirectory())) {
            FileExtension.makeDirectory(FileExtension.defaultApkDirectory());
        }
    }
}