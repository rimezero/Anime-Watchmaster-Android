package com.cspeitch.animewatchmaster.utils.Asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cspeitch.animewatchmaster.R;
import com.cspeitch.animewatchmaster.utils.NetworkUtils;
import com.cspeitch.animewatchmaster.utils.databaseUtils.DBHelper;
import com.cspeitch.animewatchmaster.utils.databaseUtils.jsonDataImport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 6/10/2016.
 */
public class WatchlistUpdater extends AsyncTask<String,Void,Void> {

    public Context mainContext;

    public WatchlistUpdater(Context context){
        mainContext=context;
    }


    //ProgressDialog dialog;
    @Override
    protected void onPreExecute(){
        //dialog = ProgressDialog.show(mainContext,"Database Update","Updating database",true);
        Log.d("WatchlistUpdater -"," Starting watchlist update");
    }

    @Override
    protected Void doInBackground(String... databaseurl) {

        if(!NetworkUtils.isInternetConnectionActive(mainContext.getSystemService(Context.CONNECTIVITY_SERVICE))){
            Log.i("WatchlistUpdater -"," No internet connection or cannot connect to database server");
            return null;
        }

        DBHelper dbinstance = DBHelper.getInstance(mainContext);

        JSONArray jarr = jsonDataImport.getWatchlistData(mainContext.getString(R.string.base_db_url));

        if(jarr.length()==0){
            Log.i("WatchlistUpdater","Empty array");
            return null;
        }

        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<Integer> episodes = new ArrayList<>();
        ArrayList<String> lastupdated = new ArrayList<>();

        for(int i=0; i<jarr.length(); i++){
            try {
                JSONObject job = jarr.getJSONObject(i);
                int id = dbinstance.getAnimeID(job.getString("title"));
                if(id!=-1){
                    if(dbinstance.checkIfExistsInWatchlist(id)){
                        ids.add(id);
                        episodes.add(job.getInt("currentepisode"));
                        lastupdated.add(job.getString("lastupdated"));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("WatchlistUpdater"," JSON exception trying to parse array index to json object");
            }
        }

        //telos jsoup arxi SQLite
        for(int i=0; i<ids.size(); i++){
            dbinstance.updateWatchlistAnime(ids.get(i),episodes.get(i),lastupdated.get(i));
        }

        dbinstance.handleWatchlistRemainingUpdate(ids);

        /* DEBUGGING ON CREATE
        List<WatchListModel> list = dbinstance.getWatchlistData();
        for(WatchListModel anime : list){
            Log.d("asynctask debug", String.valueOf(anime.getCurrentEpisode()));
        }*/

        return null;
    }
}
