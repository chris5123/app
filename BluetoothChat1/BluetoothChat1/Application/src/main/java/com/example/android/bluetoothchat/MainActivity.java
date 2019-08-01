/*
* Copyright 2013 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


package com.example.android.bluetoothchat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.MenuItemHoverListener;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.example.android.bluetoothchat.R;
import com.example.android.common.activities.SampleActivityBase;
import com.example.android.common.logger.Log;
import com.example.android.common.logger.LogFragment;
import com.example.android.common.logger.LogView;
import com.example.android.common.logger.LogWrapper;
import com.example.android.common.logger.MessageOnlyLogFilter;
import com.google.android.gms.maps.GoogleMap;

import java.util.concurrent.Delayed;
import java.util.logging.Logger;


/**
 * A simple launcher activity containing a summary sample description, sample log and a custom
 * {@link android.support.v4.app.Fragment} which can display a view.
 * <p>
 * For devices with displays with a width of 720dp or greater, the sample log is always visible,
 * on other devices it's visibility is controlled by an item on the Action Bar.
 */
public class MainActivity extends SampleActivityBase {

    public static final String TAG = "main";
    private static final int LOCATION_CODE = 100;
    private static final int RegisterRequestCode = 10;
    private Intent intent;
    private static final int RegisterData = 1;

    // Whether the Log Fragment is currently shown
    private boolean mLogShown;
    private TextView text_Result;
    private MessageOnlyLogFilter msgFilter;
    private String msg;
    private LocationManager lm;
    private TextView textView_main;
    private LogFragment mLogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BluetoothChatFragment fragment = new BluetoothChatFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
        textView_main = (TextView) findViewById(R.id.textView2);
        textView_main.setText("");
        gps();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.ble_gatt_mode:
                Intent intent_ble = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent_ble);
                finish();
                break;
            case R.id.google_maps_mode:
//                Intent intent_map = new Intent(MainActivity.this, MapsActivity.class);
//                intent_map.putExtra("registerdata","Register Activity return.");
//                String buffer = msgFilter.toString();
//                intent_map.putExtra("register_result",buffer);
//                setResult(RegisterData,intent_map);
//                startActivity(intent_map);
                textView_main.append(BluetoothChatFragment.readMessage);
                Intent intent_map = new Intent(this, MapsActivity.class);
                Bundle bundle_main = new Bundle();

                String buffer = textView_main.getText().toString();
                bundle_main.putString("1111",buffer);
                intent_map.putExtras(bundle_main);
                if(buffer.startsWith("v0")) {
                    startActivity(intent_map);
                    textView_main.setText("0");
                }
                else
                {
                    Toast.makeText(this, "Message Error Please Try Again",Toast.LENGTH_SHORT).show();
                    textView_main.setText("");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /** Create a chain of targets that will receive log data */
    @Override
    public void initializeLogging() {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
        msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);


        Log.i(TAG, "Ready");
    }


    public void gps(){
        lm = (LocationManager) MainActivity.this.getSystemService(MainActivity.this.LOCATION_SERVICE);
        boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (ok) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.e("BRG","NO PERMISSION");
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);

            }
        } else {
            Log.e("BRG","NO GPS");
            Toast.makeText(MainActivity.this, "NO GPS", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 100);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(MainActivity.this, "NO GPS PERMISSION ",Toast.LENGTH_LONG).show();
                }

            }
        }
    }
}

