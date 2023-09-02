package com.apk.mod.io.Home.Extract;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apk.mod.io.Home.Extension.SystemData;
import com.apk.mod.io.Home.Extension.SystemUI;
import com.apk.mod.io.Home.Extension.PopupMenu;
import com.apk.mod.io.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExtractActivity extends AppCompatActivity {

    private ListView listView;
    private ImageView back;
    private ImageView menu;
    private SwipeRefreshLayout swipe;
    private TextView title;
    private Spinner spinner;
    private EditText search;
    private static final int BACK_PRESS_DELAY = 2000; // 2 seconds
    private long backPressTime;
    private AppListAdapter adapter;
    private List<AppInfo> appList;

    @Override
    public void onBackPressed() {
        if (SystemData.isConnected(getApplicationContext())) {
            finish();
        } else if (!SystemData.isConnected(getApplicationContext())) {
            if (System.currentTimeMillis() - backPressTime < BACK_PRESS_DELAY) {
                finishAffinity();
            } else {
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
                backPressTime = System.currentTimeMillis();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SystemUI.customizeSystemUI(this);
        listView = findViewById(R.id.listview);
        back = findViewById(R.id.back);
        swipe = findViewById(R.id.swipe);
        title = findViewById(R.id.title);
        spinner = findViewById(R.id.spinner);
        search = findViewById(R.id.search);
        menu = findViewById(R.id.menu);

        appList = new ArrayList<>();
        spinner.setVisibility(View.GONE);
        search.setVisibility(View.GONE);
        menu.setVisibility(View.GONE);
        swipe.setEnabled(false);
        title.setText(R.string.extract_apk);
        SystemUI.setCornerRadius(this, search, ColorStateList.valueOf(0xFF202226), 0, ColorStateList.valueOf(0xFF2A2B2F), 2, 0, false);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Parcelable listState = listView.onSaveInstanceState();
                    ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
                    listView.onRestoreInstanceState(listState);
                    swipe.setRefreshing(false);
                } catch (Exception e) {
                    swipe.setRefreshing(false);
                }
            }
        });
        back.setOnClickListener(view -> {
            if (SystemData.isConnected(getApplicationContext())) {
                finish();
            } else if (SystemData.isConnected(getApplicationContext())) {
                finishAffinity();
            }
        });

        PackageManager packageManager = getPackageManager();
        List<PackageInfo> packages = packageManager.getInstalledPackages(0);

        for (PackageInfo packageInfo : packages) {
            // Include only non-system apps
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                String packageName = packageInfo.packageName;
                Drawable appIcon = packageInfo.applicationInfo.loadIcon(packageManager);
                long appSize = calculateAppSize(packageInfo.applicationInfo.sourceDir);
                String appVersion = packageInfo.versionName; // Get app version

                appList.add(new AppInfo(appName, packageName, appIcon, appSize, appVersion));
            }
        }
        adapter = new AppListAdapter(appList);
        listView.setAdapter(adapter);
        listView.setHorizontalScrollBarEnabled(false);
        listView.setVerticalScrollBarEnabled(false);
        listView.setDivider(null);
        listView.setDividerHeight(0);
    }

    private class AppListAdapter extends ArrayAdapter<AppInfo> {

        public AppListAdapter(List<AppInfo> appList) {
            super(ExtractActivity.this, R.layout.home_list, appList);
        }
        @NonNull
        @Override
        public View getView(int position, View view, @NonNull ViewGroup parent) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.home_list, parent, false);
            }
            final LinearLayout linear1 = view.findViewById(R.id.linear1);
            final LinearLayout linear2 = view.findViewById(R.id.linear2);
            final TextView number = view.findViewById(R.id.number);
            final TextView fileName = view.findViewById(R.id.textview2);
            final TextView packageName = view.findViewById(R.id.textview3);
            final TextView textview4 = view.findViewById(R.id.textview4);
            final TextView type1 = view.findViewById(R.id.type1);
            final TextView type2 = view.findViewById(R.id.type2);
            final TextView type3 = view.findViewById(R.id.type3);
            final TextView type4 = view.findViewById(R.id.type4);
            final TextView end_of_list = view.findViewById(R.id.end_of_list);
            final LinearLayout type_holder1 = view.findViewById(R.id.type_holder1);
            final LinearLayout type_holder2 = view.findViewById(R.id.type_holder2);
            final LinearLayout type_holder3 = view.findViewById(R.id.type_holder3);
            final LinearLayout type_holder4 = view.findViewById(R.id.type_holder4);
            final ImageView img1 = view.findViewById(R.id.img1);
            final ImageView img2 = view.findViewById(R.id.img2);
            final ImageView img3 = view.findViewById(R.id.img3);
            final ImageView img4 = view.findViewById(R.id.img4);
            final ImageView install = view.findViewById(R.id.install);
            final ImageView delete = view.findViewById(R.id.delete);
            final ImageView image = view.findViewById(R.id.image);
            final ImageView option = view.findViewById(R.id.option);
            textview4.setVisibility(View.GONE);
            type_holder1.setVisibility(View.VISIBLE);
            type_holder2.setVisibility(View.VISIBLE);
            type_holder3.setVisibility(View.GONE);
            type_holder4.setVisibility(View.GONE);
            install.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            type2.setTextColor(getColor(R.color.gray));
            SystemUI.setCornerRadius(getContext(), linear1, ColorStateList.valueOf(0xFF202226), 0, ColorStateList.valueOf(0xFF000000), 0, 0, false);
            SystemUI.setCornerRadius(getContext(), linear2, ColorStateList.valueOf(0xFF202226), 0, ColorStateList.valueOf(0xFF2A2B2F), 2, 10, false);
            SystemUI.setCornerRadius(getContext(), type_holder1, ColorStateList.valueOf(Color.TRANSPARENT), 300, ColorStateList.valueOf(0xFF9E9E9E), 1, 0, false);
            SystemUI.setCornerRadius(getContext(), type_holder2, ColorStateList.valueOf(Color.TRANSPARENT), 300, ColorStateList.valueOf(0xFF9E9E9E), 1, 0, false);

            AppInfo appInfo = appList.get(position);
            number.setText(String.valueOf((long) (position + 1)));
            if (position == getCount() - 1) {
                end_of_list.setVisibility(View.VISIBLE);
            } else {
                end_of_list.setVisibility(View.GONE);
            }

            // Set the app icon, app name, package name, and app size
            image.setImageDrawable(appInfo.getIcon());
            fileName.setText(appInfo.getName());
            packageName.setText(appInfo.getPackageName());
            type1.setText(SystemData.formatFileSize(appInfo.getSize()));
            type2.setText(String.format("Version: %s", appInfo.getVersion()));

            option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu.showExtractMenu(getContext(), getLayoutInflater(), v, listView.getChildAt(position), position, appList);
                }
            });
            return view;
        }
    }
    private long calculateAppSize(String sourceDir) {
        File file = new File(sourceDir);
        return file.length();
    }
}