package com.bethyueshi.oasis2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends Activity {

    private Camera mCamera;
    private CameraPreview mPreview;

    private static final String TAG = "Camera";
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

    private double latitude;
    private double longitude;
    private String timeStamp;
    private String encodedImage = "";
    private String android_id;

    private ProgressBar progressBar;

    private MediaPlayer _shootMP=null;
    private ImageButton capture;
    private int purpose;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        capture = (ImageButton) findViewById(R.id.image_capture);
        purpose = getIntent().getIntExtra("camera_purpose", AppConfiguration.PURPOSE_TEST);

        initializeCamera();

        // Add a listener to the Capture button
        capture.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(purpose == AppConfiguration.PURPOSE_PROFILE){
                            mCamera.takePicture(null, null, mPicture2);
                            return;
                        }

                        mCamera.autoFocus(new Camera.AutoFocusCallback() {
                            @Override
                            public void onAutoFocus(boolean success, Camera camera) {
                                //if(success){
                                    // get an image from the camera
                                    mCamera.takePicture(null, null, mPicture);// TODO: detect box and take a picture immediately without pressing???

                                //}
                            }
                        });

                        // get an image from the camera
                        //mCamera.takePicture(null, null, mPicture);// TODO: detect box and take a picture immediately without pressing???


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
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            shootSound();
            upload(data);
        }
    };

    private Camera.PictureCallback mPicture2 = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            shootSound();
            saveToInternalStorage(data);

            Intent intent = new Intent(CameraActivity.this, FeedbackActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    };

    private String saveToInternalStorage(byte[] img){

        File photo = new File(CameraActivity.this.getFilesDir().getAbsolutePath(),"profile.jpg");

        if (photo.exists()) {
            Log.d("Save", "FILE EXISTS");
            photo.delete();
        }

        FileOutputStream fos;

        try {
            fos = openFileOutput("profile.jpg", Context.MODE_PRIVATE);

            fos.write(img);
            fos.close();
        }
        catch (java.io.IOException e) {
            Log.e("Save picture", "Exception in photoCallback", e);
        }

        Log.d("Profile path: ", CameraActivity.this.getFilesDir().getAbsolutePath());
        return CameraActivity.this.getFilesDir().getAbsolutePath();
    }


    public void shootSound()
    {
        AudioManager meng = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        int volume = meng.getStreamVolume( AudioManager.STREAM_NOTIFICATION);

        if (volume != 0)
        {
            if (_shootMP == null)
                _shootMP = MediaPlayer.create(getApplicationContext(), Uri.parse("file:///system/media/audio/ui/camera_click.ogg"));
            if (_shootMP != null)
                _shootMP.start();
        }
    }

    private void upload(byte[] data){

        encodedImage = Base64.encodeToString(data, Base64.DEFAULT);
        Log.d(TAG, encodedImage);

        int testNum = getIntent().getIntExtra("test_num", 0);

        new UploadImgur(progressBar, encodedImage,
                        latitude, longitude, timeStamp, testNum, CameraActivity.this).execute();
    }

    /** Check if this device has a camera TODO maybe we need to call this*/
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // This device has a camera
            return true;
        } else {
            // No camera on this device
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
                Log.d(TAG, "Camerad is null" + num);

        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.d(TAG, "Error getting camera: " + e.getMessage());
            e.printStackTrace();
        }

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera, capture, purpose);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }

    private void initializeCamera() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "Requesting Camera Permission");

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
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
            getCameraInstance();
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

                    Log.d(TAG, "Camera permission is granted");
                    // Create an instance of Camere
                    getCameraInstance();

                    if(mCamera == null){
                        Log.d(TAG, "Camera is null in requesting permission");
                    }

                } else {
                    Log.d(TAG, "Camera permission is denied");
                    Toast.makeText(CameraActivity.this, "Camera permission is required!", Toast.LENGTH_SHORT).show();
                    initializeCamera();
                }
                return;
            }

        }
    }
}