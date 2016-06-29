package com.example.admin.animewatchmaster.activities.watched;

/**
 * Created by admin on 6/29/2016.
 */
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.model.WatchedModel;
import com.example.admin.animewatchmaster.utils.databaseUtils.DBHelper;
import com.example.admin.animewatchmaster.model.WatchlaterlistModel;

import java.util.List;

public class WatchedAnime extends AppCompatActivity {


    private ListView listView;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_watchedlist);

        List<WatchedModel> modelList = DBHelper.getInstance(getApplicationContext()).getWatchedListData();
        listView = (ListView)findViewById(R.id.watchedlist);

        WatchedAdapter watchListAdapter = new WatchedAdapter(getApplicationContext(),modelList);
        listView.setAdapter(watchListAdapter);

    }


}
