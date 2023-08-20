package com.apk.mod.io.Home.Extension;

import android.os.Environment;

import java.io.File;

public class FileExtension {
    public static String getExternalStorageDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
    public static String defaultApkDirectory() {
        return getExternalStorageDir().concat("/ModApk/files/Apk/");
    }
    public static String Offline() {
        return getExternalStorageDir().concat("/ModApk/files/.Offline/");
    }
    public static boolean isExistFile(String path) {
        File file = new File(path);
        return file.exists();
    }
    public static void makeDirectory(String path) {
        if (!isExistFile(path)) {
            File file = new File(path);
            file.mkdirs();
        }
    }
    public static void deleteFile(String path) {
        File file = new File(path);
        if (!file.exists()) return;
        if (file.isFile()) {
            file.delete();
            return;
        }
        File[] fileArr = file.listFiles();
        if (fileArr != null) {
            for (File subFile : fileArr) {
                if (subFile.isDirectory()) {
                    deleteFile(subFile.getAbsolutePath());
                }

                if (subFile.isFile()) {
                    subFile.delete();
                }
            }
        }
        file.delete();
    }
    public static void renameFile(final String path, final String oldFileName, final String newFileName) {
        //Create file objects for the old and new file names
        File oldFile = new File(path, oldFileName);
        File newFile = new File(path, newFileName);
        if (oldFile.exists()) {
            if (oldFile.renameTo(newFile)) {
                //Toast.makeText(activity, "File renamed successfully!", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(activity, "Failed to rename the file.", Toast.LENGTH_SHORT).show();
            }
        } else {
            //Toast.makeText(activity, "The old file does not exist.", Toast.LENGTH_SHORT).show();
        }
    }
}
