package com.evolsoft.animewatchmaster.activities.watched;

/**
 * Created by admin on 6/29/2016.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.evolsoft.animewatchmaster.R;
import com.evolsoft.animewatchmaster.model.WatchedModel;
import com.evolsoft.animewatchmaster.utils.databaseUtils.DBHelper;
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


}
