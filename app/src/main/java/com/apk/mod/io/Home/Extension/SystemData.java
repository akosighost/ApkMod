package com.apk.mod.io.Home.Extension;

import static android.app.Activity.RESULT_OK;
import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class SystemData {

    private static Intent intent = new Intent();

    public static boolean isConnected(Context _context) {
        ConnectivityManager _connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo _activeNetworkInfo = _connectivityManager.getActiveNetworkInfo();
        return _activeNetworkInfo != null && _activeNetworkInfo.isConnected();
    }
    public static void sortListMap(final ArrayList<HashMap<String, Object>> listMap, final String key, final boolean isNumber, final boolean ascending) {
        Collections.sort(listMap, new Comparator<HashMap<String, Object>>() {
            public int compare(HashMap<String, Object> _compareMap1, HashMap<String, Object> _compareMap2) {
                if (isNumber) {
                    int _count1 = Integer.parseInt(_compareMap1.get(key).toString());
                    int _count2 = Integer.parseInt(_compareMap2.get(key).toString());
                    if (ascending) {
                        return _count1 < _count2 ? -1 : 0;
                    } else {
                        return _count1 > _count2 ? -1 : 0;
                    }
                } else {
                    if (ascending) {
                        return (_compareMap1.get(key).toString()).compareTo(_compareMap2.get(key).toString());
                    } else {
                        return (_compareMap2.get(key).toString()).compareTo(_compareMap1.get(key).toString());
                    }
                }
            }
        });
    }
    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public static void installApk(Context context, String PATH) {
        java.io.File file = new java.io.File(PATH);
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uriFromFile(context, new java.io.File(PATH)), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Log.e("TAG", "Error in opening the file!");
            }
        }
    }
    static Uri uriFromFile(Context context, java.io.File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return androidx.core.content.FileProvider.getUriForFile(context,context.getApplicationContext().getPackageName() + ".provider", file);
        } else {
            return Uri.fromFile(file);
        }
    }
    public static void installApkWithPackage(Context context, String packageName) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Handle the case where the Play Store is not installed on the device
            Toast.makeText(context, "Play Store app not found", Toast.LENGTH_SHORT).show();
        }
    }
    public static void shareApkFile(Context context, File apkFile) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/vnd.android.package-archive");
        Uri apkUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", apkFile);
        intent.putExtra(Intent.EXTRA_STREAM, apkUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            Intent chooser = Intent.createChooser(intent, "Share APK using");
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(chooser);
        } catch (ActivityNotFoundException e) {
            // Handle if no suitable app to handle sharing
            Toast.makeText(context, "No app found to share the APK.", Toast.LENGTH_SHORT).show();
        }
    }
    public static void shareApkByPackageName(Context context, String packageName) {
        String uri = (context.getPackageName());
        try {
            android.content.pm.PackageInfo pi = context.getPackageManager().getPackageInfo(uri, android.content.pm.PackageManager.GET_ACTIVITIES);
            packageName = pi.applicationInfo.publicSourceDir;
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new java.io.File(packageName)));

            context.startActivity(Intent.createChooser(intent, packageName));
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    public static String formatFileSize(long fileSize) {
        String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double size = fileSize;
        while (size > 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        return new DecimalFormat("#,##0.#").format(size) + " " + units[unitIndex];
    }
    public static void openTxtFile(Context context, File txtFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", txtFile);
        intent.setDataAndType(uri, "text/plain");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Handle if there's no suitable app to open the file
            Toast.makeText(context, "No app found to open the file.", Toast.LENGTH_SHORT).show();
        }
    }
    public static Drawable getApkIcon(PackageManager packageManager, File apkFile) {
        PackageManager pm = packageManager;
        PackageInfo packageInfo = pm.getPackageArchiveInfo(apkFile.getAbsolutePath(), 0);
        if (packageInfo != null) {
            ApplicationInfo appInfo = packageInfo.applicationInfo;
            appInfo.sourceDir = apkFile.getAbsolutePath();
            appInfo.publicSourceDir = apkFile.getAbsolutePath();
            return appInfo.loadIcon(pm);
        }
        return null;
    }
    public static void uninstallApp(Context context, String packageName) {
        Uri packageUri = Uri.parse("package:" + packageName);
        Intent uninstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageUri);
        context.startActivity(uninstallIntent);
    }
    public static void extractApp(Context context, String packageName, String appName) {
        try {
            // Get the source directory of the app
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
            String sourceDir = appInfo.sourceDir;

            // Specify the destination directory to save the extracted APK
            String destinationDir = FileExtension.extractDirectory();

            // Create the destination directory if it doesn't exist
            File destDir = new File(destinationDir);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }

            // Extract the APK by copying it to the destination directory
            File sourceFile = new File(sourceDir);
            File destFile = new File(destDir, appName + ".apk");

            // Copy the APK file
            copyFile(sourceFile, destFile);
            Toast.makeText(context, "App extracted to " + destFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(context, "App not found", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, "Error extracting app", Toast.LENGTH_SHORT).show();
        }
    }
    // Helper method to copy a file
    private static void copyFile(File sourceFile, File destFile) throws IOException {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try (InputStream in = Files.newInputStream(sourceFile.toPath());
                 OutputStream out = Files.newOutputStream(destFile.toPath())) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }
        }
    }
}
