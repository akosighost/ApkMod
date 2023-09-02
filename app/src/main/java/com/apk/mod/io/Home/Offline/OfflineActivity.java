package com.apk.mod.io.Home.Offline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.apk.mod.io.Home.Extension.FileExtension;
import com.apk.mod.io.Home.Extension.SystemData;
import com.apk.mod.io.Home.Extension.SystemUI;
import com.apk.mod.io.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OfflineActivity extends AppCompatActivity {

    private ListView listView;
    private ImageView back;
    private SwipeRefreshLayout swipe;
    private List<ApkFileData> fileData = new ArrayList<>();
    private static final int BACK_PRESS_DELAY = 2000; // 2 seconds
    private long backPressTime;

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
        setContentView(R.layout.activity_offline);
        SystemUI.customizeSystemUI(this);
        listView = findViewById(R.id.listview);
        back = findViewById(R.id.back);
        swipe = findViewById(R.id.swipe);
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
        listView.setHorizontalScrollBarEnabled(false);
        listView.setVerticalScrollBarEnabled(false);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        String directoryPath = FileExtension.Offline();

        List<File> apkFiles = getFiles(directoryPath);
        ArrayAdapter<File> adapter = new ArrayAdapter<File>(this, R.layout.home_list, R.id.textview2, apkFiles) {
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
                final TextView filesize = view.findViewById(R.id.textview3);
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
                type_holder1.setVisibility(View.GONE);
                type_holder2.setVisibility(View.GONE);
                type_holder3.setVisibility(View.GONE);
                type_holder4.setVisibility(View.GONE);
                install.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
                image.setVisibility(View.GONE);
                type2.setText(R.string.install);
                type3.setText(R.string.share);
                type4.setText(R.string.delete);
                type2.setTextColor(0xFF228B22);
                type3.setTextColor(0xFF2196F3);
                type4.setTextColor(0xFFF44336);
                SystemUI.setCornerRadius(getApplicationContext(), linear1, ColorStateList.valueOf(0xFF202226), 0, ColorStateList.valueOf(0xFF000000), 0, 0, false);
                SystemUI.setCornerRadius(getApplicationContext(), linear2, ColorStateList.valueOf(0xFF202226), 0, ColorStateList.valueOf(0xFF2A2B2F), 2, 10, false);
                if (position == getCount() - 1) {
                    end_of_list.setVisibility(View.VISIBLE);
                } else {
                    end_of_list.setVisibility(View.GONE);
                }
                File apkFile = getItem(position);
                if (apkFile != null) {
                    fileName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    fileName.setText(apkFile.getName());
                    fileName.setSelected(true);
                    fileName.setSingleLine(true);
                    long fileSize = apkFile.length();
                    String fileSizeFormatted = SystemData.formatFileSize(fileSize);
                    filesize.setText(fileSizeFormatted);
                }
                assert apkFile != null;
                Drawable apkIcon = SystemData.getApkIcon(getPackageManager(), apkFile);
                if (apkIcon != null) {
                    image.setImageDrawable(apkIcon);
                }
                number.setText(String.valueOf((long) (position + 1)));
                linear2.setOnClickListener(v -> SystemData.installApk(getApplicationContext(), String.valueOf(apkFile)));
                option.setOnClickListener(v -> {
                    Context wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.popupMenuStyle);
                    android.widget.PopupMenu popupMenu = new android.widget.PopupMenu(wrapper, v);
                    popupMenu.inflate(R.menu.offline_menu);

                    popupMenu.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int itemId = item.getItemId();

                            if (itemId == R.id.install) {
                                SystemData.installApk(getApplicationContext(), String.valueOf(apkFile));
                                return true;
                            } else if (itemId == R.id.delete) {
                                if (apkFile != null) {
                                    apkFiles.remove(apkFile);
                                    apkFile.delete();
                                    Parcelable listState = listView.onSaveInstanceState();
                                    sortApkFilesByTime();
                                    ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
                                    listView.onRestoreInstanceState(listState);
                                }
                                return true;
                            } else if (itemId == R.id.share) {
                                SystemData.shareApkFile(getApplicationContext(), apkFile);
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    popupMenu.show();
                });
                type2.setOnClickListener(view1 -> {
                    if (String.valueOf(apkFile).contains(".apk")) {
                        SystemData.installApk(getApplicationContext(), String.valueOf(apkFile));
                    }
//                    if (String.valueOf(apkFile).contains(".apk")) {
//                        SystemData.install(getApplicationContext(), String.valueOf(apkFile));
//                    } else if (String.valueOf(apkFile).contains(".txt")) {
//                        openTxtFile(apkFile);
//                    }
                });
                type4.setOnClickListener(v -> {
                    if (apkFile != null) {
                        apkFiles.remove(apkFile);
                        apkFile.delete();
                        Parcelable listState = listView.onSaveInstanceState();
                        sortApkFilesByTime();
                        ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
                        listView.onRestoreInstanceState(listState);
                    }
                });
                return view;
            }
        };
        Parcelable listState = listView.onSaveInstanceState();
        listView.setAdapter(adapter);
        sortApkFilesByTime();
        ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
        listView.onRestoreInstanceState(listState);
    }

    @NonNull
    private static List<File> getFiles(String directoryPath) {
        List<File> apkFiles = new ArrayList<>();
        File directory = new File(directoryPath);

        // Make sure the directory exists and is a directory
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".apk") || file.isFile() && file.getName().toLowerCase().endsWith(".txt")) {
                        apkFiles.add(file);
                    }
                }
            }
        }
        return apkFiles;
    }
    private void sortApkFilesByTime() {
        Collections.sort(fileData, new Comparator<ApkFileData>() {
            @Override
            public int compare(ApkFileData apk1, ApkFileData apk2) {
                return Long.compare(apk1.getTimestamp(), apk2.getTimestamp());
            }
        });
    }
    private static class ApkFileData {
        private File file;
        private long timestamp;

        public ApkFileData(File file, long timestamp) {
            this.file = file;
            this.timestamp = timestamp;
        }

        public File getFile() {
            return file;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}