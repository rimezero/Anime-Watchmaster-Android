package com.example.admin.animewatchmaster.utils.Asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.utils.NetworkUtils;
import com.example.admin.animewatchmaster.utils.databaseUtils.DBHelper;
import com.example.admin.animewatchmaster.utils.databaseUtils.jsonDataImport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by admin on 7/1/2016.
 */
public class TopanimeUpdater extends AsyncTask<String,Void,Void> {
    public Context mainContext;

    public TopanimeUpdater(Context context){
        mainContext=context;
    }

    //ProgressDialog dialog;
    @Override
    protected void onPreExecute(){
        //dialog = ProgressDialog.show(mainContext,"Database Update","Updating database",true);
    }

    protected Void doInBackground(String... databaseurl) {
        if (!NetworkUtils.isInternetConnectionActive(mainContext.getSystemService(Context.CONNECTIVITY_SERVICE))) {
            Log.i("TopanimeUpdater -", " No internet connection or cannot connect to database server");
            return null;
        }

        DBHelper dbinstance = DBHelper.getInstance(mainContext);

        JSONArray jarr = jsonDataImport.getMALtopanimeData(mainContext.getString(R.string.base_db_url));

        int spot = 1;
        for(int i=0; i<jarr.length(); i++){
            try {
                JSONObject job = jarr.getJSONObject(i);
                int id = dbinstance.getAnimeID(job.getString("title"));
                if(id!=-1) {
                    if (dbinstance.checkIfSpotIsFilledInMALtopanime(spot)) {
                        dbinstance.updateMALtopanime(spot, id, job.getDouble("score"));
                    } else {
                        dbinstance.insertIntoMALtopanime(id, spot, job.getDouble("score"));
                    }
                    spot++;
                }else{
                    Log.i("TopanimeUpdater","Cannot find id for anime with title: "+job.getString("title"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("TopanimeUpdater","Json exception in loop");
            }

        }

        dbinstance.deleteMALtopanimeAfterSpot(spot);

        //testing on create
        /*
        ArrayList<TopanimeModel> anime = (ArrayList<TopanimeModel>) dbinstance.getTopAnimeData();
        for(TopanimeModel anim : anime){
            Log.d("in loop",anim.getTitle());
        }*/

        return null;
    }
}
