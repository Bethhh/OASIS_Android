package com.bethyueshi.oasis2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraActivity extends Activity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private static final String TAG = "Camera";
    double latitude;
    double longitude;
    String img;
    private String API_KEY =  "271a72b8dd082c6";
    ProgressBar progressBar;
    String encodedImage = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Create an instance of Camera
        //mCamera
        getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        progressBar = (ProgressBar) findViewById(R.id.progressBar4);
        progressBar.setVisibility(View.INVISIBLE);


        // Add a listener to the Capture button
        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);
                        GPSTracker tracker = new GPSTracker(CameraActivity.this);
                        if (tracker.canGetLocation() == false) {
                            tracker.showSettingsAlert();
                        } else {
                            latitude = tracker.getLatitude();
                            longitude = tracker.getLongitude();
                        }
                    }
                }
        );
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            uploadImage(data);
        }
    };

    private void uploadImage(byte[] data) {


        encodedImage = Base64.encodeToString(data, Base64.DEFAULT);
        Log.d(TAG, encodedImage);

        //URL to imgur

        new UploadImgur().execute();
    }

    class UploadImgur extends AsyncTask<Void, Void, Integer> {
        protected void onPreExecute(){
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... params){

            String ret = "";

            final String upload_to = "https://api.imgur.com/3/upload.json";
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(upload_to);

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("type", "base64");
                jsonObject.accumulate("image", encodedImage);


                String json = jsonObject.toString();
                Log.d(TAG, json); //json sent

                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);

                httpPost.setHeader("Authorization", "Client-ID " +API_KEY);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                final HttpResponse response = httpClient.execute(httpPost,
                        localContext);

                final String response_string = EntityUtils.toString(response
                        .getEntity());

                jsonObject = new JSONObject(response_string);
                jsonObject = jsonObject.getJSONObject("data");

                Log.d("JSON", jsonObject.getString("link").toString()); //for my own understanding
                ret = jsonObject.getString("link").toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return prepareAndSendData(ret);
        }

        protected void onPostExecute(Integer status){
            progressBar.setVisibility(View.INVISIBLE);
            //Do somehting with status
        }
    }

    private Integer prepareAndSendData(String url){
        Integer status;
        //time stamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        final String submit_to = "https://distributed-health.herokuapp.com/distributed_healths.json";
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(submit_to);

        try {

            JSONObject jsonObject = new JSONObject();

            jsonObject.accumulate("ph", 2);
            jsonObject.accumulate("magnified_Link", url);
            jsonObject.accumulate("lat", latitude);
            jsonObject.accumulate("long", longitude);
            //jsonObject.accumulate("timestamp", timeStamp);

            String json = jsonObject.toString();
            //json = "{\"id\":{\"$oid\":\"5588d50e6437320003000000\"},\"ph\":32.0,\"chlorine\":3.0,\"magnified_Link\":\"http://fdasfasf.com\",\"taste\":\"3\",\"odor\":\"4\",\"temperature\":4.0,\"mercury\":4.0,\"hardness\":4.0,\"lat\":4.0,\"long\":4.0,\"url\":\"https://distributed-health.herokuapp.com/distributed_healths/5588d50e6437320003000000.json\"}";
            Log.d(TAG, json);  //json sent

            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            final HttpResponse response = httpClient.execute(httpPost, localContext);
            final String response_string = EntityUtils.toString(response.getEntity());
            jsonObject = new JSONObject(response_string);

            Log.d("JSON", jsonObject.toString()); //Response (json)

            status = response.getStatusLine().getStatusCode();
            return status;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    private void releaseCameraAndPreview() {
        //mPreview.setCamera(null);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }
    /** A safe way to get an instance of the Camera object. */
    public void getCameraInstance(){
        releaseCameraAndPreview();
        //Camera c = null;
        try {
            mCamera = Camera.open(0); // attempt to get a Camera instance
            if(mCamera == null)
                Log.d(TAG, "Camera is null");
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            Log.d(TAG, "Error getting camera: " + e.getMessage());
        }
        //xreturn c; // returns null if camera is unavailable
    }

    @Override
    protected void onPause() {
        super.onPause();
        //releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }


    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }
}
