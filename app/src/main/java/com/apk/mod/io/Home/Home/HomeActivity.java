package com.apk.mod.io.Home.Home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apk.mod.io.Home.Extension.FileExtension;
import com.apk.mod.io.Home.Extension.SystemData;
import com.apk.mod.io.Home.Extension.SystemUI;
import com.apk.mod.io.Home.Network.RequestNetwork;
import com.apk.mod.io.Home.Network.RequestNetworkController;
import com.apk.mod.io.Home.Permission.PermissionActivity;
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
    private LinearLayout spinner_holder;
    private HorizontalScrollView scroll1;
    private Spinner spinner;
    private RequestNetwork internet;
    private RequestNetwork.RequestListener _internet_request_listener;
    private RequestNetwork update;
    private RequestNetwork.RequestListener _update_request_listener;
    private SharedPreferences save;
    private Intent intent = new Intent();
    private ArrayList<HashMap<String, Object>> spin = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> databaseList = new ArrayList<>();
    private String string_search;
    private static final int BACK_PRESS_DELAY = 2000; // 2 seconds
    private long backPressTime;
    private double language = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SystemUI.customizeSystemUI(this);
        intent = new Intent(this, BackgroundService.class);
        startService(intent);
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            intent.setClass(getApplicationContext(), PermissionActivity.class);
            startActivity(intent);
        } else {
            make();
            initialize(savedInstanceState);
            initialize();
        }
    }
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - backPressTime < BACK_PRESS_DELAY) {
            finishAffinity(); // Exit the app or perform your desired action here
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            backPressTime = System.currentTimeMillis();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        setDataUpdate(getString(R.string.update_data));
    }
    @Override
    protected void onStart() {
        super.onStart();
        swipeRefreshLayout.setRefreshing(true);
        setDataUpdate(getString(R.string.update_data));
        setData(getString(R.string.data1));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                // Installation was successful
                Toast.makeText(this, "Installation success!", Toast.LENGTH_SHORT).show();
            } else {
                // Installation was denied or failed
                Toast.makeText(this, "Installation denied or failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initialize(Bundle savedInstanceState) {
        internet = new RequestNetwork(this);
        update = new RequestNetwork(this);
        swipeRefreshLayout = findViewById(R.id.swipe);
        search = findViewById(R.id.search);
        search_holder = findViewById(R.id.search_holder);
        close_holder = findViewById(R.id.close_holder);
        close = findViewById(R.id.close);
        listView = findViewById(R.id.listview);
        linear1 = findViewById(R.id.linear1);
        scroll1 = findViewById(R.id.scroll1);
        spinner = findViewById(R.id.spinner);
        spinner_holder = findViewById(R.id.spinner_holder);
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
                    SearchHandler searchHandler = new SearchHandler(getApplicationContext(), getLayoutInflater(), search, close_holder, search_holder, listView, string_search);
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
    }
    private void initialize() {
        SystemUI.setCornerRadius(this, spinner_holder, ColorStateList.valueOf(0xFF202226), 5, ColorStateList.valueOf(0xFF2A2B2F), 2, 0, false);
        SystemUI.setCornerRadius(this, search, ColorStateList.valueOf(0xFF202226), 0, ColorStateList.valueOf(0xFF2A2B2F), 2, 0, false);
        listView.setHorizontalScrollBarEnabled(false);
        listView.setVerticalScrollBarEnabled(false);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("language", "Categories");
            spin.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("language", "Android IDE");
            spin.add(_item);
        }
        spinner.setAdapter(new Spinner1Adapter(spin));
    }
    public class Spinner1Adapter extends BaseAdapter {

        ArrayList<HashMap<String, Object>> _data;

        public Spinner1Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }

        @Override
        public View getView(final int _position, View _v, ViewGroup _container) {
            LayoutInflater _inflater = getLayoutInflater();
            View _view = _v;
            if (_view == null) {
                _view = _inflater.inflate(R.layout.language, null);
            }

            final TextView textview1 = _view.findViewById(R.id.textview1);

            textview1.setText(_data.get((int)_position).get("language").toString());
//            type = _data.get((int)_position).get("language").toString();
            return _view;
        }
    }
    private void setDataUpdate(String data) {
        update.startRequestNetwork(RequestNetworkController.GET, data, "", _update_request_listener);
    }
    private void setData(String data) {
        internet.startRequestNetwork(RequestNetworkController.GET, data, "", _internet_request_listener);
    }
    private void make() {
        String[] directoriesToCheck = {
                FileExtension.defaultApkDirectory(),
                FileExtension.Offline()
        };
        for (String directoryPath : directoriesToCheck) {
            if (!FileExtension.isExistFile(directoryPath)) {
                FileExtension.makeDirectory(directoryPath);
            }
        }
    }
}