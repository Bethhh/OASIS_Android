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
            //Bitmap bitmap = image;

            // Creates Byte Array from picture
            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); // Not sure whether this should      be jpeg or png, try both and see which works best

            String ret = "";
            /*try{
                url = new URL("http://api.imgur.com/3/upload");
            } catch(MalformedURLException e){
                Log.d(TAG, "Error Malformed: " + e.getMessage());
            }
            //encodes picture with Base64 and inserts api key

            String urldata = "";

            try {
                urldata = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(encodedImage.toString(), "UTF-8");
                urldata += "&" + URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode(API_KEY, "UTF-8");
            }catch(UnsupportedEncodingException e){
                Log.d(TAG, "Error encoding: " + e.getMessage());
            }

            // opens connection and sends data
            URLConnection conn;
            try {
                if(url != null) {
                    conn = url.openConnection();
                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(urldata);
                    wr.flush();
                }
            }catch(IOException e){
                Log.d(TAG, "Error open url connection: " + e.getMessage());
            }*/

            final String upload_to = "https://api.imgur.com/3/upload.json";
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(upload_to);

            try {
                //final MultipartEntity entity = new MultipartEntity(
                  //      HttpMultipartMode.BROWSER_COMPATIBLE);
                /*String urldata="" ;
                try {
                    urldata = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(encodedImage.toString(), "UTF-8");
                    urldata += "&" + URLEncoder.encode("Client-ID", "UTF-8") + "=" + URLEncoder.encode(API_KEY, "UTF-8");
                }catch(UnsupportedEncodingException e){
                    Log.d(TAG, "Error encoding: " + e.getMessage());
                }
*/

                JSONObject jsonObject = new JSONObject();
                //jsonObject.accumulate("client_id", API_KEY);
                jsonObject.accumulate("type", "base64");
                jsonObject.accumulate("image", encodedImage);//URLEncoder.encode(encodedImage, "UTF-8"));


                String json = jsonObject.toString(); // Output to string
                Log.d(TAG, json);

                StringEntity se = new StringEntity(json);
                // put json string into server
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
                //return ret;

            } catch (Exception e) {
                e.printStackTrace();
            }

            //return ret;
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
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); //not being used at the moment

        final String upload_to = "https://distributed-health.herokuapp.com/distributed_healths.json";
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(upload_to);

        try {
            //final MultipartEntity entity = new MultipartEntity(
            //      HttpMultipartMode.BROWSER_COMPATIBLE);
                /*String urldata="" ;
                try {
                    urldata = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(encodedImage.toString(), "UTF-8");
                    urldata += "&" + URLEncoder.encode("Client-ID", "UTF-8") + "=" + URLEncoder.encode(API_KEY, "UTF-8");
                }catch(UnsupportedEncodingException e){
                    Log.d(TAG, "Error encoding: " + e.getMessage());
                }
*/

            JSONObject jsonObject = new JSONObject();
            //jsonObject.accumulate("client_id", API_KEY);
            jsonObject.put("ph", 2);
            jsonObject.put("chlorine", 2.0);
            jsonObject.put("magnified_Link", url);//URLEncoder.encode(encodedImage, "UTF-8"));
            jsonObject.put("taste", "yucky");
            jsonObject.put("odor", "smelly");
            jsonObject.put("temperature", "77.0");
            jsonObject.put("mercury", 234);
            jsonObject.put("hardness", 9.0);
            jsonObject.put("lat", latitude);
            jsonObject.put("long", longitude);

            String json = jsonObject.toString(); // Output to string
            Log.d(TAG, json);

            StringEntity se = new StringEntity(json);
            // put json string into server
            httpPost.setEntity(se);

            //httpPost.setHeader("Authorization", "Client-ID " +API_KEY);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            final HttpResponse response = httpClient.execute(httpPost,
                    localContext);

            final String response_string = EntityUtils.toString(response
                    .getEntity());

            jsonObject = new JSONObject(response_string);
            //jsonObject = jsonObject.getJSONObject("data");

            Log.d("JSON", jsonObject.toString()); //for my own understanding
            //ret = new URL(jsonObject.getString("link").toString());
            //return ret;
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
