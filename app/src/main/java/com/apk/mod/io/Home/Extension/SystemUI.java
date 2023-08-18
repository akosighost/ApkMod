package com.apk.mod.io.Home.Extension;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.apk.mod.io.R;

public class SystemUI {

    public static void customizeSystemUI(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int darkColor = ContextCompat.getColor(activity, R.color.dark);
        window.setStatusBarColor(darkColor);
        window.setNavigationBarColor(darkColor);
    }
    public static void setCornerRadius(Context context, View view, ColorStateList color, int radi, ColorStateList stroke_color, int stroke, int elevation, boolean setClickable) {
        if (setClickable) {
            GradientDrawable SketchUi = new GradientDrawable();
            int d = (int) context.getResources().getDisplayMetrics().density;
            SketchUi.setColor(color);
            SketchUi.setCornerRadius(d*radi);
            SketchUi.setStroke(d*stroke,stroke_color);
            view.setElevation(d*elevation);
            android.graphics.drawable.RippleDrawable SketchUiRD = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{0xFFE0E0E0}), SketchUi, null);
            view.setBackground(SketchUiRD);
            view.setClickable(true);
        } else {
            GradientDrawable SketchUi = new GradientDrawable();
            int d = (int) context.getResources().getDisplayMetrics().density;
            SketchUi.setColor(color);
            SketchUi.setCornerRadius(d*radi);
            SketchUi.setStroke(d*stroke,stroke_color);
            view.setElevation(d*elevation);
            view.setBackground(SketchUi);
            view.setClickable(true);
        }
    }
    public static void Transition(final View view, final double duration) {
        LinearLayout ViewGroup = (LinearLayout) view;
        android.transition.AutoTransition autoTransition = new android.transition.AutoTransition();
        autoTransition.setDuration((long)duration);
        android.transition.TransitionManager.beginDelayedTransition(ViewGroup, autoTransition);
    }
}
