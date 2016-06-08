package com.example.admin.animewatchmaster.databaseUtils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by admin on 11/6/2015.
 */
public class jsonDataImport {


    private static final String CLASS_TAG = "jsonDataImport - ";

    public static JSONArray getAllanimeData(String base_db_url){
        return getData(base_db_url+"/animedraw/drawclasses/drawallanime.php");
    }

    public static JSONArray getAnimeinfoData(String base_db_url){
        return getData(base_db_url+"/animedraw/drawclasses/drawanimeinfo.php");
    }

    public static JSONArray getAnimeultimaData(String base_db_url){
        return getData(base_db_url+"/animedraw/drawclasses/animeultima.php");
    }

    public static JSONArray getAllanimeDataByVersion(String base_db_url, int version){
        return getData(base_db_url+"/animedraw/drawclasses/drawallanimebyversion.php?version="+version);
    }

    public static JSONArray getAnimeinfoDataByVersion(String base_db_url, int version){
        return getData(base_db_url+"/animedraw/drawclasses/drawanimeinfobyversion.php?version="+version);
    }

    public static JSONArray getAnimeultimaDataByVersion(String base_db_url, int version){
        return getData(base_db_url+"/animedraw/drawclasses/drawanimultimabyversion.php?version="+version);
    }

    private static JSONArray getData(String db_url){
        final String TAG = CLASS_TAG+"getData";
        JSONArray jarr=null;
        try {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(db_url)
                    .build();

            Response response = client.newCall(request).execute();
            String result = response.body().string();

            jarr = new JSONArray(result);


        }  catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"IOException");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jarr;
    }

    public static JSONObject getVData(String db_url){
        final String TAG = CLASS_TAG+"getVData";
        JSONObject job=null;
        db_url+="/animedraw/drawclasses/drawversion.php";
        try {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(db_url)
                    .build();

            Response response = client.newCall(request).execute();
            String result = response.body().string();

            job = new JSONObject(result);


        }  catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"IOException");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return job;
    }
}
