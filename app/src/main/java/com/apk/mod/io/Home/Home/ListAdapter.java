package com.apk.mod.io.Home.Home;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apk.mod.io.Home.Extension.SystemUI;
import com.apk.mod.io.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ListAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, Object>> data;
    private Context context;

    public ListAdapter(Context context, ArrayList<HashMap<String, Object>> arr) {
        this.context = context;
        this.data = arr;
    }

    @Override
    public int getCount() {
        return data.size();
    }
    @Override
    public HashMap<String, Object> getItem(int _index) {
        return data.get(_index);
    }
    @Override
    public long getItemId(int _index) {
        return _index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.home_list, null);
        }
        final LinearLayout linear1 = view.findViewById(R.id.linear1);
        final LinearLayout linear2 = view.findViewById(R.id.linear2);
        final TextView textview1 = view.findViewById(R.id.textview1);
        final TextView textview2 = view.findViewById(R.id.textview2);
        final TextView textview3 = view.findViewById(R.id.textview3);
        final TextView textview4 = view.findViewById(R.id.textview4);
        final TextView type1 = view.findViewById(R.id.type1);
        final TextView type2 = view.findViewById(R.id.type2);
        final TextView type3 = view.findViewById(R.id.type3);
        final TextView type4 = view.findViewById(R.id.type4);
        final TextView end_of_list = view.findViewById(R.id.end_of_list);
        final LinearLayout type_holder1 = view.findViewById(R.id.type_holder1);
        final LinearLayout type_holder2 = view.findViewById(R.id.type_holder2);
        final LinearLayout type_holder3 = view.findViewById(R.id.type_holder3);
        final LinearLayout type_holder4 = view.findViewById(R.id.type_holder4);
        final ImageView img1 = view.findViewById(R.id.img1);
        final ImageView img2 = view.findViewById(R.id.img2);
        final ImageView img3 = view.findViewById(R.id.img3);
        final ImageView img4 = view.findViewById(R.id.img4);
        final ImageView install = view.findViewById(R.id.install);
        final ImageView delete = view.findViewById(R.id.delete);
        final ImageView apkicon = view.findViewById(R.id.image);

        setTextViewVisibility(type_holder1, type1, "type1", position);
        setTextViewVisibility(type_holder2, type2, "type2", position);
        setTextViewVisibility(type_holder3, type3, "type3", position);
        setTextViewVisibility(type_holder4, type4, "type4", position);
        setImageViewVisibility(img1, "img1", position);
        setImageViewVisibility(img2, "img2", position);
        setImageViewVisibility(img3, "img3", position);
        setImageViewVisibility(img4, "img4", position);

        install.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        apkicon.setVisibility(View.GONE);
        textview3.setVisibility(View.GONE);
        textview4.setVisibility(View.GONE);
        if (position == getCount() - 1) {
            end_of_list.setVisibility(View.VISIBLE);
        } else {
            end_of_list.setVisibility(View.GONE);
        }
        SystemUI.gradientDrawable(linear1, 0, 0, 0, "#FF202226", "#000000", false);
        SystemUI.gradientDrawable(linear2, 0, 2, 20, "#FF202226", "#FF2A2B2F", false);
        SystemUI.setCornerRadius(context, type_holder1, ColorStateList.valueOf(Color.TRANSPARENT),300,ColorStateList.valueOf(0xFF9E9E9E));
        SystemUI.setCornerRadius(context, type_holder2, ColorStateList.valueOf(Color.TRANSPARENT),300,ColorStateList.valueOf(0xFF2196F3));
        SystemUI.setCornerRadius(context, type_holder3, ColorStateList.valueOf(Color.TRANSPARENT),300,ColorStateList.valueOf(0xFFF44336));
        SystemUI.setCornerRadius(context, type_holder4, ColorStateList.valueOf(Color.TRANSPARENT),300,ColorStateList.valueOf(0xFF228B22));

        textview1.setText(String.valueOf((long) (position + 1)));
        if (data.get(position).containsKey("name") && !Objects.equals(data.get(position).get("name"), "")) {
            linear1.setVisibility(View.VISIBLE);
            textview2.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textview2.setText(data.get(position).get("name").toString().trim());
            textview2.setSelected(true);
            textview2.setSingleLine(true);
        } else {
            linear1.setVisibility(View.GONE);
        }
        if (data.get((int) position).containsKey("version") && !Objects.equals(data.get(position).get("version"), "")) {
            String version = Objects.requireNonNull(data.get(position).get("version")).toString();
            textview3.setVisibility(!version.isEmpty() ? View.VISIBLE : View.GONE);
            textview3.setText(!version.isEmpty() ? context.getString(R.string.version).concat(" : ").concat(version) : "");
        } else {
            textview3.setVisibility(View.GONE);
        }
        if (data.get((int)position).containsKey("link") && Objects.equals(data.get(position).get("link"), "")) {
            textview4.setText(R.string.unable_to_download);
        } else {
            textview4.setVisibility(View.GONE);
        }
        return view;
    }
    private void setTextViewVisibility(LinearLayout layout, TextView textView, String key, int position) {
        if (data.get(position).containsKey(key)) {
            String value = data.get(position).get(key).toString();
            if (!value.isEmpty()) {
                layout.setVisibility(View.VISIBLE);
                textView.setText(value.trim());
            } else {
                layout.setVisibility(View.GONE);
            }
        } else {
            layout.setVisibility(View.GONE);
        }
    }

    private void setImageViewVisibility(ImageView imageView, String key, int position) {
        if (data.get(position).containsKey(key)) {
            String imageUrl = data.get(position).get(key).toString().replace("blob", "raw");
            if (!imageUrl.isEmpty()) {
                imageView.setVisibility(View.VISIBLE);
                Glide.with(context).load(Uri.parse(imageUrl)).into(imageView);
            } else {
                imageView.setVisibility(View.GONE);
            }
        } else {
            imageView.setVisibility(View.GONE);
        }
    }
}


