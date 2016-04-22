package com.bethyueshi.oasis2;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by bethyueshi on 4/21/16.
 */
public class TestWater extends AppCompatActivity {
    private ProgressBar barTimer;
    private TextView textTimer;
    private CountDownTimer countDownTimer;
    private double wait = 0.1; // minutes to wait
    public static final int SIXTY = 60; //TODO change it back to 60
    private int seconds = (int)(SIXTY * wait);
    private int testNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        barTimer = (ProgressBar)findViewById(R.id.barTimer);
        textTimer = (TextView)findViewById(R.id.textTimer);

        testNum = getIntent().getIntExtra("test_num", 0);
        Timer timer = new Timer(barTimer, textTimer, null, testNum, 0, getApplicationContext());
    }

}
