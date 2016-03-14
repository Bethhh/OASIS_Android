package com.bethyueshi.oasis2;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TimerActivity extends AppCompatActivity {
    private ProgressBar barTimer;
    private TextView textTimer;
    private CountDownTimer countDownTimer;
    private int wait = 6; // minutes to wait

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        barTimer = (ProgressBar)findViewById(R.id.barTimer);
        textTimer = (TextView)findViewById(R.id.textTimer);

        Animation an = new RotateAnimation(0.0f, -90.0f, 450.0f, 450.0f);
        an.setFillAfter(true);
        barTimer.startAnimation(an);
        barTimer.setMax(60 * wait);
        startTimer(wait);
    }

    public void setWaitTime(int m){
        this.wait = m;
    }

    private void startTimer(final int m) {
        countDownTimer = new CountDownTimer(60 * m * 1000, 100) {
            // 500 means, onTick function will be called at every 500 milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;
                barTimer.setProgress((int)seconds);
                textTimer.setText(String.format("%02d", seconds / 60) + ":" +
                                  String.format("%02d", seconds % 60));
                // format the textview to show the easily readable format
            }

            @Override
            public void onFinish() {
                if(textTimer.getText().equals("00:00")){
                    textTimer.setText("STOP");
                }
                else{
                    textTimer.setText(wait + ":00");
                    barTimer.setProgress(60 * m);
                }
            }
        }.start();
    }
}
