package com.example.admin.animewatchmaster.utils.Asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.admin.animewatchmaster.utils.Jsoup.JsoupToAnimefreak;
import com.example.admin.animewatchmaster.utils.NetworkUtils;
import com.example.admin.animewatchmaster.utils.databaseUtils.DBHelper;

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

        if(!NetworkUtils.isInternetConnectionActiveAnimeFreak(mainContext.getSystemService(Context.CONNECTIVITY_SERVICE))){
            Log.i("WatchlistUpdater -"," No internet connection or cannot connect to animefreak server");
            return null;
        }

        DBHelper dbinstance = DBHelper.getInstance(mainContext);

        ArrayList<Object> data = JsoupToAnimefreak.getWatchlistData(mainContext);

        ArrayList<Integer> ids = (ArrayList<Integer>)data.get(0);
        ArrayList<Integer> episodes = (ArrayList<Integer>)data.get(1);
        ArrayList<String> lastupdated = (ArrayList<String>)data.get(2);

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
