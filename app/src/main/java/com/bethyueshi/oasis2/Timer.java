package com.bethyueshi.oasis2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by bethyueshi on 4/21/16.
 */
public class Timer {

    private ProgressBar barTimer;
    //private Button btnTimer;
    private TextView textTimer;

    private CountDownTimer countDownTimer;
    private double wait = 0.1; // minutes to wait
    public static final int SIXTY = 60; //TODO change it back to 60
    private int seconds = (int)(SIXTY * wait);

    private int testNum = 0;
    private int step = 0;
    private final Context ctx;
    public MediaPlayer _shootMP = null;
    public MediaPlayer _shootMP2 = null;

    public Timer(ProgressBar pb, TextView text, //Button btn,
                 int num, int step, Context ctx){

        this.barTimer = pb;
        this.textTimer = text;
        //this.btnTimer = btn;
        this.ctx = ctx;
        this.step = step;

        // Add steps TODO
        this.testNum = num;
        setTimer(testNum, step);

        this.barTimer.setMax(seconds);
        this.barTimer.setProgress(seconds);
        this.barTimer.setSecondaryProgress(seconds);
        this.textTimer.setText(String.format("%02d", (int) (Math.floor(wait))) + ":" +
                String.format("%02d", seconds % SIXTY));

        AudioManager meng = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
        int volume = meng.getStreamVolume( AudioManager.STREAM_NOTIFICATION);

        if (volume != 0)
        {
            if (_shootMP == null)
                _shootMP = MediaPlayer.create(ctx, R.raw.ticking);
                _shootMP.setLooping(true);
            if (_shootMP2 == null)
                _shootMP2 = MediaPlayer.create(ctx, R.raw.tensecs);
        }

        if (_shootMP != null)
            _shootMP.start();

        //if(btnTimer != null) {
        //    this.btnTimer.setOnClickListener(new View.OnClickListener() {
        //@Override
        //        public void onClick(View v) {
        //            startTimer(wait);
        //            btnTimer.setEnabled(false);
        //            btnTimer.setVisibility(View.INVISIBLE);
        //        }
        //    });
        //}else{
            startTimer(wait);
        //}
    }

    public int getWaitSeconds(){
        return this.seconds;
    }

    public void setTimer(int testNum, int step){//add steps TODO
        switch(testNum){
            case 0:
                if(step == 0) wait = 0.25;
                else wait = 1;
                break;
            case 1:
                if(step == 0) wait = 0.5;
                else wait = 2;
                break;
            case 2:
                wait = 0.1;
                break;
            case 3:
                wait = 0.1;
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
                barTimer.setSecondaryProgress((int) secs);
                // format the textview to show the easily readable format
                textTimer.setText(String.format("%02d", secs / SIXTY) + ":" +
                        String.format("%02d", secs % SIXTY));
                if(step == 1){
                    if(secs <= 10 && _shootMP2 != null){
                        _shootMP2.start();
                    }
                }
            }

            @Override
            public void onFinish() {
                if(textTimer.getText().equals("00:00")){
                    if(step == 1) {
                        _shootMP.stop();
                        _shootMP.release();

                        _shootMP2.stop();
                        _shootMP2.release();

                        Intent intent = new Intent(ctx, CameraActivity.class);
                        intent.putExtra("test_num", testNum);

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ctx.startActivity(intent);
                    }else{
                        // Resets timer
                        barTimer.setMax(seconds);
                        barTimer.setProgress(seconds);
                        barTimer.setSecondaryProgress(seconds);
                        textTimer.setText(String.format("%02d", (int) (Math.floor(wait))) + ":" +
                                String.format("%02d", seconds % SIXTY));
                        startTimer(wait); //TODO end it
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
