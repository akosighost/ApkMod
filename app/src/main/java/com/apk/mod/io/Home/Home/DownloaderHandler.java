package com.apk.mod.io.Home.Home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.apk.mod.io.R;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;

import java.util.Objects;

public class DownloaderHandler {

    private AlertDialog downloadDialog;
    private int downloadId;

    public void showDownloader(Context context, LayoutInflater layoutInflater) {
        downloadDialog = new AlertDialog.Builder(context).create();
        LayoutInflater cvLI = layoutInflater;
        View cvCV = (View) cvLI.inflate(R.layout.downloader_layout, null);
        downloadDialog.setView(cvCV);
        Objects.requireNonNull(downloadDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final LinearLayout bg = (LinearLayout) cvCV.findViewById(R.id.linear1);
        final TextView value = (TextView) cvCV.findViewById(R.id.value);
        final TextView title = (TextView) cvCV.findViewById(R.id.title);
        final TextView percent = (TextView) cvCV.findViewById(R.id.percent);
        final TextView button1 = (TextView) cvCV.findViewById(R.id.b1);
        final TextView button2 = (TextView) cvCV.findViewById(R.id.b2);
        final ProgressBar progressBar = (ProgressBar) cvCV.findViewById(R.id.progress);
        final CheckBox checkBox = (CheckBox) cvCV.findViewById(R.id.checkbox);
        checkBox.setChecked(true);
        {
            GradientDrawable SketchUi = new GradientDrawable();
            int d = (int) getApplicationContext().getResources().getDisplayMetrics().density;
            SketchUi.setColor(getColor(R.color.dark));
            SketchUi.setCornerRadius(d*20);
            bg.setBackground(SketchUi);
        }
        try {
            if (Double.parseDouble(save.getString("save_switch", "")) == 0) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = data.get((int)position).get("link").toString().replace("blob", "raw").trim();
        String path = DataExtension.defaultApkDirectory();
        String name = data.get((int)position).get("name").toString().concat(" ".concat(data.get((int)position).get("version").toString().concat(".apk")));

            PRDownloader.initialize(context);
            PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                    .setDatabaseEnabled(true)
                    .build();
            PRDownloader.initialize(context, config);
            downloadId = PRDownloader.download(url, path, name)
                    .build()
                    .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                        @Override
                        public void onStartOrResume() {
                        }
                    })
                    .setOnPauseListener(new OnPauseListener() {
                        @Override
                        public void onPause() {
                        }
                    })
                    .setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel() {
                        }
                    })
                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(Progress progress) {
                            long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
                            value.setText(Utils.getProgressDisplayLine(progress.currentBytes, progress.totalBytes));
                            progressBar.setProgress((int)progressPercent);
                            percent.setText(String.valueOf((long)progressPercent).concat(" %"));
                        }
                    })
                    .start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            title.setText(getString(R.string.download_success));
                            if (checkBox.isChecked()) {
                                downloadDialog.dismiss();
                                install(DataExtension.defaultApkDirectory().concat(name));
                            } else {
                                downloadDialog.dismiss();
                                successDialog = new AlertDialog.Builder(HomeActivity.this).create();
                                LayoutInflater cvLI = getLayoutInflater();
                                View cvCV = (View) cvLI.inflate(R.layout.dialog_popup2, null);
                                successDialog.setView(cvCV);
                                Objects.requireNonNull(successDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                final LinearLayout bg = (LinearLayout)  cvCV.findViewById(R.id.linear1);
                                final TextView descriptions = (TextView) cvCV.findViewById(R.id.textview2);
                                final TextView open = (TextView) cvCV.findViewById(R.id.textview3);
                                {
                                    GradientDrawable SketchUi = new GradientDrawable();
                                    int d = (int) getApplicationContext().getResources().getDisplayMetrics().density;
                                    SketchUi.setColor(getColor(R.color.dark));
                                    SketchUi.setCornerRadius(d*20);
                                    bg.setBackground(SketchUi);
                                }
                                open.setText(R.string.install_dialog);
                                descriptions.setText(DataExtension.defaultApkDirectory().concat(name));
                                open.setOnClickListener(view -> {
                                    install(DataExtension.defaultApkDirectory().concat(name));
                                    successDialog.dismiss();
                                });
                                successDialog.setCancelable(true);
                                successDialog.show();
                            }
                        }
                        @Override
                        public void onError(Error error) {
                            downloadDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Unable to download!", Toast.LENGTH_SHORT).show();
                        }
                    });
            checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    check = 0;
                    save.edit().putString("save_switch", String.valueOf((long)(check))).apply();
                } else {
                    check = 1;
                    save.edit().putString("save_switch", String.valueOf((long)(check))).apply();
                }
            });
            button1.setOnClickListener(view1 -> {
                if (number == 1) {
                    number = 0;
                    PRDownloader.resume(downloadId);
                    button1.setText(R.string.pause_download_dialog);
                } else if (number == 0) {
                    number = 1;
                    PRDownloader.pause(downloadId);
                    button1.setText(R.string.resume_download_dialog);
                }
            });
            button2.setOnClickListener(view12 -> {
                PRDownloader.cancel(downloadId);
                downloadDialog.dismiss();
            });
        }
        downloadDialog.setCancelable(false);
        downloadDialog.show();
    }
}
