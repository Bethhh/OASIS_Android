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
    private int wait = 1; // minutes to wait
    public static final int SIXTY = 10; //TODO change it back to 60

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        barTimer = (ProgressBar)findViewById(R.id.barTimer);
        textTimer = (TextView)findViewById(R.id.textTimer);
        btnTimer = (Button)findViewById(R.id.button_start);

        barTimer.setMax(SIXTY * wait);
        barTimer.setProgress(SIXTY * wait);
        barTimer.setSecondaryProgress(SIXTY * wait);
        textTimer.setText(wait + ":00");

        btnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer(wait);
                btnTimer.setEnabled(false);
                btnTimer.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void setWaitTime(int m){
        this.wait = m;
    }

    private void startTimer(final int m) {
        countDownTimer = new CountDownTimer(SIXTY * m * 1000, 100) {
            // 500 means, onTick function will be called at every 500 milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;
                barTimer.setSecondaryProgress((int)seconds);
                textTimer.setText(String.format("%02d", seconds / SIXTY) + ":" +
                                  String.format("%02d", seconds % SIXTY));
                // format the textview to show the easily readable format
            }

            @Override
            public void onFinish() {
                if(textTimer.getText().equals("00:00")){
                    Intent intent = new Intent(TimerActivity.this, CameraActivity.class);
                    intent.putExtra("test_num", getIntent().getIntExtra("test_num", 0));
                    startActivity(intent);
                }
                else{
                    textTimer.setText(wait + ":00");
                    barTimer.setSecondaryProgress(SIXTY * m);
                }
            }
        }.start();
    }
}
