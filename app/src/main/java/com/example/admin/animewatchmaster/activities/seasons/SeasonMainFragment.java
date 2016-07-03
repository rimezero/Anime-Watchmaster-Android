package com.example.admin.animewatchmaster.activities.seasons;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

/**
 * Created by abraham on 3/7/2016.
 */
public class SeasonMainFragment extends MainFragment {


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        getSupportActionBar().setTitle("seasons");
    }


    @Override
    public void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        //Framgent framgent = new Framgent();

       // adapter.addFragment(framgent, "fragment");

        //Framgent2 framgent2 = new Framgent2();

        //adapter.addFragment(framgent2, "fragment2");
        viewPager.setAdapter(adapter);

    }

}
