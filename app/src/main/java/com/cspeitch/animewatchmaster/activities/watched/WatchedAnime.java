package com.cspeitch.animewatchmaster.activities.watched;

/**
 * Created by admin on 6/29/2016.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cspeitch.animewatchmaster.R;
import com.cspeitch.animewatchmaster.model.WatchedModel;
import com.cspeitch.animewatchmaster.utils.databaseUtils.DBHelper;
import com.facebook.appevents.AppEventsLogger;
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.SlideInEffect;

import java.util.List;

public class WatchedAnime extends AppCompatActivity {


    private JazzyListView listView;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_watchedlist);
        List<WatchedModel> modelList = DBHelper.getInstance(getApplicationContext()).getWatchedListData();
        listView = (JazzyListView)findViewById(R.id.watchedlist);
        listView.setTransitionEffect(new SlideInEffect());

        WatchedAdapter watchListAdapter = new WatchedAdapter(getApplicationContext(),modelList);
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
