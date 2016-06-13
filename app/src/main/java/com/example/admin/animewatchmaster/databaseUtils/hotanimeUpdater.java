package com.example.admin.animewatchmaster.databaseUtils;

//import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.admin.animewatchmaster.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 6/11/2016.
 */
public class hotanimeUpdater extends AsyncTask<String,Void,Void> {
    public Context mainContext;

    public hotanimeUpdater(Context context){
        mainContext=context;
    }

    //ProgressDialog dialog;
    @Override
    protected void onPreExecute(){
        //dialog = ProgressDialog.show(mainContext,"Database Update","Updating database",true);
    }

    protected Void doInBackground(String... databaseurl) {

        if (!NetworkUtils.isInternetConnectionActive(mainContext.getSystemService(Context.CONNECTIVITY_SERVICE))) {
            Log.i("hotanimeUpdater -", " No internet connection or cannot connect to animefreak server");
            return null;
        }

        DBHelper dbinstance = DBHelper.getInstance(mainContext);
        JSONArray hotanimedata = jsonDataImport.getHotanimeData(databaseurl[0]);
        //JSONObject versionjob = jsonDataImport.getVData(databaseurl[0]);
        List<String> titlelist = new ArrayList<>();

        for(int i=0; i<hotanimedata.length(); i++){
            try {
                JSONObject hotanime = (JSONObject) hotanimedata.get(i);
                String title = hotanime.getString("title");

                //temporal fix gia 2 anime theloume prolog giati einai pithikoi xd
                if(title.equals("Netoge no Yome wa Onnanoko ja Nai to Omotta?"))
                    title = "Netoge no Yome wa Onnanoko ja Nai to Omotta";
                if(title.equals("Kiznaiver"))
                    title = "Kiznavier";

                titlelist.add(title);
            } catch (JSONException e) {
                Log.e("hotanimeUpdater"," Unable to cast imported data to json object");
                e.printStackTrace();
            }
        }

        //Log.d("hotanimeUpdater","imported data: "+titlelist.toString());
        DBHelper.getInstance(mainContext).handleNewHotanimeUpdate(titlelist);

        /*
        List<WatchlaterlistModel> data = DBHelper.getInstance(mainContext).getHotAnimeData();
        Log.d("hotanimeUpdater","data starting:");
        for(WatchlaterlistModel model : data){
            Log.d("hotanimeUpdater",model.getTitle());
        }*/

        return null;
    }
}
