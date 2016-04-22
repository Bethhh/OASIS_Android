package com.bethyueshi.oasis2;

import android.app.Activity;
import android.content.Context;
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
public class Timer {

    private ProgressBar barTimer;
    private Button btnTimer;
    private TextView textTimer;
    private CountDownTimer countDownTimer;
    private double wait = 0.1; // minutes to wait
    public static final int SIXTY = 60; //TODO change it back to 60
    private int seconds = (int)(SIXTY * wait);
    private int testNum = 0;
    private int step = 0;
    private final Context ctx;
    //private Activity next;

    public Timer(ProgressBar pb, TextView text, Button btn,
                 int num, int step, Context ctx){

        this.barTimer = pb;
        this.textTimer = text;
        this.btnTimer = btn;
        this.ctx = ctx;
        this.step = step;
        //this.next = n;

        // Add steps TODO
        this.testNum = num;
        setTimer(testNum, step);

        this.barTimer.setMax(seconds);
        this.barTimer.setProgress(seconds);
        this.barTimer.setSecondaryProgress(seconds);
        this.textTimer.setText(String.format("%02d", (int) (Math.floor(wait))) + ":" +
                String.format("%02d", seconds % SIXTY));

        if(btnTimer != null) {
            this.btnTimer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startTimer(wait);
                    btnTimer.setEnabled(false);
                    btnTimer.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    public void setWaitTime(double m){
        this.wait = m;
    }

    public void setTimer(int testNum, int step){//add steps TODO
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
//                    Intent intent = new Intent(TimerActivity.this, CameraActivity.class);
//                    intent.putExtra("test_num", testNum);
//                    startActivity(intent);
                    if(btnTimer != null) {
                        Intent intent;
                        if (step == 0) {
                            intent = new Intent(ctx, TimerActivity.class);
                        } else { // 1
                            intent = new Intent(ctx, CameraActivity.class);
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("test_num", testNum);
                        ctx.startActivity(intent);
                    }else{
                        //do something
                        barTimer.setMax(seconds);
                        barTimer.setProgress(seconds);
                        barTimer.setSecondaryProgress(seconds);
                        textTimer.setText(String.format("%02d", (int) (Math.floor(wait))) + ":" +
                                String.format("%02d", seconds % SIXTY));
                    }
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
