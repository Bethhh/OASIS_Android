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

public class CameraActivity extends Activity {

    private Camera mCamera;
    private CameraPreview mPreview;

    private static final String TAG = "Camera";
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

    double latitude;
    double longitude;
    String timeStamp;
    String encodedImage = "";
    private String android_id;

    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        initializeCamera();

        // Add a listener to the Capture button
        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);// TODO: detect box and take a picture immediately without pressing???

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

        int testNum = getIntent().getIntExtra("test_num", 0);

        Intent intent;
        if(testNum == SelectTest.TOTAL_TEST - 1) {
            //TODO: handle wait for the last upload.
            intent = new Intent(CameraActivity.this, FeedbackActivity.class);
            //intent.putExtra("test_num", getIntent().getIntExtra("test_num", 0));
        }else{
            intent = new Intent(CameraActivity.this, SelectTest.class);
            intent.putExtra("test_num", getIntent().getIntExtra("test_num", 0) + 1);
        }

        //intent.putExtra("lat", latitude);
        //intent.putExtra("lng", longitude);
        //intent.putExtra("ts", timeStamp);
        //intent.putExtra("img", encodedImage);

        new UploadImgur(progressBar, encodedImage,
                        latitude, longitude, timeStamp,
                        android_id, testNum, CameraActivity.this).execute();

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

    /** A safe way to get an instance of the Camera object. */
    private void getCameraInstance() {
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

    private void initializeCamera() {
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
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCameraAndPreview(); //release the camera immediately on pause event
    }

    @Override
    protected void onStop() {
        super.onStop();
        //releaseCameraAndPreview();              // release the camera immediately on pause event
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        // Get the Camera instance as the activity achieves full user focus
        if (mCamera == null) {
            getCameraInstance(); // Local method to handle camera init
        }
    }

    private void releaseCameraAndPreview() {
        if(mPreview != null) {
            mPreview.setCamera(null);
        }
        if (mCamera != null) {
            mCamera.release(); // release the camera for other applications
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