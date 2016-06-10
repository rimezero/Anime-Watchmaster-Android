package com.example.admin.animewatchmaster.watchlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.databaseUtils.DBHelper;
import com.example.admin.animewatchmaster.model.WatchListModel;

import java.util.List;

/**
 * Created by abraham on 10/6/2016.
 */
public class WatchList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_watchlist);


        List<WatchListModel> modelList = DBHelper.getInstance(getApplicationContext()).getWatchlistData();
        ListView listView = (ListView)findViewById(R.id.watchlist);

        WatchListAdapter watchListAdapter = new WatchListAdapter(getApplicationContext(),modelList);
        listView.setAdapter(watchListAdapter);

    }


}
