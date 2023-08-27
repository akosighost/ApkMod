package com.apk.mod.io.Home.Home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.apk.mod.io.Home.Extension.FileExtension;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class  UpdateListener {
    private static int downloadId;
    private static double number = 0;
    private static AlertDialog updateDialog;
    public static void showUpdateDialog(@NonNull Context context, @NonNull LayoutInflater layoutInflater,@NonNull String response) {
        try {
            ArrayList<HashMap<String, Object>> dataupdate = new Gson().fromJson(response, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
            try {
                android.content.pm.PackageManager pm = context.getPackageManager(); android.content.pm.PackageInfo pinfo = pm.getPackageInfo(context.getPackageName().toString(), 0); String your_version = pinfo.versionName;
                if (!your_version.equals(dataupdate.get((int)0).get("version").toString())) {
                    {
                        updateDialog = new AlertDialog.Builder(context).create();
                        LayoutInflater cvLI = layoutInflater;
                        View cvCV = (View) cvLI.inflate(R.layout.dialog_popup1, null);
                        updateDialog.setView(cvCV);
                        Objects.requireNonNull(updateDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        final LinearLayout bg = cvCV.findViewById(R.id.linear1);
                        final LinearLayout download_holder = cvCV.findViewById(R.id.download_holder);
                        final LinearLayout button_holder = cvCV.findViewById(R.id.button_holder);
                        final TextView title = (TextView) cvCV.findViewById(R.id.textview1);
                        final TextView descriptions = (TextView) cvCV.findViewById(R.id.textview2);
                        final TextView size = (TextView) cvCV.findViewById(R.id.size);
                        final TextView button1 = (TextView) cvCV.findViewById(R.id.button1);
                        final TextView button2 = (TextView) cvCV.findViewById(R.id.button2);
                        final TextView value = (TextView) cvCV.findViewById(R.id.value);
                        final TextView percent = (TextView) cvCV.findViewById(R.id.percent);
                        final ImageView image = (ImageView) cvCV.findViewById(R.id.image);
                        final ProgressBar progressBar = (ProgressBar) cvCV.findViewById(R.id.progress);
                        download_holder.setVisibility(View.GONE);
                        button2.setText(R.string.update_dialog);
                        if (dataupdate.get((int) 0).containsKey("title")) {
                            title.setVisibility(View.VISIBLE);
                            title.setText(dataupdate.get((int) 0).get("title").toString().trim());
                        } else {
                            title.setVisibility(View.GONE);
                        }
                        if (dataupdate.get((int) 0).containsKey("description")) {
                            descriptions.setVisibility(View.VISIBLE);
                            descriptions.setText(dataupdate.get((int) 0).get("description").toString().trim());
                        } else {
                            descriptions.setVisibility(View.GONE);
                        }
                        button1.setOnClickListener(view -> {
                            updateDialog.dismiss();
                        });
                        button2.setOnClickListener(view -> {
                            SystemUI.Transition(bg, 300);
                            button_holder.setVisibility(View.GONE);
                            download_holder.setVisibility(View.VISIBLE);
                            if (dataupdate.get((int) 0).containsKey("link")) {
                                if (!Objects.equals(dataupdate.get((int) 0).get("link"), "")) {
                                    {

                                        PRDownloader.initialize(context);
                                        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                                                .setDatabaseEnabled(true)
                                                .build();
                                        final String url = dataupdate.get((int) 0).get("link").toString().trim();
                                        final String path = FileExtension.defaultApkDirectory();
                                        final String filename = new File(url).getName();
                                        final String newName = dataupdate.get((int) 0).get("name").toString().concat(" ".concat(dataupdate.get((int) 0).get("version").toString().concat(".apk")));
                                        PRDownloader.initialize(context, config);
                                        downloadId = PRDownloader.download(url, path, filename)
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
                                                        progressBar.setProgress((int)progressPercent);
                                                        percent.setText(String.valueOf((long)progressPercent).concat(" %"));
                                                    }
                                                })
                                                .start(new OnDownloadListener() {
                                                    @Override
                                                    public void onDownloadComplete() {
                                                        FileExtension.renameFile(FileExtension.defaultApkDirectory(), filename, newName);
                                                        SystemUI.Transition(bg, 300);
                                                        button_holder.setVisibility(View.VISIBLE);
                                                        download_holder.setVisibility(View.GONE);
                                                        size.setVisibility(View.GONE);
                                                        SystemData.install(context, path.concat(newName));
                                                        button1.setOnClickListener(view12 -> {
                                                            SystemData.install(context, FileExtension.defaultApkDirectory().concat(newName));
                                                        });
                                                    }
                                                    @Override
                                                    public void onError(Error error) {
                                                        Toast.makeText(context, R.string.unable_to_download, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        image.setOnClickListener(view1 -> {
                                            if (number == 1) {
                                                number = 0;
                                                PRDownloader.resume(downloadId);
                                                button1.setText(R.string.pause_download_dialog);
                                                image.setImageResource(R.drawable.pause);
                                            } else if (number == 0) {
                                                number = 1;
                                                PRDownloader.pause(downloadId);
                                                button1.setText(R.string.resume_download_dialog);
                                                image.setImageResource(R.drawable.resume);
                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(context, R.string.unable_to_download, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, R.string.unable_to_download, Toast.LENGTH_SHORT).show();
                            }
                        });
                        // Always false
                        updateDialog.setCancelable(false);
                        updateDialog.show();
                    }
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e) { e.printStackTrace(); }
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
