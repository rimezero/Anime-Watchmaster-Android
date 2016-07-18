package com.cspeitch.animewatchmaster.utils.Asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cspeitch.animewatchmaster.utils.NetworkUtils;
import com.cspeitch.animewatchmaster.utils.databaseUtils.DBHelper;
import com.cspeitch.animewatchmaster.utils.databaseUtils.jsonDataImport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 7/3/2016.
 */
public class APdatabaseUpdater extends AsyncTask<String,Void,String> {
    public Context mainContext;

    private APdatabaseUpdaterListener listener;
    private Exception exception;

    public APdatabaseUpdater(Context context){
        mainContext=context;
    }

    //ProgressDialog dialog;
    @Override
    protected void onPreExecute(){
        //dialog = ProgressDialog.show(mainContext,"Database Update","Updating database",true);
    }

    protected String doInBackground(String... databaseurl) {
        if (!NetworkUtils.isInternetConnectionActive(mainContext.getSystemService(Context.CONNECTIVITY_SERVICE))) {
            Log.i("APdatabaseUpdater", " No internet connection or cannot connect to database server");
            return "1";
        }

        DBHelper dbinstance = DBHelper.getInstance(mainContext);
        JSONObject versionjob = jsonDataImport.getVData(databaseurl[0]);
        if(versionjob != null) {

            int localversion = dbinstance.getAPVersion();
            int onlineversion = -1;
            try {
                onlineversion = versionjob.getInt("version");
            } catch (JSONException e) {
                Log.e("APdbupdater - bkground", "Cannot read version from json object");
                e.printStackTrace();
            }
            Log.d("APdbupdater - bkground", "local version: " + localversion + " online version: " + onlineversion);
            JSONArray jarr = new JSONArray();

            if (onlineversion > localversion) {
                jarr = jsonDataImport.getAPAnimeinfoData(databaseurl[0], localversion);

                if(jarr != null && jarr.length() > 1) {
                    JSONObject job = null;
                    try {

                        job = (JSONObject) jarr.get(0);
                        String currentSeason = job.getString("season");
                        dbinstance.updateCurrentSeason(currentSeason);

                        int lastVersion = ((JSONObject) jarr.get(1)).getInt("version");
                        int currentVersion = 0;

                        for (int i = 1; i < jarr.length(); i++) {

                            job = (JSONObject) jarr.get(i);
                            int id;

                            currentVersion=job.getInt("version");
                            if(currentVersion>lastVersion) {
                                dbinstance.updateAPVersion(lastVersion);
                                lastVersion=currentVersion;
                            }

                            int animeinfo_id = dbinstance.getAnimeID(job.getString("frtitle"));
                            if (!dbinstance.checkIfExistsInAPAnimeInfo(job.getString("title"))) {
                                boolean s = dbinstance.insertIntoAPAnimeinfo(animeinfo_id,job.getString("title"),job.getString("season"),job.getString("imgurl"),job.getString("genre"),job.getString("animetype"),job.getString("description"),job.getDouble("rating"));
                            }else{
                                boolean s = dbinstance.updateAPAnimeinfo(dbinstance.getAPAnimeID(job.getString("title")),animeinfo_id,job.getString("title"),job.getString("season"),job.getString("imgurl"),job.getString("genre"),job.getString("animetype"),job.getString("description"),job.getDouble("rating"));
                            }
                        }

                        dbinstance.updateAPVersion(onlineversion);
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            } else {
                Log.i("APdbupdater - backgrnd", "Up to date update not needed");
            }

        }


        return "1";
    }



    public static interface APdatabaseUpdaterListener {
        public void onComplete(String res,Exception ex);
    }


    @Override
    protected void onPostExecute(String result) {
        if(this.listener != null) {
            this.listener.onComplete(result,exception);
        }
    }

    @Override
    protected void onCancelled() {
        if (this.listener != null) {
            exception = new InterruptedException("AsyncTask cancelled");
            this.listener.onComplete(null, exception);
        }
    }

    public static interface hotanimeupdaterInterface {
        public void onComplete(String result,Exception ex);
    }


    public APdatabaseUpdater setListener(APdatabaseUpdaterListener listener) {
        this.listener = listener;
        return this;
    }

}
