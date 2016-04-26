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

    public static final String STR_SUBMIT_TO = "http://52.53.219.240/";
    public static final String STR_PHP = ".php";
    public static final String STR_PUT = "put";
    public static final String STR_GET = "get";

    public static final String STR_PH = "ph";
    public static final String STR_PHOTO = "photo";
    public static final String STR_GEO = "geo";
    public static final String STR_LAT = "lat";
    public static final String STR_LONG = "long";



    private ProgressBar progressBar;
    private String encodedImage;
    private double latitude;
    private double longitude;
    private String timestamp;
    private String aid;
    private int testNum;
    private Context ctx;

    public UploadImgur(ProgressBar pb, String data, double lat, double lng, String ts, String a_id, int test_num, Context context){
        this.progressBar = pb;
        this.encodedImage = data;
        this.latitude = lat;
        this.longitude = lng;
        this.timestamp = ts;
        this.aid = a_id;
        this.testNum = test_num;
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
        if(testNum == SelectTest.TOTAL_TEST - 1) {
            Intent intent = new Intent(ctx, FeedbackActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        }else{
            // TODO: let user know if the picture is upload successfully? or do we want to keep all photos and upload at the end?
        }
    }
    private String genPostUrl(String androidId, String item, String val, String opt){

        String add =  STR_SUBMIT_TO + STR_PUT + item + STR_PHP;
        if(item == STR_GEO){
            return add + "?" + "id=" + androidId +
                               "&" + STR_LAT + "=" + val + "&" + STR_LONG + "=" + opt;
        }else {
            return add + "?" + "id=" + androidId + "&" + item + "=" + val;
        }
    }

    private Integer prepareAndSendData(String url){
        Integer status;


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


            String postURL_ph = genPostUrl(android_id, STR_PH, String.valueOf(8.8), null);
            String postURL_photo = genPostUrl(android_id, STR_PHOTO, url, null);
            String postURL_geo = genPostUrl(android_id, STR_GEO, String.valueOf(latitude),
                                                                 String.valueOf(longitude));

            Log.d(TAG, postURL_ph);
            Log.d(TAG, postURL_photo);
            Log.d(TAG, postURL_geo);

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost_ph = new HttpPost("http://52.53.219.240/putph.php?id=ddf46d3eaf5ff1c7&ph=8.8");
//            HttpPost httpPost_photo = new HttpPost(postURL_photo);
//            HttpPost httpPost_geo = new HttpPost(postURL_geo);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            httpPost_ph.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
//            httpPost_photo.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
//            httpPost_geo.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            httpPost_ph.setHeader("Accept", "application/x-www-form-urlencoded;charset=UTF-8");
            httpPost_ph.setHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
//            httpPost_photo.setHeader("Accept", "application/x-www-form-urlencoded;charset=UTF-8");
//            httpPost_photo.setHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
//            httpPost_geo.setHeader("Accept", "application/x-www-form-urlencoded;charset=UTF-8");
//            httpPost_geo.setHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");

            final HttpResponse response_ph = httpClient.execute(httpPost_ph, localContext);
            final String response_string_ph = EntityUtils.toString(response_ph.getEntity());
            Log.d("REST", "response ph : " + response_string_ph);

//            final HttpResponse response_photo = httpClient.execute(httpPost_photo, localContext);
//            final String response_string_photo = EntityUtils.toString(response_photo.getEntity());
//            Log.d("REST", "response photo : " + response_string_photo);
//
//            final HttpResponse response_geo = httpClient.execute(httpPost_geo, localContext);
//            final String response_string_geo = EntityUtils.toString(response_geo.getEntity());
//            Log.d("REST", "response geo : " + response_string_geo);



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