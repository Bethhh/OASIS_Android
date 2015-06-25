package com.bethyueshi.oasis2;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

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

    public UploadImgur(ProgressBar pb, String data, double lat, double lng, String ts){
        this.progressBar = pb;
        this.encodedImage = data;
        this.latitude = lat;
        this.longitude = lng;
        this.timestamp = ts;
    }
    protected void onPreExecute(){
        progressBar.setVisibility(View.VISIBLE);
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... params){

        Integer status;
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

    protected void onPostExecute(Integer status){
        progressBar.setVisibility(View.INVISIBLE);
        //Do somehting with status
    }

    private Integer prepareAndSendData(String url){
        Integer status;

        final String submit_to = "https://distributed-health.herokuapp.com/distributed_healths.json";
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(submit_to);

        try {

            JSONObject jsonObject = new JSONObject();

            //Test data
            jsonObject.put("ph", 2);
            jsonObject.put("chlorine", 3.0);
            jsonObject.put("magnified_Link", url);
            jsonObject.put("taste", "yucky");
            jsonObject.put("odor", "smelly");
            jsonObject.put("temperature", "77.0");
            jsonObject.put("mercury", 234);
            jsonObject.put("hardness", 9.0);
            jsonObject.put("lat", latitude);
            jsonObject.put("long", longitude);
            jsonObject.put("timestamp", timestamp);
            jsonObject.put("testdata", true);


            String json = jsonObject.toString();
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
}