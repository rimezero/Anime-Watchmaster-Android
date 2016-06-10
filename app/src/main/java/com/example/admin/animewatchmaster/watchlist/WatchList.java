package com.example.admin.animewatchmaster.watchlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.admin.animewatchmaster.Jsoup.WatchlistUpdater;
import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.databaseUtils.DBHelper;
import com.example.admin.animewatchmaster.model.WatchListModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by abraham on 10/6/2016.
 */
public class WatchList extends AppCompatActivity {

    private ListView listView;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_watchlist);


        List<WatchListModel> modelList = DBHelper.getInstance(getApplicationContext()).getWatchlistData();
        listView = (ListView)findViewById(R.id.watchlist);

        WatchListAdapter watchListAdapter = new WatchListAdapter(getApplicationContext(),modelList);
        listView.setAdapter(watchListAdapter);

    }

    public void updateWatchlist(View v){
        final Button btn = (Button) findViewById(R.id.ButtonUpdate);
        btn.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {

                Log.d("updateWatchlist","Starting watchlist update");
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

                        WatchListAdapter watchListAdapter = new WatchListAdapter(getApplicationContext(),modelList);
                        listView.setAdapter(watchListAdapter);

                        btn.setEnabled(true);
                    }
                });
            }
        }).start();
    }


}
