package com.apk.mod.io.Home.Extract;

import android.graphics.drawable.Drawable;

public class AppInfo {
    private String name;
    private String packageName;
    private Drawable icon;
    private long size;
    private String version; // App version

    public AppInfo(String name, String packageName, Drawable icon, long size, String version) {
        this.name = name;
        this.packageName = packageName;
        this.icon = icon;
        this.size = size;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public long getSize() {
        return size;
    }

    public String getVersion() {
        return version;
    }
}
