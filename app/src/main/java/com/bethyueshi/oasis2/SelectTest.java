package com.bethyueshi.oasis2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class SelectTest extends Activity {
    private Integer[] gifBtns = new Integer[9];

    private String android_id;
    private String[] testTextFiller = new String[]{"pH","Chlorine","Taste","Odor","Temperature","Mercury","Hardness","русский", "OASIS"};

    ProgressBar progressBar;
    double latitude = 0;
    double longitude = 0;
    String img = null;
    String timeStamp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_test);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        //Get extras in intent from previous activity
        Intent in = getIntent();
        latitude = in.getDoubleExtra("lat", 0);
        longitude = in.getDoubleExtra("lng", 0);

        Bundle extras = in.getExtras();
        if(extras != null) {
            timeStamp = extras.getString("ts");
            img = extras.getString("img");
        }

        android_id = android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        // Get a reference to our ListView
        GridView gridView = (GridView) findViewById(R.id.gridView);

        gifBtns[0] = R.drawable.t1;
        gifBtns[1] = R.drawable.t1;
        gifBtns[2] = R.drawable.t1;
        gifBtns[3] = R.drawable.t1;
        gifBtns[4] = R.drawable.t2;
        gifBtns[5] = R.drawable.t2;
        gifBtns[6] = R.drawable.t2;
        gifBtns[7] = R.drawable.t2;
        gifBtns[8] = R.drawable.t2;

        // Create the adapter passing a reference to the XML layout for each row
        // and a reference to the EditText (or TextView) in the item XML layout
        ArrayAdapter adapter = new ArrayAdapter(SelectTest.this, R.layout.grid_items, gifBtns) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row = convertView;

                if(row == null){
                    //getting custom layout for the row
                    LayoutInflater inflater = getLayoutInflater();//LayoutInflater.from(getActivity());
                    row = inflater.inflate(R.layout.grid_items, parent, false);
                }

                //TextView testNameField = (TextView)row.findViewById(R.id.test_name);
                GIFView picTop = (GIFView)row.findViewById(R.id.test_pic);

                //Here put images of tests
                //testNameField.setText(testTextFiller[position]);
                picTop.setSrc((int)getItem(position));
                // picBox.setImageBitmap(bitPic.get(position));
                return row; //the row that ListView draws
            }
        };


        // Set the adapter on the List View
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                new UploadImgur(progressBar, img,
                                latitude, longitude, timeStamp,
                                android_id, SelectTest.this).execute();
            }
        });
    }
}
