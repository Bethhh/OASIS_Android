package com.bethyueshi.oasis2;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;


public class SelectTest extends FragmentActivity {
    private int[] gifBtns = new int[4];

    //private String android_id;
    private String[] testTextFiller = new String[]{"pH","Chlorine","Taste","Odor","Temperature","Mercury","Hardness","русский", "OASIS"};
    private int currTest = R.drawable.t1;

    //double latitude = 0;
    //double longitude = 0;
    //String img = null;
    //String timeStamp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_test);


        VideoFragment vf = (VideoFragment) getSupportFragmentManager()
                .findFragmentById(R.id.video_instr);
        vf.setGIF(currTest, 600);
        //Get extras in intent from previous activity
        //Intent in = getIntent();
        //latitude = in.getDoubleExtra("lat", 0);
        //longitude = in.getDoubleExtra("lng", 0);

        //Bundle extras = in.getExtras();
        //if(extras != null) {
            //timeStamp = extras.getString("ts");
            //img = extras.getString("img");
        //}

        //android_id = android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
        //        Settings.Secure.ANDROID_ID);

        // Get a reference to our ListView
        //GridView gridView = (GridView) findViewById(R.id.gridView);

        // get fragment manager
        //FragmentManager fm = getSupportFragmentManager();

        // add
        //FragmentTransaction ft = fm.beginTransaction();

        //Fragment fragment = new VideoFragment();
        //Bundle args = new Bundle();
        //args.putInt("resId", currTest);
        //fragment.setArguments(args);

        //ft.add(R.id.video_instr, fragment);
        // alternatively add it with a tag
        // trx.add(R.id.your_placehodler, new YourFragment(), "detail");
        //ft.commit();

        // replace
        //FragmentTransaction ft = fm.beginTransaction();
        //ft.replace(R.id.your_placehodler, new YourFragment());
        //ft.commit();

        // remove
        //Fragment fragment = fm.findFragmentById(R.id.your_placehodler);
        //FragmentTransaction ft = fm.beginTransaction();
        //ft.remove(fragment);
        //ft.commit();

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
