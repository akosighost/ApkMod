package com.apk.mod.io.Home.Home;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.apk.mod.io.R;

public class PopupMenu {

    public static void showPopupMenu(Context context, LayoutInflater layoutInflater, View view, String link, String path, String filename, int number) {
        Context wrapper = new ContextThemeWrapper(context, R.style.popupMenuStyle);
        android.widget.PopupMenu popupMenu = new android.widget.PopupMenu(wrapper, view);
        popupMenu.inflate(R.menu.home_menu);

        popupMenu.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.offline) {
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
}
