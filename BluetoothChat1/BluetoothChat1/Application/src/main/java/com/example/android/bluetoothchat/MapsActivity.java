package com.example.android.bluetoothchat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.common.activities.SampleActivityBase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.BreakIterator;


// OnMapReadyCallback： 地圖元件建立完成介面
public class MapsActivity extends SampleActivityBase
        implements OnMapReadyCallback {

    // Google Map 元件
    private GoogleMap map;
    // Google Map UI 設定元件
    private UiSettings uiSettings;

    private LinearLayout title_panel;
    private TableLayout menu_panel;
    private Switch zoom_button_switch, compass_switch,
            traffic_switch, mylocation_button_switch,
            mylocation_layer_switch, scroll_gestures_switch,
            zoom_gestures_switch, tilt_gestures_switch,
            rotate_gestures_switch;

    // 選單是否已顯示
    private boolean showMenu = false;

    // 請求授權使用的請求代碼
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private static final int RegisterData = 1;
    private static final int RegisterRequestCode = 10;
    private TextView textView_v0;
    private String text_v0, text_v1;
    private double text_v0_double, text_v1_double;
    private TextView textView_v1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        processViews();
        processControllers();
        textView_v0 = (TextView)findViewById(R.id.textView_v0);
        textView_v1 = (TextView)findViewById(R.id.textView_v1);
        textView_v0.setText("");
        textView_v1.setText("");
        Bundle bundle_map = this.getIntent().getExtras();
        String positionData1 = bundle_map.getString("1111");
        Log.d(TAG,"positionData1:"+positionData1);
        if(positionData1.startsWith("v0:")) {
            textView_v0.append(positionData1,3,16);
            textView_v1.append(positionData1,19,33);
            Log.d(TAG,"positionData0:"+textView_v0);
            Log.d(TAG,"positionData1:"+textView_v1);
        }
        text_v0 = textView_v0.getText().toString();
        text_v0_double = Double.parseDouble(text_v0);
        text_v1 = textView_v1.getText().toString();
        text_v1_double = Double.parseDouble(text_v1);
    }

    private void processViews() {
        // 讀取型態為 SupportMapFragment 的 Google Map 物件
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        // 註冊地圖元件建立完成監聽物件
        mapFragment.getMapAsync(this);

        title_panel = (LinearLayout) findViewById(R.id.title_panel);
        menu_panel = (TableLayout) findViewById(R.id.menu_panel);

        zoom_button_switch = (Switch) findViewById(R.id.zoom_button_switch);
        compass_switch = (Switch) findViewById(R.id.compass_switch);
        traffic_switch = (Switch) findViewById(R.id.traffic_switch);
        mylocation_button_switch = (Switch) findViewById(R.id.mylocation_button_switch);
        mylocation_layer_switch = (Switch) findViewById(R.id.mylocation_layer_switch);
        scroll_gestures_switch = (Switch) findViewById(R.id.scroll_gestures_switch);
        zoom_gestures_switch = (Switch) findViewById(R.id.zoom_gestures_switch);
        tilt_gestures_switch = (Switch) findViewById(R.id.tilt_gestures_switch);
        rotate_gestures_switch = (Switch) findViewById(R.id.rotate_gestures_switch);
    }

    private void processControllers() {
        title_panel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // 顯示或隱藏設定面版
                showMenu = !showMenu;
                menu_panel.setVisibility(showMenu ? View.VISIBLE
                        : View.INVISIBLE);
            }

        });

        // 開啟或關閉地圖的設定
        CompoundButton.OnCheckedChangeListener switchListener;
        // 使用 Switch 元件控制地圖的設定
        switchListener = new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (buttonView == zoom_button_switch) {
                    // 縮放控制按鈕
                    uiSettings.setZoomControlsEnabled(isChecked);
                } else if (buttonView == compass_switch) {
                    // 指北針圖示
                    uiSettings.setCompassEnabled(isChecked);
                } else if (buttonView == traffic_switch) {
                    // 交通資訊圖層
                    map.setTrafficEnabled(isChecked);
                } else if (buttonView == mylocation_button_switch) {
                    // 我的位置按鈕
                    uiSettings.setMyLocationButtonEnabled(isChecked);
                } else if (buttonView == mylocation_layer_switch) {
                    // 我的位置圖層
                    requestPermission();
                } else if (buttonView == scroll_gestures_switch) {
                    // 移動
                    uiSettings.setScrollGesturesEnabled(isChecked);
                } else if (buttonView == zoom_gestures_switch) {
                    // 縮放
                    uiSettings.setZoomGesturesEnabled(isChecked);
                } else if (buttonView == tilt_gestures_switch) {
                    // 傾斜
                    uiSettings.setTiltGesturesEnabled(isChecked);
                } else if (buttonView == rotate_gestures_switch) {
                    // 旋轉
                    uiSettings.setRotateGesturesEnabled(isChecked);
                }
            }

        };

        // 註冊所有 Switch 元件開關狀態改變事件
        zoom_button_switch.setOnCheckedChangeListener(switchListener);
        compass_switch.setOnCheckedChangeListener(switchListener);
        traffic_switch.setOnCheckedChangeListener(switchListener);
        mylocation_button_switch.setOnCheckedChangeListener(switchListener);
        mylocation_layer_switch.setOnCheckedChangeListener(switchListener);
        scroll_gestures_switch.setOnCheckedChangeListener(switchListener);
        zoom_gestures_switch.setOnCheckedChangeListener(switchListener);
        tilt_gestures_switch.setOnCheckedChangeListener(switchListener);
        rotate_gestures_switch.setOnCheckedChangeListener(switchListener);
    }

    // 地圖元件建立完成時，Android 自動呼叫這個方法
    // 參數是建立好的 Google Map 物件
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // 設定 Google Map 物件
        map = googleMap;

        // 移動地圖到台北車站
        LatLng taipeimainstation = new LatLng(text_v0_double, text_v1_double);
        map.addMarker(new MarkerOptions().position(taipeimainstation).title("Now Position"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(taipeimainstation, 17));

        // 取得地圖 UI 設定物件
        uiSettings = map.getUiSettings();

        // 讀取地圖目前的設定
        configMenuPanel();
    }

    // 讀取地圖目前的設定
    private void configMenuPanel() {
        zoom_button_switch.setChecked(uiSettings.isZoomControlsEnabled());
        compass_switch.setChecked(uiSettings.isCompassEnabled());
        traffic_switch.setChecked(map.isTrafficEnabled());
        scroll_gestures_switch.setChecked(uiSettings.isScrollGesturesEnabled());
        zoom_gestures_switch.setChecked(uiSettings.isZoomGesturesEnabled());
        tilt_gestures_switch.setChecked(uiSettings.isTiltGesturesEnabled());
        rotate_gestures_switch.setChecked(uiSettings.isRotateGesturesEnabled());
    }

    // 請求授權
    private void requestPermission() {
        // 如果裝置版本是6.0（包含）以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 取得讀取高精確度位置資訊授權狀態，參數是請求授權的名稱
            int hasPermission = checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION);

            // 如果未授權
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                // 請求讀取高精確度位置資訊授權
                //     第一個參數是請求授權的名稱
                //     第二個參數是請求代碼
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);
                return;
            }
        }

        // 如果裝置版本是6.0以下，
        // 或是裝置版本是6.0（包含）以上，使用已經授權
        //
        processMyLocation();
    }

    // 使用完成授權的選擇以後，Android會呼叫這個方法
    //     第一個參數：請求授權代碼
    //     第二個參數：請求的授權名稱
    //     第三個參數：使用選擇授權的結果
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        // 如果是讀取位置授權請求
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            // 如果在授權請求選擇「允許」
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //
                processMyLocation();
            }
            // 如果在授權請求選擇「拒絕」
            else {
                // 顯示沒有授權的訊息
                Toast.makeText(this, "沒有讀取位置授權",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions,
                    grantResults);
        }
    }

    // 啟動位置更新服務
    private void processMyLocation() throws SecurityException {
        map.setMyLocationEnabled(mylocation_layer_switch.isChecked());
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RegisterRequestCode) {
//            if (resultCode == RegisterData) {
////                String position = data.getStringExtra("registerdata");
////                android.util.Log.d(TAG, "requestCode = " + requestCode);
////                android.util.Log.d(TAG, "resultCode = " + resultCode);
////                android.util.Log.d(TAG, "position = " + position);
////                String positionData = data.getStringExtra("register_result");
////                if(positionData.startsWith("v0:")) {
////                        Log.d(TAG, "requestCode = " + requestCode);
////                        Log.d(TAG, "resultCode = " + resultCode);
////                        Log.d(TAG, "msg3 = " + msg3);
//                String position = data.getStringExtra("1111");
//                String positionData = data.getStringExtra("2222");
//                        Log.d(TAG, "requestCode = " + requestCode);
//                        Log.d(TAG, "resultCode = " + resultCode);
//                        Log.d(TAG, "msg3 = " + positionData);
//                    textView_v0.setText(positionData);
////                    textView_v0.append(positionData,2,18);  //btdata
//
//                text_v0 = textView_v0.getText().toString();
//                text_v0_double = Double.parseDouble(text_v0);
////                if(positionData.startsWith("v1:")) {
//
//                    textView_v1.setText(positionData);
////                    textView_v1.append(positionData, 2, 18);  //btdata
//
//                text_v1 = textView_v1.getText().toString();
//                text_v1_double = Double.parseDouble(text_v1);
//                LatLng taipeimainstation = new LatLng(text_v0_double, text_v1_double);
//                map.addMarker(new MarkerOptions().position(taipeimainstation).title("Now Position"));
//                map.moveCamera(CameraUpdateFactory.newLatLngZoom(taipeimainstation, 17));
//            }
//        }
//    }

}