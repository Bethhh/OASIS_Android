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
    private int currTest = R.drawable.t1;
    private Button btnDone;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_water);

        testNum = getIntent().getIntExtra("test_num", 0);

        VideoFragment vf = (VideoFragment) getSupportFragmentManager()
                .findFragmentById(R.id.init_video_instr);
        vf.setGIF(getTestVideo(testNum), 100);


        barTimer = (ProgressBar)findViewById(R.id.barTimer);
        textTimer = (TextView)findViewById(R.id.textTimer);
        btnDone = (Button)findViewById(R.id.button_done);

        this.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestWater.this, TimerActivity.class);
                intent.putExtra("test_num", testNum);
                startActivity(intent);
            }
        });


        timer = new Timer(barTimer, textTimer, null, testNum, 0, getApplicationContext());

        if (timer._shootMP != null)
            timer._shootMP.start();
    }

    private int getTestVideo(int testNum){
        switch(testNum){
            case 0:
                return R.drawable.t1;
            case 1:
                return R.drawable.t2;
            case 2:
                return R.drawable.t1;
            case 3:
                return R.drawable.t2;
            default:
                return R.drawable.t1;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer._shootMP != null)
            timer._shootMP.stop();
    }

}
