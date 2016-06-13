package com.example.admin.animewatchmaster.utils.Asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.admin.animewatchmaster.utils.NetworkUtils;
import com.example.admin.animewatchmaster.utils.databaseUtils.DBHelper;
import com.example.admin.animewatchmaster.utils.databaseUtils.jsonDataImport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 4/12/2016.
 */
//json import
public class databaseUpdater extends AsyncTask<String,Void,Void> {
    public Context mainContext;

    public databaseUpdater(Context context){
        mainContext=context;
    }


    ProgressDialog dialog;
    @Override
    protected void onPreExecute(){
        dialog = ProgressDialog.show(mainContext,"Database Update","Updating database",true);
    }



    @Override
    protected Void doInBackground(String... databaseurl) {

        if(!NetworkUtils.isInternetConnectionActive(mainContext.getSystemService(Context.CONNECTIVITY_SERVICE))){
            Log.i("databaseUpdater -"," No internet connection or cannot connect to animefreak server");
            return null;
        }

        DBHelper dbinstance = DBHelper.getInstance(mainContext);
        JSONObject versionjob = jsonDataImport.getVData(databaseurl[0]);
        if(versionjob != null) {

            int localversion = dbinstance.getVersion();
            int onlineversion = -1;
            try {
                onlineversion = versionjob.getInt("version");
            } catch (JSONException e) {
                Log.e("dbupdater - background", "Cannot read version from json object");
                e.printStackTrace();
            }
            Log.d("dbupdater - background", "local version: " + localversion + " online version: " + onlineversion);
            JSONArray jarr = new JSONArray();

            if (onlineversion > localversion) {

                jarr = jsonDataImport.getAnimeinfoDataIncludingLinksByVersion(databaseurl[0], localversion);

                if(jarr != null && jarr.length() > 0) {

                    JSONObject job = null;

                    for (int i = 0; i < jarr.length(); i++) {
                        try {
                            job = (JSONObject) jarr.get(i);
                            int id;
                            if (!dbinstance.checkIfExistsInAnimeInfo(job.getString("title"))) {
                    /*
                    System.out.println(job.getInt("id"));
                    System.out.println(job.getString("title"));
                    System.out.println(job.getString("imgurl"));
                    System.out.println(job.getString("genre"));
                    System.out.println(job.getString("episodes"));
                    System.out.println(job.getString("agerating"));
                    System.out.println(job.getString("animetype"));
                    System.out.println(job.getString("description"));*/
                                boolean s = dbinstance.insertIntoAnimeinfo(job.getString("title"), job.getString("imgurl"), job.getString("genre"), job.getString("episodes"), job.getString("animetype"), job.getString("agerating"), job.getString("description"));
                                id = dbinstance.getAnimeID(job.getString("title"));
                                s = dbinstance.insertIntoAnimelinks(id,job.getString("frlink"),job.getString("ultimalink"));
                                //System.out.println(s);
                            } else {
                                id = dbinstance.getAnimeID(job.getString("title"));
                                boolean s = dbinstance.updateAnimeinfo(id, job.getString("title"), job.getString("imgurl"), job.getString("genre"), job.getString("episodes"), job.getString("animetype"), job.getString("agerating"), job.getString("description"));
                                if(dbinstance.checkIfExistsInAnimelinks(id)) {
                                    s = dbinstance.updateAnimelinks(id, job.getString("frlink"), job.getString("ultimalink"));
                                }else{
                                    s = dbinstance.insertIntoAnimelinks(id,job.getString("frlink"),job.getString("ultimalink"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                    dbinstance.updateVersion(onlineversion);


                }



            } else {
                Log.i("dbupdater - background", "Up to date update not needed");

            }

        }

        dialog.dismiss();
        return null;

    }


}
