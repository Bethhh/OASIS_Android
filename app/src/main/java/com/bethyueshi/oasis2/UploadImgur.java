package com.bethyueshi.oasis2;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import android.provider.Settings.Secure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bethyueshi on 6/22/15.
 * Upload to imgur and submit to database
 */
class UploadImgur extends AsyncTask<Void, Void, Integer> {

    private static final String TAG = "UploadImgur";
    private static final String API_KEY =  "271a72b8dd082c6"; //Imgur client ID

    private ProgressBar progressBar;
    private String encodedImage;
    private double latitude;
    private double longitude;
    private String timestamp;
    private int testNum;
    private Context ctx;
    private MediaPlayer _shootMP=null;

    public UploadImgur(ProgressBar pb, String data, double lat, double lng,
                       String ts, int test_num, Context context){
        this.progressBar = pb;
        this.encodedImage = data;
        this.latitude = lat;
        this.longitude = lng;
        this.timestamp = ts;
        this.testNum = test_num;
        this.ctx = context.getApplicationContext();
    }

    protected void onPreExecute(){
        progressBar.setVisibility(View.VISIBLE);
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... params){

        //Integer status;
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

            httpPost.setHeader("Authorization", "Client-ID " + API_KEY);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            final HttpResponse response = httpClient.execute(httpPost,
                    localContext);

            final String response_string = EntityUtils.toString(response
                    .getEntity());

            jsonObject = new JSONObject(response_string);
            jsonObject = jsonObject.getJSONObject("data");

            Log.d("JSON", jsonObject.getString("link").toString()); //for my own understanding
            ret = jsonObject.getString("link").toString();//return url from imgur

            //status = response.getStatusLine().getStatusCode();
            //return status;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return prepareAndSendData(ret);
    }

    private void shootSound()
    {
        AudioManager meng = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
        int volume = meng.getStreamVolume( AudioManager.STREAM_NOTIFICATION);

        if (volume != 0)
        {
            if (_shootMP == null)
                _shootMP = MediaPlayer.create(ctx, R.raw.alert);
            if (_shootMP != null)
                _shootMP.start();
        }
    }

    protected void onPostExecute(Integer status){
        progressBar.setVisibility(View.INVISIBLE);

        // TODO Do somehting with status code

        shootSound();

        Intent intent;
        if(testNum == AppConfiguration.TOTAL_TEST - 1) {
            intent = new Intent(ctx, FeedbackActivity.class);
        }else{
            intent = new Intent(ctx, SelectTest.class);
            intent.putExtra("test_num", testNum + 1);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    private String genPostUrl(String androidId, String item, String val, String opt){

        String add =  AppConfiguration.STR_SUBMIT_TO + AppConfiguration.STR_PUT + item + AppConfiguration.STR_PHP;
        if(item == AppConfiguration.STR_GEO){
            return add + "?" + "id='" + androidId +
                               "'&" + AppConfiguration.STR_LAT + "=" + val + "&" + AppConfiguration.STR_LONG + "=" + opt;
        }else if (item == AppConfiguration.STR_PHOTO) {
            return add + "?" + "id='" + androidId + "'&" + item + "='" + val + "'";
        }else {
            return add + "?" + "id=" + androidId + "&" + item + "=" + val; // TODO update ph id in database
        }
    }

    private Integer prepareAndSendData(String url){
        Integer status;

        try {
            String android_id = Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);
            Log.d("Android","Android ID : "+android_id);

            String postURL_ph = genPostUrl(android_id, AppConfiguration.STR_PH, String.valueOf(8.8), null);
            String postURL_photo = genPostUrl(android_id, AppConfiguration.STR_PHOTO, url, null);
            String postURL_geo = genPostUrl(android_id, AppConfiguration.STR_GEO, String.valueOf(latitude),
                                                                 String.valueOf(longitude));

            Log.d(TAG, postURL_ph);
            Log.d(TAG, postURL_photo);
            Log.d(TAG, postURL_geo);

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost_ph = new HttpPost(postURL_ph);
            HttpPost httpPost_photo = new HttpPost(postURL_photo);
            HttpPost httpPost_geo = new HttpPost(postURL_geo);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            httpPost_ph.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            httpPost_photo.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            httpPost_geo.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            httpPost_ph.setHeader("Accept", "application/x-www-form-urlencoded;charset=UTF-8");
            httpPost_ph.setHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
            httpPost_photo.setHeader("Accept", "application/x-www-form-urlencoded;charset=UTF-8");
            httpPost_photo.setHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
            httpPost_geo.setHeader("Accept", "application/x-www-form-urlencoded;charset=UTF-8");
            httpPost_geo.setHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");

            final HttpResponse response_ph = httpClient.execute(httpPost_ph, localContext);
            final String response_string_ph = EntityUtils.toString(response_ph.getEntity());
            Log.d("REST", "response ph : " + response_string_ph);

            final HttpResponse response_photo = httpClient.execute(httpPost_photo, localContext);
            final String response_string_photo = EntityUtils.toString(response_photo.getEntity());
            Log.d("REST", "response photo : " + response_string_photo);

            final HttpResponse response_geo = httpClient.execute(httpPost_geo, localContext);
            final String response_string_geo = EntityUtils.toString(response_geo.getEntity());
            Log.d("REST", "response geo : " + response_string_geo);


            //TODO status code
            status = response_ph.getStatusLine().getStatusCode();
            Log.d("REST","status : "+ status);
            return status;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}