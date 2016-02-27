package com.bethyueshi.oasis2;

import android.content.Context;
import android.content.Intent;
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
    private String aid;
    private Context ctx;

    public UploadImgur(ProgressBar pb, String data, double lat, double lng, String ts, String a_id, Context context){
        this.progressBar = pb;
        this.encodedImage = data;
        this.latitude = lat;
        this.longitude = lng;
        this.timestamp = ts;
        this.aid = a_id;
        this.ctx = context.getApplicationContext();
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
        //Do somehting with status code TODO

        Intent intent = new Intent(ctx, FeedbackActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    private Integer prepareAndSendData(String url){
        Integer status;
        final String submit_to = "http://52.53.219.240/putph.php";

        try {
            String android_id = Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);
            Log.d("Android","Android ID : "+android_id);

            //Test data
            /*jsonObject.put("ph", 2);
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
            jsonObject.put("id", android_id);*/


            String postURL = submit_to + "?" + "id=" + android_id + "&ph=" + 8.8;
            Log.d(TAG, postURL);

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(postURL);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            httpPost.setHeader("Accept", "application/x-www-form-urlencoded;charset=UTF-8");
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");

            final HttpResponse response = httpClient.execute(httpPost, localContext);
            final String response_string = EntityUtils.toString(response.getEntity());
            Log.d("REST","response : "+ response_string);


            //if(response_string.charAt(response_string.length()-2)=='1'){
            //    Log.d("Upload", "Successful POST");
            //}else{
            //    Log.d("Upload", "Failure" + response_string.charAt(response_string.length()-1));
            //}

            //TODO status code
            status = response.getStatusLine().getStatusCode();
            Log.d("REST","status : "+ status);
            return status;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}