package com.peitch.animewatchmaster.activities.watchlater;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.peitch.animewatchmaster.R;
import com.peitch.animewatchmaster.model.WatchlaterlistModel;
import com.peitch.animewatchmaster.utils.databaseUtils.DBHelper;
import com.facebook.appevents.AppEventsLogger;
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.SlideInEffect;

import java.util.List;

/**
 * Created by abraham on 10/6/2016.
 */
public class AnimeWatchLater extends AppCompatActivity {


    private JazzyListView listView;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_watchlaterlist);

        List<WatchlaterlistModel> modelList = DBHelper.getInstance(getApplicationContext()).getWatchlaterlistData();
        listView = (JazzyListView)findViewById(R.id.watchlaterlist);
        listView.setTransitionEffect(new SlideInEffect());

        WatchLaterAdapter watchListAdapter = new WatchLaterAdapter(getApplicationContext(),modelList);
        listView.setAdapter(watchListAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }


}
