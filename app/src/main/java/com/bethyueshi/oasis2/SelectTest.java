package com.bethyueshi.oasis2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.RecyclerView;


public class SelectTest extends FragmentActivity {
    private int[] gifBtns = new int[4];

    private String[] testTextFiller = new String[]{"pH","Chlorine","Taste","Odor",
            "Temperature","Mercury","Hardness","русский", "OASIS"};
    private int currTest = R.drawable.t1;
    private int testNum = 0;
    public static final int TOTAL_TEST = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_test);

        Intent intent = getIntent();
        testNum = intent.getIntExtra("test_num", 0);
        currTest = getTestVideo(testNum);

        Log.d("testNum", " " + testNum);

        VideoFragment vf = (VideoFragment) getSupportFragmentManager()
                .findFragmentById(R.id.video_instr);
        vf.setGIF(currTest, 300);

        RecyclerView testListView = (RecyclerView) findViewById(R.id.testRecyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        testListView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.scrollToPosition(testNum);
        testListView.setLayoutManager(layoutManager);

        gifBtns[0] = R.drawable.t1;
        gifBtns[1] = R.drawable.t2;
        gifBtns[2] = R.drawable.t1;
        gifBtns[3] = R.drawable.t2;

        // Create the adapter passing a reference to the XML layout for each row
        // and a reference to the EditText (or TextView) in the item XML layout
        TestListAdapter adapter = new TestListAdapter(gifBtns, testNum);
        // specify an adapter (see also next example)
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

    public int getTestNum(){
        return this.testNum;
    }
}
