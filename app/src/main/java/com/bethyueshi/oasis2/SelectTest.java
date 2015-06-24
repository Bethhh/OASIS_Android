package com.bethyueshi.oasis2;

import android.app.Activity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_test);

        // Get a reference to our ListView
        GridView gridView = (GridView) findViewById(R.id.gridView);
        testPic = new ArrayList<Bitmap>();
        Log.d("select","inHAHAHAHAHAH");
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
                Log.d("select","HAHAHAHAHAH");
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

                /*Toast.makeText(getApplicationContext(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                .show();*/
                //need to add pop up first
                boolean flag = true;
                while(flag){
                    if(allowToSubmit){//need to global
                        new SubmitData(progressBar, url, latitude, longitude).execute();
                    }

                }

                /*Intent intent = new Intent(ViewAlbumActivity.this, PicturesActivity.class);
                //Convert to byte array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitPic.get(position).compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                intent.putExtra("bitmap", byteArray);
                //use album name to interact with data base
                //EditText editText = (EditText) findViewById(R.id.edit_message);
                //String message = editText.getText().toString();
                //intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);

                // http request to data base to submit stuff*/

            }
        });

    }


}
