package com.bethyueshi.oasis2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;


import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraActivity extends Activity {

    private Camera mCamera;
    private CameraPreview mPreview;

    private static final String TAG = "Camera";
    double latitude;
    double longitude;
    String timeStamp;
    String encodedImage = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Create an instance of Camere
        getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

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
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            chooseTest(data);
        }
    };

    private void chooseTest(byte[] data){

        encodedImage = Base64.encodeToString(data, Base64.DEFAULT);
        Log.d(TAG, encodedImage);

        //Start Select Test
        Intent intent = new Intent(CameraActivity.this, SelectTest.class);

        intent.putExtra("lat", latitude);
        intent.putExtra("lng", longitude);
        intent.putExtra("ts", timeStamp);
        intent.putExtra("img", encodedImage);

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
            mCamera = null;
        }
    }

    /** A safe way to get an instance of the Camera object. */
    public void getCameraInstance() {
        releaseCameraAndPreview();

        try {
            mCamera = Camera.open(0); // attempt to get a Camera instance TODO check front/back camera
            if (mCamera == null)
                Log.d(TAG, "Camera is null");
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.d(TAG, "Error getting camera: " + e.getMessage());
        }
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
}
