package com.example.admin.animewatchmaster.activities.seasons;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.model.SeasonModel;
import com.example.admin.animewatchmaster.model.SeasonsSortModel;
import com.example.admin.animewatchmaster.utils.databaseUtils.DBHelper;

import org.lucasr.twowayview.TwoWayView;

import java.util.List;

/**
 * Created by abraham on 4/7/2016.
 */
public class SeasonsMainActivity extends AppCompatActivity {

    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle bundle) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(bundle);
        setContentView(R.layout.layout_season_vert);

        dbHelper = DBHelper.getInstance(getApplicationContext());

        TwoWayView toplist = (TwoWayView)findViewById(R.id.horlist);
        List<SeasonsSortModel> seasonsSortModel = dbHelper.getSeasons();
        SeasonYearAdapter seasonYearAdapter = new SeasonYearAdapter(getApplicationContext(),seasonsSortModel);
        toplist.setAdapter(seasonYearAdapter);


        GridView gridView = (GridView)findViewById(R.id.listview);
        List<SeasonModel> seasonModel = dbHelper.getSeasonData(true,seasonsSortModel.get(1).toString());

        for(SeasonModel s : seasonModel) {
            System.out.println(s.getTitle());
        }

        SeasonDataAdapter seasonDataAdapter = new SeasonDataAdapter(getApplicationContext(),seasonModel);
        gridView.setAdapter(seasonDataAdapter);

    }




}
