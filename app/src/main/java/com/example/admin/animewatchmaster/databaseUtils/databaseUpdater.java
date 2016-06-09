package com.example.admin.animewatchmaster.databaseUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

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


        JSONObject versionjob = jsonDataImport.getVData(databaseurl[0]);
        if(versionjob != null) {

            Cursor csvs = DBHelper.getInstance(mainContext).getVersion();
            csvs.moveToFirst();
            int localversion = csvs.getInt(csvs.getColumnIndex("version"));
            csvs.close();
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
                            if (!DBHelper.getInstance(mainContext).checkIfExistsInAnimeInfo(job.getString("title"))) {
                    /*
                    System.out.println(job.getInt("id"));
                    System.out.println(job.getString("title"));
                    System.out.println(job.getString("imgurl"));
                    System.out.println(job.getString("genre"));
                    System.out.println(job.getString("episodes"));
                    System.out.println(job.getString("agerating"));
                    System.out.println(job.getString("animetype"));
                    System.out.println(job.getString("description"));*/
                                boolean s = DBHelper.getInstance(mainContext).insertIntoAnimeinfo(mainContext, job.getString("title"), job.getString("imgurl"), job.getString("genre"), job.getString("episodes"), job.getString("animetype"), job.getString("agerating"), job.getString("description"));
                                id = DBHelper.getInstance(mainContext).getAnimeID(job.getString("title"));
                                s = DBHelper.getInstance(mainContext).insertIntoAnimelinks(mainContext,id,job.getString("frlink"),job.getString("ultimalink"));
                                //System.out.println(s);
                            } else {
                                id = DBHelper.getInstance(mainContext).getAnimeID(job.getString("title"));
                                boolean s = DBHelper.getInstance(mainContext).updateAnimeinfo(id, job.getString("title"), job.getString("imgurl"), job.getString("genre"), job.getString("episodes"), job.getString("animetype"), job.getString("agerating"), job.getString("description"));
                                if(DBHelper.getInstance(mainContext).checkIfExistsInAnimelinks(id)) {
                                    s = DBHelper.getInstance(mainContext).updateAnimelinks(id, job.getString("frlink"), job.getString("ultimalink"));
                                }else{
                                    s = DBHelper.getInstance(mainContext).insertIntoAnimelinks(mainContext,id,job.getString("frlink"),job.getString("ultimalink"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                    DBHelper.getInstance(mainContext).updateVersion(onlineversion);


                }



            } else {
                Log.i("dbupdater - background", "Up to date update not needed");

            }

        }

        dialog.dismiss();
        return null;

    }


}
