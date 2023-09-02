package com.apk.mod.io.Home.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.apk.mod.io.Home.Extension.PRDownloaderUtils;
import com.apk.mod.io.Home.Extension.SystemData;
import com.apk.mod.io.Home.Extension.SystemUI;
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
    private SharedPreferences save;
    private int downloadId;
    private double number;
    private double check;

    public void showDownloader(Context context, LayoutInflater layoutInflater, String link, String path, String filename,final int num) {
        downloadDialog = new AlertDialog.Builder(context).create();
        LayoutInflater cvLI = layoutInflater;
        View cvCV = (View) cvLI.inflate(R.layout.downloader_layout, null);
        downloadDialog.setView(cvCV);
        Objects.requireNonNull(downloadDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final LinearLayout bg = (LinearLayout) cvCV.findViewById(R.id.linear1);
        final LinearLayout download_holder = (LinearLayout) cvCV.findViewById(R.id.download_holder);
        final TextView title = (TextView) cvCV.findViewById(R.id.title);
        final TextView value = (TextView) cvCV.findViewById(R.id.value);
        final TextView percent = (TextView) cvCV.findViewById(R.id.percent);
        final TextView button1 = (TextView) cvCV.findViewById(R.id.b1);
        final TextView button2 = (TextView) cvCV.findViewById(R.id.b2);
        final ProgressBar progressBar = (ProgressBar) cvCV.findViewById(R.id.progress);
        final CheckBox checkBox = (CheckBox) cvCV.findViewById(R.id.checkbox);
        if (num == 1) {
            title.setVisibility(View.VISIBLE);
            value.setVisibility(View.VISIBLE);
            download_holder.setVisibility(View.VISIBLE);
        } else if (num == 2) {
            title.setVisibility(View.GONE);
            value.setVisibility(View.GONE);
            download_holder.setVisibility(View.GONE);
        }
        SystemUI.setCornerRadius(context, bg, ColorStateList.valueOf(0xFF202226), 20, ColorStateList.valueOf(Color.TRANSPARENT), 0, 0, false);

        PRDownloader.initialize(context);
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder().setDatabaseEnabled(true).build();
        PRDownloader.initialize(context, config);
        downloadId = PRDownloader.download(link, path, filename)
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
                        progressBar.setIndeterminate(false);
                        long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
                        value.setText(PRDownloaderUtils.getProgressDisplayLine(progress.currentBytes, progress.totalBytes));
                        progressBar.setProgress((int) progressPercent);
                        percent.setText(String.valueOf((long) progressPercent).concat(" %"));
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        if (num == 1) {
                            title.setText(R.string.download_success);
                            downloadDialog.dismiss();
                            SystemData.installApk(context, path.concat(filename));
                        } else if (num == 2) {
                            downloadDialog.dismiss();
                        }
                    }
                    @Override
                    public void onError(Error error) {
                        downloadDialog.dismiss();
                        Toast.makeText(context, "Unable to download!", Toast.LENGTH_SHORT).show();
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
        downloadDialog.setCancelable(false);
        downloadDialog.show();
    }
}
