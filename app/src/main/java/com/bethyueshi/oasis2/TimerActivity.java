package com.bethyueshi.oasis2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TimerActivity extends AppCompatActivity {
    private ProgressBar barTimer;
    //private Button btnTimer;
    private TextView textTimer;
    private CountDownTimer countDownTimer;
    private double wait = 0.1; // minutes to wait
    public static final int SIXTY = 60; //TODO change it back to 60
    private int seconds = (int)(SIXTY * wait);
    private int testNum = 0;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        VideoFragment vf = (VideoFragment) getSupportFragmentManager()
                .findFragmentById(R.id.init_video_instr);
        vf.setGIF(getTestVideo(testNum), 100);
        vf.setClickable(false);

        barTimer = (ProgressBar)findViewById(R.id.barTimer);
        textTimer = (TextView)findViewById(R.id.textTimer);
        //btnTimer = (Button)findViewById(R.id.button_start);

        testNum = getIntent().getIntExtra("test_num", 0);
        timer = new Timer(barTimer, textTimer, testNum, 1, getApplicationContext());

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//        if (timer._shootMP != null)
//            timer._shootMP.start();
    }

    //TODO get this method out (reuse it)
    private int getTestVideo(int testNum){
        switch(testNum){
            case 0:
                return R.drawable.recordph;
            case 1:
                return R.drawable.recordmetal;
            case 2:
                return R.drawable.recordph;
            case 3:
                return R.drawable.recordph;
            default:
                return R.drawable.recordph;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (timer._shootMP != null){
//            timer._shootMP.stop();
//            timer._shootMP.release();
//        }
    }
}
