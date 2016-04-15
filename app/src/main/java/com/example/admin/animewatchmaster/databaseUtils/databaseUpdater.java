package com.example.admin.animewatchmaster.databaseUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.example.admin.animewatchmaster.model.Anime;

/**
 * Created by admin on 4/12/2016.
 */
//json import
public class databaseUpdater extends AsyncTask<String,Void,Boolean> {
    public Context mainContext;
    public DBHelper animedb;

    public databaseUpdater(Context context,DBHelper db){
        mainContext=context;
        animedb=db;
    }

    ProgressDialog dialog;
    @Override
    protected void onPreExecute(){
        dialog = ProgressDialog.show(mainContext,"Database Update","Updating database",true);
    }

    @Override
    protected Boolean doInBackground(String... databaseurl) {

            JSONArray jarr = jsonDataImport.getAllanimeData(databaseurl[0]);
            System.out.println(jarr.toString());
            jarr = jsonDataImport.getAnimeinfoData(databaseurl[0]);
            System.out.println(jarr.toString());
            for(int i=0; i<jarr.length(); i++){
                try {
                    JSONObject job = (JSONObject) jarr.get(i);
                    if(!animedb.checkIfExists(job.getString("title"))) {
                        System.out.println(job.getInt("id"));
                        System.out.println(job.getString("title"));
                        System.out.println(job.getString("imgurl"));
                        System.out.println(job.getString("genre"));
                        System.out.println(job.getString("episodes"));
                        System.out.println(job.getString("agerating"));
                        System.out.println(job.getString("animetype"));
                        System.out.println(job.getString("description"));
                        boolean s = animedb.insertAnime(job.getString("title"), job.getString("imgurl"), job.getString("genre"), job.getString("episodes"), job.getString("animetype"), job.getString("agerating"), job.getString("description"));
                        System.out.println(s);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            /*
            jarr = jsonDataImport.getAnimeultimaData(databaseurl[0]);
            System.out.println(jarr.toString());*/
            JSONObject job = jsonDataImport.getVData(databaseurl[0]);
            System.out.println("version: " + job.toString());
        try {
            animedb.updateVersion(job.getInt("version"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ArrayList<Anime> animeres = animedb.getAllAnimeByLetter("A");
        for(Anime anime : animeres){
            System.out.println(anime.toString());
        }

        return true;

    }

    protected void onPostExecute(final Boolean success){
        //if success can be used
        dialog.dismiss();
    }
}
