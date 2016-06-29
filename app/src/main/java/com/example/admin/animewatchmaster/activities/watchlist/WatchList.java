package com.example.admin.animewatchmaster.activities.watchlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.model.WatchListModel;
import com.example.admin.animewatchmaster.utils.Asynctasks.WatchlistUpdater;
import com.example.admin.animewatchmaster.utils.databaseUtils.DBHelper;
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.SlideInEffect;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by abraham on 10/6/2016.
 */
public class WatchList extends AppCompatActivity {

    private JazzyListView listView;
    private static Thread updateThread;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_watchlist);


        List<WatchListModel> modelList = DBHelper.getInstance(getApplicationContext()).getWatchlistData();
        listView = (JazzyListView)findViewById(R.id.watchlist);
        listView.setTransitionEffect(new SlideInEffect());

        WatchListAdapter watchListAdapter = new WatchListAdapter(getApplicationContext(),modelList);
        listView.setAdapter(watchListAdapter);

    }



    public void updateWatchlist(View v){

        if(updateThread == null || !updateThread.isAlive()) {

            final Button btn = (Button) findViewById(R.id.ButtonUpdate);
            btn.setEnabled(false);

            updateThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    Log.d("updateWatchlist", "Starting watchlist update");
                    try {
                        new WatchlistUpdater(getApplicationContext()).execute("").get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<WatchListModel> modelList = DBHelper.getInstance(getApplicationContext()).getWatchlistData();

                            WatchListAdapter watchListAdapter = new WatchListAdapter(getApplicationContext(), modelList);
                            listView.setAdapter(watchListAdapter);

                            btn.setEnabled(true);
                        }
                    });
                }
            });
            updateThread.start();

        }
    }


}
