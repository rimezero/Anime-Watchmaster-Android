package com.evolsoft.animewatchmaster.activities.watchlater;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.evolsoft.animewatchmaster.R;
import com.evolsoft.animewatchmaster.model.WatchlaterlistModel;
import com.evolsoft.animewatchmaster.utils.databaseUtils.DBHelper;
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


}
