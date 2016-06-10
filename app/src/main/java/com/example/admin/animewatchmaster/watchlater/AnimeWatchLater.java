package com.example.admin.animewatchmaster.watchlater;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.databaseUtils.DBHelper;
import com.example.admin.animewatchmaster.model.WatchlaterlistModel;

import java.util.List;

/**
 * Created by abraham on 10/6/2016.
 */
public class AnimeWatchLater extends AppCompatActivity {


    private ListView listView;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_watchlaterlist);

        List<WatchlaterlistModel> modelList = DBHelper.getInstance(getApplicationContext()).getWatchlaterlistData();
        listView = (ListView)findViewById(R.id.watchlaterlist);

        WatchLaterAdapter watchListAdapter = new WatchLaterAdapter(getApplicationContext(),modelList);
        listView.setAdapter(watchListAdapter);

    }


}
