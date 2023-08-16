package com.apk.mod.io.Home.Extension;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class SystemData {
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
    public static void install(Context context, String PATH) {
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
}
