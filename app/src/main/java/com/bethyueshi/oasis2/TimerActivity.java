package com.bethyueshi.oasis2;

import android.content.Intent;
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
    private Button btnTimer;
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
        btnTimer = (Button)findViewById(R.id.button_start);

        testNum = getIntent().getIntExtra("test_num", 0);
        setTimer(testNum);

        barTimer.setMax(seconds);
        barTimer.setProgress(seconds);
        barTimer.setSecondaryProgress(seconds);
        textTimer.setText(String.format("%02d", (int)(Math.floor(wait))) + ":" +
                String.format("%02d", seconds % SIXTY));

        btnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer(wait);
                btnTimer.setEnabled(false);
                btnTimer.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void setWaitTime(double m){
        this.wait = m;
    }

    public void setTimer(int testNum){
        switch(testNum){
            case 0:
                wait = 0.1;
                break;
            case 1:
                wait = 0.3;
                break;
            case 2:
                wait = 0.2;
                break;
            case 3:
                wait = 0.5;
                break;
            default:
                wait = 1;
        }

        seconds = (int)(SIXTY * wait);
    }

    private void startTimer(final double m) {
        countDownTimer = new CountDownTimer((int)(SIXTY * m * 1000), 100) {
            // 500 means, onTick function will be called at every 500 milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long secs = leftTimeInMilliseconds / 1000;
                barTimer.setSecondaryProgress((int)secs);
                textTimer.setText(String.format("%02d", secs / SIXTY) + ":" +
                                  String.format("%02d", secs % SIXTY));
                // format the textview to show the easily readable format
            }

            @Override
            public void onFinish() {
                if(textTimer.getText().equals("00:00")){
                    Intent intent = new Intent(TimerActivity.this, CameraActivity.class);
                    intent.putExtra("test_num", testNum);
                    startActivity(intent);
                }
                else{
                    textTimer.setText(String.format("%02d", (int)(Math.floor(m))) + ":" +
                                      String.format("%02d", seconds % SIXTY));
                    barTimer.setSecondaryProgress((int)(SIXTY * m));
                }
            }
        }.start();
    }
}
