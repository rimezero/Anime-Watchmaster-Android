package com.example.admin.animewatchmaster.activities.topanime;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.model.TopanimeModel;
import com.example.admin.animewatchmaster.utils.databaseUtils.DBHelper;
import com.twotoasters.jazzylistview.JazzyGridView;
import com.twotoasters.jazzylistview.effects.SlideInEffect;

import java.util.List;

/**
 * Created by abraham on 2/7/2016.
 */
public class TopAnimeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_top_anime);

        JazzyGridView jazzyGridView = (JazzyGridView)findViewById(R.id.gridview);
        List<TopanimeModel> topanimeModelList = DBHelper.getInstance(getApplicationContext()).getTopAnimeData();
        TopAnimeAdapter topAnimeAdapter = new TopAnimeAdapter(getApplicationContext(),topanimeModelList);
        jazzyGridView.setTransitionEffect(new SlideInEffect());
        jazzyGridView.setAdapter(topAnimeAdapter);



    }


}
