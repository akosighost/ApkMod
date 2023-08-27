package com.apk.mod.io.Home.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.apk.mod.io.Home.Offline.OfflineActivity;
import com.apk.mod.io.Home.Permission.PermissionActivity;
import com.apk.mod.io.Home.Service.BackgroundService;
import com.apk.mod.io.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout close_holder;
    private LinearLayout search_holder;
    private LinearLayout spinner_holder;
    private LinearLayout offline_holder;
    private ListView listView;
    private EditText search;
    private ImageView close;
    private ImageView offline;
    private HorizontalScrollView scroll1;
    private Spinner spinner;
    private RequestNetwork internet;
    private RequestNetwork dropdown;
    private RequestNetwork.RequestListener _internet_request_listener;
    private RequestNetwork.RequestListener _dropdown_request_listener;
    private RequestNetwork update;
    private RequestNetwork.RequestListener _update_request_listener;
    private ArrayList<HashMap<String, Object>> spin = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> databaseList = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> databasedrop = new ArrayList<>();
    private String string_search;
    private static final int BACK_PRESS_DELAY = 2000; // 2 seconds
    private long backPressTime;
    private ProgressDialog progressDialog;
    private Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SystemUI.customizeSystemUI(this);
        Intent background = new Intent(this, BackgroundService.class);
        startService(background);
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            intent.setClass(getApplicationContext(), PermissionActivity.class);
            startActivity(intent);
        } else {
            make();
            initialize(savedInstanceState);
            initialize();
            showProgressDialog();
            setDataUpdate(getString(R.string.update_data));
            setData(getString(R.string.Apps));
            dropdown.startRequestNetwork(RequestNetworkController.GET, getString(R.string.dropdown_down), "", _dropdown_request_listener);
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
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            intent.setClass(getApplicationContext(), PermissionActivity.class);
            startActivity(intent);
        } else {
            setDataUpdate(getString(R.string.update_data));
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            intent.setClass(getApplicationContext(), PermissionActivity.class);
            startActivity(intent);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initialize(Bundle savedInstanceState) {
        internet = new RequestNetwork(this);
        dropdown = new RequestNetwork(this);
        update = new RequestNetwork(this);
        search = findViewById(R.id.search);
        search_holder = findViewById(R.id.search_holder);
        close_holder = findViewById(R.id.close_holder);
        offline_holder = findViewById(R.id.offline_holder);
        close = findViewById(R.id.close);
        listView = findViewById(R.id.listview);
        scroll1 = findViewById(R.id.scroll1);
        spinner = findViewById(R.id.spinner);
        spinner_holder = findViewById(R.id.spinner_holder);
        offline = findViewById(R.id.offline);
        close.setOnClickListener(view -> {
            search.setText("");
            SystemData.hideKeyboard(this);
        });

        _internet_request_listener = new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> _param3) {
                hideProgressDialog();
                try {
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
        _dropdown_request_listener = new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {

                databasedrop = new Gson().fromJson(response, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
                spinner.setAdapter(new Spinner1Adapter(databasedrop));
                ((BaseAdapter)spinner.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onErrorResponse(String tag, String message) {

            }
        };
        offline_holder.setOnClickListener(v -> {
            intent.setClass(getApplicationContext(), OfflineActivity.class);
            startActivity(intent);
        });
    }
    private void initialize() {
        SystemUI.setCornerRadius(this, offline_holder, ColorStateList.valueOf(0xFF2A2B2F), 300, ColorStateList.valueOf(Color.TRANSPARENT), 0, 0, false);
        SystemUI.setCornerRadius(this, spinner_holder, ColorStateList.valueOf(0xFF202226), 5, ColorStateList.valueOf(0xFF2A2B2F), 2, 0, false);
        SystemUI.setCornerRadius(this, search, ColorStateList.valueOf(0xFF202226), 0, ColorStateList.valueOf(0xFF2A2B2F), 2, 0, false);
        listView.setHorizontalScrollBarEnabled(false);
        listView.setVerticalScrollBarEnabled(false);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        OverScrollDecoratorHelper.setUpOverScroll(listView);
        scroll1.setHorizontalScrollBarEnabled(false);
        scroll1.setVerticalScrollBarEnabled(false);
    }
    public class Spinner1Adapter extends BaseAdapter {

        ArrayList<HashMap<String, Object>> data;

        public Spinner1Adapter(ArrayList<HashMap<String, Object>> _arr) {
            data = _arr;
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
        public View getView(final int position, View _v, ViewGroup _container) {
            LayoutInflater _inflater = getLayoutInflater();
            View _view = _v;
            if (_view == null) {
                _view = _inflater.inflate(R.layout.dropdown, null);
            }
            final TextView textview1 = _view.findViewById(R.id.textview1);

            textview1.setText(data.get((int)position).get("name").toString());

            if (Objects.equals(data.get((int) position).get("name"), "")) {
                textview1.setVisibility(View.GONE);
            }

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String selectedItem = parentView.getItemAtPosition(position).toString();

                    if (!Objects.equals(data.get((int) position).get("link"), "")) {
                        showProgressDialog();
                        setData(Objects.requireNonNull(data.get((int) position).get("link")).toString());
                    } else {
                        Toast.makeText(HomeActivity.this, getString(R.string.not_available), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // Do nothing here
                }
            });
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
    private void showProgressDialog() {
        new SimulatedTask();
        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Use circular style
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    private class SimulatedTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // Simulate a time-consuming task
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            hideProgressDialog();
        }
    }
}