package com.apk.mod.io.Home.Home;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.apk.mod.io.Home.Extension.SystemUI;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchHandler {
    private Context context;
    private EditText search;
    private LinearLayout close_holder;
    private View search_holder;
    private ListView listView;
    private String string_search;
    private ArrayList<HashMap<String, Object>> datakey;
    private int length;
    private int number;
    private String value;
    private LayoutInflater layoutInflater;

    public SearchHandler(Context context, LayoutInflater layoutInflater, EditText search, LinearLayout close_holder, View search_holder, ListView listView, String string_search) {
        this.context = context;
        this.search = search;
        this.close_holder = close_holder;
        this.search_holder = search_holder;
        this.listView = listView;
        this.string_search = string_search;
        this.layoutInflater = layoutInflater;
        setupSearchListener();
    }

    private void setupSearchListener() {
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
                final String _charSeq = _param1.toString();
                double num = 200.0;
                if (search.getText().toString().isEmpty()) {
                    SystemUI.Transition(search_holder, num);
                    SystemUI.Transition(close_holder, num);
                    close_holder.setVisibility(View.GONE);
                } else {
                    SystemUI.Transition(search_holder, num);
                    SystemUI.Transition(close_holder, num);
                    close_holder.setVisibility(View.VISIBLE);
                    search.setCursorVisible(true);
                }

                try {
                    datakey = new Gson().fromJson(string_search, new TypeToken<ArrayList<HashMap<String, Object>>>() {
                    }.getType());
                    length = datakey.size();
                    number = length - 1;
                    for (int _repeat17 = 0; _repeat17 < (int) (length); _repeat17++) {
                        value = datakey.get((int) number).get("name").toString();
                        if (!(_charSeq.length() > value.length()) && value.toLowerCase().contains(_charSeq.toLowerCase())) {
                        } else {
                            datakey.remove((int) (number));
                            listView.setAdapter(new ListAdapter(context, layoutInflater, datakey));
                            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                        }
                        number--;
                    }
                    listView.setAdapter(new ListAdapter(context, layoutInflater, datakey));
                    ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                } catch (Exception ignored) {
                }
            }

            @Override
            public void afterTextChanged(Editable _param1) {
            }

            @Override
            public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
            }
        });
    }
}
