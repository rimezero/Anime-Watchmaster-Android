package com.example.admin.animewatchmaster.activities.upcoming;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.model.Anime;
import com.twotoasters.jazzylistview.JazzyGridView;
import com.twotoasters.jazzylistview.effects.SlideInEffect;

import java.util.ArrayList;
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

        List<Anime> models = new ArrayList<>();
        UpcomingAdapter upcomingAdapter = new UpcomingAdapter(getApplicationContext(),models);
        gridView.setAdapter(upcomingAdapter);
        
    }


}
