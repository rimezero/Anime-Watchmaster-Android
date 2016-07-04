package com.example.admin.animewatchmaster.activities.upcoming;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.model.SeasonModel;
import com.example.admin.animewatchmaster.model.SeasonsSortModel;
import com.example.admin.animewatchmaster.utils.databaseUtils.DBHelper;
import com.twotoasters.jazzylistview.JazzyGridView;
import com.twotoasters.jazzylistview.effects.SlideInEffect;

import java.util.List;

/**
 * Created by abraham on 3/7/2016.
 */
public class UpcomingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_upcoming);

        JazzyGridView gridView = (JazzyGridView)findViewById(R.id.gridview);
        gridView.setTransitionEffect(new SlideInEffect());

        DBHelper dbHelper = DBHelper.getInstance(getApplicationContext());

        List<SeasonModel> seasonModels = dbHelper.getSeasonData(false,"Upcoming");

        UpcomingAdapter upcomingAdapter = new UpcomingAdapter(getApplicationContext(),seasonModels);
        gridView.setAdapter(upcomingAdapter);
        
    }


}
