package com.bethyueshi.oasis2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class CameraActivity extends Activity {

    private Camera mCamera;
    private CameraPreview mPreview;

    private static final String TAG = "Camera";
    double latitude;
    double longitude;
    String timeStamp;
    String encodedImage = "";
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

    ProgressBar progressBar;
    private String android_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            //if (ActivityCompat.shouldShowRequestPermissionRationale(this,
             //       Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            //} else {
            Log.d(TAG, "no permission request");

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            //}
        }else{
            getCameraInstance();
            if(mCamera == null){
                Log.d(TAG, "hahaCamerddddda is null");
            }
        }


        if(mCamera == null){
            Log.d(TAG, "hahaCdfdfdfdffamerddddda is null");
        }


        //Camera.Parameters parameters = mCamera.getParameters();
        //parameters.setPictureSize(480, 800);
        //mCamera.setParameters(parameters);

        // Add a listener to the Capture button
        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);

                        //time stamp
                        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                        //location
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

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            upload(data);
        }
    };

    private void upload(byte[] data){

        encodedImage = Base64.encodeToString(data, Base64.DEFAULT);
        Log.d(TAG, encodedImage);

        //Start Select Test
        Intent intent = new Intent(CameraActivity.this, FeedbackActivity.class);

        //intent.putExtra("lat", latitude);
        //intent.putExtra("lng", longitude);
        //intent.putExtra("ts", timeStamp);
        //intent.putExtra("img", encodedImage);

        new UploadImgur(progressBar, encodedImage,
                        latitude, longitude, timeStamp,
                        android_id, CameraActivity.this).execute();
        startActivity(intent);
    }

    /** Check if this device has a camera TODO maybe we need to call this*/
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
            //mCamera = null;
        }
    }

    /** A safe way to get an instance of the Camera object. */
    public void getCameraInstance() {
        releaseCameraAndPreview();

        int num = Camera.getNumberOfCameras();
        try {

            mCamera = Camera.open(); // attempt to get a Camera instance TODO check front/back camera
            if (mCamera == null)
                Log.d(TAG, "Camerad is null" + num + "hello");

        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.d(TAG, "Error getting camera: " + e.getMessage());
            e.printStackTrace();
            Log.d(TAG, "" + num + "hello");
        }

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();              // release the camera immediately on pause event
    }


    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // Create an instance of Camere
                    getCameraInstance();
                    Log.d(TAG, "try camera done");
                    if(mCamera == null){
                        Log.d(TAG, "Camerddddda is null");
                    }



                } else {
//TODO
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                Log.d(TAG, "get permission");
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
