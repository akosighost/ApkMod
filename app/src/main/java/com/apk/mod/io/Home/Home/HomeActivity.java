package com.apk.mod.io.Home.Home;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.apk.mod.io.Home.Extension.SystemData;
import com.apk.mod.io.Home.Extension.SystemUI;
import com.apk.mod.io.Home.Network.RequestNetwork;
import com.apk.mod.io.Home.Network.RequestNetworkController;
import com.apk.mod.io.Home.Service.BackgroundService;
import com.apk.mod.io.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private EditText search;
    private LinearLayout close_holder;
    private LinearLayout search_holder;
    private ImageView close;
    private LinearLayout linear1;
    private HorizontalScrollView scroll1;
    private LinearLayout slot1;
    private LinearLayout slot2;
    private LinearLayout slot3;
    private TextView textview_slot1;
    private TextView textview_slot2;
    private TextView textview_slot3;
    private AlertDialog successDialog;
    private AlertDialog downloadDialog;
    private AlertDialog updateDialog;
    private RequestNetwork internet;
    private RequestNetwork.RequestListener _internet_request_listener;
    private RequestNetwork update;
    private RequestNetwork.RequestListener _update_request_listener;
    private SharedPreferences save;
    private Intent intent = new Intent();
    private ArrayList<HashMap<String, Object>> databaseList = new ArrayList<>();
    private int downloadId;
    private double number = 0;
    private double check = 0;
    private double length = 0;
    private String string_search;
    private String value = "";
    private String data = "";
    private static final int BACK_PRESS_DELAY = 2000; // 2 seconds
    private long backPressTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SystemUI.customizeSystemUI(this);
        intent = new Intent(this, BackgroundService.class);
        startService(intent);
        initialize(savedInstanceState);
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDataUpdate(getString(R.string.update_data));
    }

    @Override
    protected void onStart() {
        super.onStart();
        setDataUpdate(getString(R.string.update_data));
        setData(getString(R.string.data1));
    }
    private void initialize(Bundle savedInstanceState) {
        internet = new RequestNetwork(this);
        update = new RequestNetwork(this);
        save = getSharedPreferences("save", Activity.MODE_PRIVATE);
        swipeRefreshLayout = findViewById(R.id.swipe);
        search = findViewById(R.id.search);
        search_holder = findViewById(R.id.search_holder);
        close_holder = findViewById(R.id.close_holder);
        close = findViewById(R.id.close);
        listView = findViewById(R.id.listview);
        linear1 = findViewById(R.id.linear1);
        scroll1 = findViewById(R.id.scroll1);
        // Slot views
        slot1 = findViewById(R.id.slot1);
        slot2 = findViewById(R.id.slot2);
        slot3 = findViewById(R.id.slot3);
        // Text views for slots
        textview_slot1 = findViewById(R.id.textview_slot1);
        textview_slot2 = findViewById(R.id.textview_slot2);
        textview_slot3 = findViewById(R.id.textview_slot3);
        close.setOnClickListener(view -> {
            search.setText("");
            SystemData.hideKeyboard(this);
        });

        _internet_request_listener = new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> _param3) {
                try {
                    swipeRefreshLayout.setRefreshing(false);
                    databaseList = new Gson().fromJson(response, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
                    ListAdapter adapter = new ListAdapter(getApplicationContext(), getLayoutInflater(), databaseList);
                    listView.setAdapter(adapter);
                    ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
                    string_search = new Gson().toJson(databaseList);
                    SearchHandler searchHandler = new SearchHandler(HomeActivity.this, getLayoutInflater(), search, close_holder, search_holder, listView, string_search);
                    SystemData.sortListMap(databaseList, "name", false, true);
                }
                catch (Exception e) {
                    Toast.makeText(HomeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
            @Override
            public void onErrorResponse(String tag, String message) {
                Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        };
        _update_request_listener = new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> headers) {
                UpdateListener.showUpdateDialog(HomeActivity.this, getLayoutInflater(),response);
            }
            @Override
            public void onErrorResponse(String tag, String error) {
                Toast.makeText(HomeActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        };
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setData(getString(R.string.data1));
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View convertView, int position, long id) {
                DownloaderHandler downloaderHandler = new DownloaderHandler();
                downloaderHandler.showDownloader(HomeActivity.this, getLayoutInflater());

            }
        });
    }
    private void initialize() {
        SystemUI.setCornerRadius(this, search, ColorStateList.valueOf(0xFF202226),5, ColorStateList.valueOf(0xFF2A2B2F));
        listView.setHorizontalScrollBarEnabled(false);
        listView.setVerticalScrollBarEnabled(false);
        listView.setDivider(null);
        listView.setDividerHeight(0);
    }
    private void setDataUpdate(String data) {
        update.startRequestNetwork(RequestNetworkController.GET, data, "", _update_request_listener);
    }
    private void setData(String data) {
        internet.startRequestNetwork(RequestNetworkController.GET, data, "", _internet_request_listener);
    }
}