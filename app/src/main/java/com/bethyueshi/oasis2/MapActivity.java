package com.bethyueshi.oasis2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;


import com.viewpagerindicator.IconPageIndicator;

public class MapActivity extends FragmentActivity {


    private MapPagerAdapter mMapPagerAdaper;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //Set the pager with an adapter
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(new MapPagerAdapter(getSupportFragmentManager()));

        //Bind the icon indicator to the adapter
        IconPageIndicator iconIndicator = (IconPageIndicator)findViewById(R.id.icons);
        iconIndicator.setViewPager(mViewPager);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

    }

}

