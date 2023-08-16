package com.apk.mod.io.Home.Extension;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
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
    public static void gradientDrawable(final View _view, final double _radius, final double _stroke, final double _shadow, final String _color, final String _borderColor, final boolean _ripple) {
        android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
        gd.setColor(Color.parseColor(_color));
        gd.setCornerRadius((int)_radius);
        gd.setStroke((int)_stroke,Color.parseColor(_borderColor));
        _view.setElevation((int)_shadow);
        android.content.res.ColorStateList clrb = new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{Color.parseColor("#9E9E9E")});
        android.graphics.drawable.RippleDrawable ripdrb = new android.graphics.drawable.RippleDrawable(clrb , gd, null);
        _view.setClickable(true);
        _view.setBackground(ripdrb);
    }
    public static void setCornerRadius(Context context, View view, ColorStateList color,int num, ColorStateList stroke) {
        GradientDrawable SketchUi = new GradientDrawable();
        int d = (int) context.getResources().getDisplayMetrics().density;
        SketchUi.setColor(color);
        SketchUi.setCornerRadius(d * num);
        SketchUi.setStroke(d, stroke);
        view.setBackground(SketchUi);
    }
    public static void Transition(final View view, final double duration) {
        LinearLayout ViewGroup = (LinearLayout) view;
        android.transition.AutoTransition autoTransition = new android.transition.AutoTransition();
        autoTransition.setDuration((long)duration);
        android.transition.TransitionManager.beginDelayedTransition(ViewGroup, autoTransition);
    }
}
