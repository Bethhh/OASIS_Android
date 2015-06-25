package com.bethyueshi.oasis2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class SelectTest extends Activity {

    private List<Bitmap> testPic;
    private Integer[] hei = new Integer[9];

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

        // Get a reference to our ListView
        GridView gridView = (GridView) findViewById(R.id.gridView);
        testPic = new ArrayList<Bitmap>();

        hei[0] = 1;
        hei[1] = 2;
        hei[3] = 4;
        hei[2] = 3;

        // Create the adapter passing a reference to the XML layout for each row
        // and a reference to the EditText (or TextView) in the item XML layout
        ArrayAdapter adapter = new ArrayAdapter(SelectTest.this, R.layout.grid_items, hei) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row = convertView;
                final int pos = position;
                if(row == null){
                    //getting custom layout for the row
                    LayoutInflater inflater = getLayoutInflater();//LayoutInflater.from(getActivity());
                    row = inflater.inflate(R.layout.grid_items, parent, false);
                }

                //get the reference to the EditText of your row.
                //find the item with row.findViewById()
                TextView testNameField = (TextView)row.findViewById(R.id.test_name);
                //ImageView picBox = (ImageView)row.findViewById(R.id.album_pic);
                //Here put images of tests

                testNameField.setText("haha");//Integer.toString(position + 1));
                // picBox.setImageBitmap(bitPic.get(position));
                return row; //the row that ListView draws
            }

            /*<ImageView
                android:layout_width="150dp"
                android:layout_height="100dp"
                android:scaleType="fitXY"

                android:background="@color/material_blue_grey_900"
                android:id="@+id/test_pic"*/

        };


        // Set the adapter on the List View
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                new UploadImgur(progressBar, img, latitude, longitude, timeStamp).execute();

            }
        });

    }


}
