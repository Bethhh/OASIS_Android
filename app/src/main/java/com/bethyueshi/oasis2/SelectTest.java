package com.bethyueshi.oasis2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.support.v7.widget.RecyclerView;


public class SelectTest extends FragmentActivity {

    private int[] gifBtns = new int[AppConfiguration.TOTAL_TEST];

    //private String[] testTextFiller = new String[]{"pH","Chlorine","Taste","Odor",
    //        "Temperature","Mercury","Hardness","русский", "OASIS"};
    private int currTest = R.drawable.t1;
    private int testNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_test);

        Intent intent = getIntent();
        testNum = intent.getIntExtra("test_num", 0);
        currTest = AppConfiguration.getTestVideo(testNum);

        Log.d("Current testNum:", " " + testNum);

        VideoFragment vf = (VideoFragment) getSupportFragmentManager().findFragmentById(R.id.video_instr);
        vf.setGIF(currTest, 300);

        RecyclerView testListView = (RecyclerView) findViewById(R.id.testRecyclerView);

        // Uses this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        testListView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.scrollToPosition(testNum);
        testListView.setLayoutManager(layoutManager);

        //TODO optimize this
        gifBtns[0] = R.drawable.testph;
        gifBtns[1] = R.drawable.testmetal;
        //gifBtns[2] = R.drawable.testph;
        //gifBtns[3] = R.drawable.testph;

        TestListAdapter adapter = new TestListAdapter(gifBtns, testNum);
        testListView.setAdapter(adapter);


//        testListView.addOnItemTouchListener(
//                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//
//                    }
//                })
//        );
    }

    public int getTestNum(){
        return this.testNum;
    }
}
