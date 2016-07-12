package com.example.admin.animewatchmaster.utils.Asynctasks;

//import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.admin.animewatchmaster.utils.NetworkUtils;
import com.example.admin.animewatchmaster.utils.databaseUtils.DBHelper;
import com.example.admin.animewatchmaster.utils.databaseUtils.jsonDataImport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 6/11/2016.
 */
public class hotanimeUpdater extends AsyncTask<String,Void,String> {

    private Context mainContext;

    private hotanimeupdaterInterface mListener = null;
    private Exception exception = null;


    public hotanimeUpdater(Context context){
        mainContext=context;
    }

    //ProgressDialog dialog;
    @Override
    protected void onPreExecute(){
        //dialog = ProgressDialog.show(mainContext,"Database Update","Updating database",true);
    }

    protected String doInBackground(String... databaseurl) {

        if (!NetworkUtils.isInternetConnectionActive(mainContext.getSystemService(Context.CONNECTIVITY_SERVICE))) {
            Log.i("hotanimeUpdater -", " No internet connection or cannot connect to database server");
            return null;
        }

        DBHelper dbinstance = DBHelper.getInstance(mainContext);
        JSONArray hotanimedata = jsonDataImport.getHotanimeData(databaseurl[0]);

        if(hotanimedata.length()==0){
            Log.i("hotanimeUpdater", "Empty array");
            return null;
        }
        //JSONObject versionjob = jsonDataImport.getVData(databaseurl[0]);
        List<String> titlelist = new ArrayList<>();

        for(int i=0; i<hotanimedata.length(); i++){
            try {
                JSONObject hotanime = (JSONObject) hotanimedata.get(i);
                String title = hotanime.getString("title");
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
        return ""+titlelist.isEmpty();
    }

    @Override
    protected void onPostExecute(String result) {
        if(this.mListener != null) {
            this.mListener.onComplete(result,exception);
        }
    }

    @Override
    protected void onCancelled() {
        if (this.mListener != null) {
            exception = new InterruptedException("AsyncTask cancelled");
            this.mListener.onComplete(null, exception);
        }
    }

    public static interface hotanimeupdaterInterface {
        public void onComplete(String result,Exception ex);
    }


    public hotanimeUpdater setListener(hotanimeupdaterInterface listener) {
        this.mListener = listener;
        return this;
    }


}
