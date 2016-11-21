package com.peitch.animewatchmaster.utils.Asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.peitch.animewatchmaster.utils.NetworkUtils;
import com.peitch.animewatchmaster.utils.databaseUtils.DBHelper;
import com.peitch.animewatchmaster.utils.databaseUtils.jsonDataImport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 11/22/2016.
 */
public class UPCdeleter extends AsyncTask<String,Void,Void> {
    public Context mainContext;

    public UPCdeleter(Context context){
        mainContext=context;
    }


    //ProgressDialog dialog;
    @Override
    protected void onPreExecute(){
        //dialog = ProgressDialog.show(mainContext,"Database Update","Updating database",true);
    }



    @Override
    protected Void doInBackground(String... databaseurl) {

        if(!NetworkUtils.isInternetConnectionActive(mainContext.getSystemService(Context.CONNECTIVITY_SERVICE))){
            Log.i("upcdeleter"," No internet connection or cannot connect to database server");
            return null;
        }

        DBHelper dbinstance = DBHelper.getInstance(mainContext);
        JSONObject versionjob = jsonDataImport.getUPCVData(databaseurl[0]);
        if(versionjob != null) {

            int localversion = dbinstance.getUPCVersion();
            int onlineversion = -1;
            try {
                onlineversion = versionjob.getInt("UPCversion");
            } catch (JSONException e) {
                Log.e("upcdeleter - background", "Cannot read version from json object");
                e.printStackTrace();
            }
            Log.d("upcdeleter - background", "local version: " + localversion + " online version: " + onlineversion);
            JSONArray jarr = new JSONArray();

            if (onlineversion > localversion) {

                jarr = jsonDataImport.getUPCtodelete(databaseurl[0], localversion);

                if(jarr != null && jarr.length() > 0) {

                    JSONObject job = null;
                    try {
                        int lastVersion = ((JSONObject)jarr.get(0)).getInt("version");
                        int currentVersion = 0;


                        for (int i = 0; i < jarr.length(); i++) {

                            job = (JSONObject) jarr.get(i);
                            int id;

                            currentVersion=job.getInt("version");
                            if(currentVersion>lastVersion) {
                                dbinstance.updateUPCVersion(lastVersion);
                                lastVersion=currentVersion;
                            }

                            id = dbinstance.getAPAnimeID(job.getString("title"));
                            boolean s = dbinstance.deleteAPAnime(id);
                        }

                        dbinstance.updateUPCVersion(onlineversion);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }



            } else {
                Log.i("upcdeleter - background", "Up to date update not needed");
            }

        }

        //dialog.dismiss();
        return null;

    }
}
