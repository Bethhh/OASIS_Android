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
class UploadValue extends AsyncTask<Void, Void, Integer> {

    private static final String TAG = "UploadValue";

    public static final String STR_SUBMIT_TO = "http://52.53.177.54/";
    public static final String STR_PHP = ".php";
    public static final String STR_PUT = "put";
    public static final String STR_GET = "get";

    public static final String STR_PH = "ph";
    public static final String STR_PHOTO = "photo";
    public static final String STR_GEO = "geo";
    public static final String STR_METAL = "metals";
    public static final String STR_LAT = "lat";
    public static final String STR_LONG = "long";


    private ProgressBar progressBar;

    private double latitude;
    private double longitude;
    private String timestamp;
    private String aid;
    private Context ctx;
    private String[] values;

    public UploadValue(ProgressBar pb, String[] values, double lat, double lng,
                       String ts, String a_id, Context context){
        this.progressBar = pb;
        this.values = values;
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
        return prepareAndSendData(this.values);
    }

    protected void onPostExecute(Integer status){
        progressBar.setVisibility(View.INVISIBLE);

        Intent intent = new Intent(ctx, MapActivity.class);
        intent.putExtra("test_num", 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    private String genPostUrl(String androidId, String item, String val, String opt){

        String add =  STR_SUBMIT_TO + STR_PUT + item + STR_PHP;
        if(item == STR_GEO){
            return add + "?" + "id='" + androidId +
                    "'&" + STR_LAT + "=" + val + "&" + STR_LONG + "=" + opt;
        }else if (item == STR_PHOTO) {
            return add + "?" + "id='" + androidId + "'&" + item + "='" + val + "'";
        }else {
            return add + "?" + "id=" + androidId + "&" + item + "=" + val; // TODO update ph id in database
        }
    }

    private Integer prepareAndSendData(String[] values){
        Integer status;

        try {
            String android_id = Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);
            Log.d("Android","Android ID : "+android_id);

            String postURL_ph = genPostUrl(android_id, STR_PH, String.valueOf(values[0]), null);
            String postURL_metal = genPostUrl(android_id, STR_METAL, String.valueOf(values[1]), null);
            String postURL_geo = genPostUrl(android_id, STR_GEO, String.valueOf(latitude),
                    String.valueOf(longitude));

            Log.d(TAG, postURL_ph);
            Log.d(TAG, postURL_geo);
            Log.d(TAG, postURL_metal);

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost_ph = new HttpPost(postURL_ph);
            HttpPost httpPost_metal = new HttpPost(postURL_metal);
            HttpPost httpPost_geo = new HttpPost(postURL_geo);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            httpPost_ph.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            httpPost_metal.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            httpPost_geo.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            httpPost_ph.setHeader("Accept", "application/x-www-form-urlencoded;charset=UTF-8");
            httpPost_ph.setHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
            httpPost_metal.setHeader("Accept", "application/x-www-form-urlencoded;charset=UTF-8");
            httpPost_metal.setHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
            httpPost_geo.setHeader("Accept", "application/x-www-form-urlencoded;charset=UTF-8");
            httpPost_geo.setHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");

            final HttpResponse response_ph = httpClient.execute(httpPost_ph, localContext);
            final String response_string_ph = EntityUtils.toString(response_ph.getEntity());
            Log.d("REST", "response ph : " + response_string_ph);

            final HttpResponse response_metal = httpClient.execute(httpPost_metal, localContext);
            final String response_string_metal = EntityUtils.toString(response_metal.getEntity());
            Log.d("REST", "response metal : " + response_string_metal);

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