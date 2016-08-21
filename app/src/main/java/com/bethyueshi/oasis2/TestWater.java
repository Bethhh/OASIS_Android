package com.bethyueshi.oasis2;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by bethyueshi on 4/21/16.
 */
public class TestWater extends AppCompatActivity {
    private ProgressBar barTimer;
    private TextView textTimer;
    private int testNum = 0;
    private Button btnDone;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_water);

        testNum = getIntent().getIntExtra("test_num", 0);

        VideoFragment vf = (VideoFragment) getSupportFragmentManager().findFragmentById(R.id.init_video_instr);
        vf.setGIF(AppConfiguration.getTestVideo(testNum), 100);
        vf.setClickable(false);


        barTimer = (ProgressBar)findViewById(R.id.barTimer);
        textTimer = (TextView)findViewById(R.id.textTimer);
        btnDone = (Button)findViewById(R.id.button_done);
        btnDone.setEnabled(false);

        Handler mHandler = new Handler();

        Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                btnDone.setEnabled(true);
            }
        };

        this.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestWater.this, TimerActivity.class);
                intent.putExtra("test_num", testNum);
                startActivity(intent);
            }
        });


        timer = new Timer(barTimer, textTimer, testNum, 0, getApplicationContext());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mHandler.postDelayed(mRunnable, timer.getWaitSeconds()*1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer._shootMP != null) {
            timer._shootMP.stop();
            //timer._shootMP.release();
        }
    }
}
