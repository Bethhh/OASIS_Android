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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bethyueshi on 6/22/15.
 */
class UploadImgur extends AsyncTask<Void, Void, Integer> {
    private ProgressBar progressBar;
    private String encodedImage;
    private static final String TAG = "UploadImgur";
    private static final String API_KEY =  "271a72b8dd082c6";
    private double latitude;
    private double longitude;

    public UploadImgur(ProgressBar pb, String data, double lat, double lng){
        this.progressBar = pb;
        this.encodedImage = data;
        this.latitude = lat;
        this.longitude = lng;
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
            ret = jsonObject.getString("link").toString();

            status = response.getStatusLine().getStatusCode();
            return status;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        //need to notify the main activity the url TODO
        //return prepareAndSendData(ret);
    }

    protected void onPostExecute(Integer status){
        progressBar.setVisibility(View.INVISIBLE);
        //Do somehting with status
    }

    private Integer prepareAndSendData(String url){
        Integer status;
        //time stamp
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

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
            //json = "{\"ph\":32.0,\"chlorine\":3.0,\"magnified_Link\":\"http://fdasfasf.com\",\"taste\":\"3\",\"odor\":\"4\",\"temperature\":4.0,\"mercury\":4.0,\"hardness\":4.0,\"lat\":4.0,\"long\":4.0}";
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