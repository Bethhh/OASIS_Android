package com.bethyueshi.oasis2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.support.v7.widget.RecyclerView;


public class SelectTest extends FragmentActivity {
    private int[] gifBtns = new int[4];

    private String[] testTextFiller = new String[]{"pH","Chlorine","Taste","Odor",
            "Temperature","Mercury","Hardness","русский", "OASIS"};
    private int currTest = R.drawable.t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_test);


        VideoFragment vf = (VideoFragment) getSupportFragmentManager()
                .findFragmentById(R.id.video_instr);
        vf.setGIF(currTest, 600);

        RecyclerView testListView = (RecyclerView) findViewById(R.id.testRecyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        testListView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        testListView.setLayoutManager(layoutManager);

        gifBtns[0] = R.drawable.t1;
        gifBtns[1] = R.drawable.t1;
        gifBtns[2] = R.drawable.t1;
        gifBtns[3] = R.drawable.t1;

        // Create the adapter passing a reference to the XML layout for each row
        // and a reference to the EditText (or TextView) in the item XML layout
        TestListAdapter adapter = new TestListAdapter(gifBtns);
        // specify an adapter (see also next example)
        testListView.setAdapter(adapter);


        testListView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
                        //new UploadImgur(progressBar, img,
                        //                latitude, longitude, timeStamp,
                        //                android_id, SelectTest.this).execute();
                        // TODO position
                        Intent intent = new Intent(SelectTest.this, CameraActivity.class);
                        startActivity(intent);
                    }
                })
        );
    }
}
