package com.apk.mod.io.Home.Extension;

import android.content.Context;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.apk.mod.io.Home.Extract.AppInfo;
import com.apk.mod.io.Home.Extract.ExtractActivity;
import com.apk.mod.io.Home.Home.DownloaderHandler;
import com.apk.mod.io.Home.Offline.OfflineActivity;
import com.apk.mod.io.R;

import java.util.List;

public class PopupMenu {

    private static Intent intent = new Intent();

    public static void showDownloadMenu(Context context, LayoutInflater layoutInflater, View view, String link, String path, String filename, int number) {
        Context wrapper = new ContextThemeWrapper(context, R.style.popupMenuStyle);
        android.widget.PopupMenu popupMenu = new android.widget.PopupMenu(wrapper, view);
        popupMenu.inflate(R.menu.home_menu);

        popupMenu.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.menu) {
                    DownloaderHandler downloaderHandler = new DownloaderHandler();
                    downloaderHandler.showDownloader(context, layoutInflater, link, path, filename, number);
                    return true;
                } else if (itemId == R.id.favorites) {
                    Toast.makeText(context, R.string.not_available, Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    return false;
                }
            }
        });
        popupMenu.show();
    }
    public static void showPopupMenu(Context context, LayoutInflater layoutInflater, View view) {
        Context wrapper = new ContextThemeWrapper(context, R.style.popupMenuStyle);
        android.widget.PopupMenu popupMenu = new android.widget.PopupMenu(wrapper, view);
        popupMenu.inflate(R.menu.option_menu);

        popupMenu.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.s1) {
                    intent.setClass(context, ExtractActivity.class);
                    context.startActivity(intent);
                    return true;
                } else if (itemId == R.id.s2) {
                    intent.setClass(context, OfflineActivity.class);
                    context.startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }
        });
        popupMenu.show();
    }
    public static void showExtractMenu(Context context, LayoutInflater layoutInflater, View view, View childAt, int position, List<AppInfo> appList) {
        Context wrapper = new ContextThemeWrapper(context, R.style.popupMenuStyle);
        android.widget.PopupMenu popupMenu = new android.widget.PopupMenu(wrapper, view);
        popupMenu.inflate(R.menu.extract_menu);

        popupMenu.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                AppInfo selectedApp2 = appList.get(position);
                if (itemId == R.id.menu_item_1) {
                    SystemData.installApkWithPackage(context, String.valueOf(appList.get(position)));
                    return true;
                } else if (itemId == R.id.menu_item_2) {
                    SystemData.uninstallApp(context, selectedApp2.getPackageName());
                    return true;
                } else if (itemId == R.id.menu_item_3) {
                    SystemData.extractApp(context, selectedApp2.getPackageName(), selectedApp2.getName());
                    return true;
                } else if (itemId == R.id.menu_item_4) {
//                    SystemData.shareApkByPackageName(context, selectedApp2.getPackageName());
                    Toast.makeText(wrapper, R.string.not_available, Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    return false;
                }
            }
        });
        popupMenu.show();
    }
}
