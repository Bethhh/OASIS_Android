package com.bethyueshi.oasis2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TimerActivity extends AppCompatActivity {
    private ProgressBar barTimer;
    private TextView textTimer;
    private int testNum = 0;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        testNum = getIntent().getIntExtra("test_num", 0);

        VideoFragment vf = (VideoFragment) getSupportFragmentManager().findFragmentById(R.id.init_video_instr);
        vf.setGIF(AppConfiguration.getRecordVideo(testNum), 100);
        vf.setClickable(false);

        barTimer = (ProgressBar)findViewById(R.id.barTimer);
        textTimer = (TextView)findViewById(R.id.textTimer);

        timer = new Timer(barTimer, textTimer, testNum, 1, getApplicationContext());

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
