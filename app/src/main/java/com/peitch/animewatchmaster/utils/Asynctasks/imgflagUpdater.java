package com.peitch.animewatchmaster.utils.Asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.peitch.animewatchmaster.utils.NetworkUtils;
import com.peitch.animewatchmaster.utils.Utils;

/**
 * Created by admin on 8/2/2016.
 */
public class imgflagUpdater extends AsyncTask<String,Void,Void> {
    public Context mainContext;

    public imgflagUpdater(Context context){
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
            Log.i("imgflagUpdater"," No internet connection or cannot connect to database server");
            return null;
        }

        Utils.initImgflag(mainContext);

        //dialog.dismiss();
        return null;

    }

}
