package com.peitch.animewatchmaster.activities.seasons;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.peitch.animewatchmaster.model.SeasonModel;
import com.peitch.animewatchmaster.model.SeasonsSortModel;
import com.peitch.animewatchmaster.utils.databaseUtils.DBHelper;

import java.util.List;

/**
 * Created by abraham on 3/8/2016.
 */
public class SeasonMainFragment extends MainFragment {

    private DBHelper dbHelper;
    private List<SeasonsSortModel> seasonsSortModel;
    private List<SeasonModel> seasonModelsList;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public void setUpViewPager(ViewPager viewPager) {

        dbHelper = DBHelper.getInstance(getApplicationContext());

        seasonsSortModel = dbHelper.getSeasons();

        if(seasonsSortModel != null && !seasonsSortModel.isEmpty()) {

            seasonModelsList = dbHelper.getSeasonData(true,seasonsSortModel.get(0).toString());

            if(seasonModelsList.size() == 0) {
                seasonModelsList = dbHelper.getSeasonData(true,seasonsSortModel.get(1).toString());
            }


            if(seasonModelsList != null && !seasonModelsList.isEmpty()) {

                ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
                for(int i =0; i < seasonsSortModel.size(); i++) {

                    SeasonFragment seasonFragment = new SeasonFragment();
                    seasonFragment.setSeason(seasonsSortModel.get(i).toString());
                    adapter.addFragment(seasonFragment,seasonsSortModel.get(i).toString());
                }

                viewPager.setAdapter(adapter);
                viewPager.setCurrentItem(0);

            } else {
                finish();
            }

        } else {
            finish();
        }


    }
}
