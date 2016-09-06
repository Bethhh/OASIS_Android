package com.bethyueshi.oasis2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.viewpagerindicator.IconPageIndicator;

public class MapActivity extends FragmentActivity {

    private ViewPager mViewPager;
    public static final int test0 = 0;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 2;
    private static final String TAG = "Map";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Checks if location permission is granted.
        checkLocationPermission();

        // Sets the pager with an adapter.
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(new MapPagerAdapter(getSupportFragmentManager()));

        // Binds the icon indicator to the adapter
        IconPageIndicator iconIndicator = (IconPageIndicator)findViewById(R.id.icons);
        iconIndicator.setViewPager(mViewPager);

        // Add button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, SelectTest.class);
                intent.putExtra("test_num", test0);
                startActivity(intent);
            }
        });
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "Request Map Permission");

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);// This is an app-defined int cont. The callback method gets the result of the request.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Location Permission is granted");
                } else {
                    Toast.makeText(MapActivity.this, "Location permission is required!", Toast.LENGTH_SHORT).show();
                    checkLocationPermission();
                }
                return;
            }
        }
    }
}


