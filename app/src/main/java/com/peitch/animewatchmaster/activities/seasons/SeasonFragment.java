package com.peitch.animewatchmaster.activities.seasons;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.peitch.animewatchmaster.R;
import com.peitch.animewatchmaster.model.SeasonModel;
import com.peitch.animewatchmaster.utils.databaseUtils.DBHelper;
import com.twotoasters.jazzylistview.JazzyGridView;
import com.twotoasters.jazzylistview.effects.SlideInEffect;

import java.util.List;

/**
 * Created by abraham on 3/8/2016.
 */
public class SeasonFragment extends Fragment {

    private String season;

    public SeasonFragment() {
        //needs default const init
    }

    public void setSeason(String s) {
        season = s;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,Bundle bundle) {
        View view = inflater.inflate(R.layout.layout_season_fragment,viewGroup,false);

        DBHelper dbHelper = DBHelper.getInstance(getContext());
        List<SeasonModel> models = dbHelper.getSeasonData(true,season);

        JazzyGridView gridView = (JazzyGridView)view.findViewById(R.id.listview);
        gridView.setTransitionEffect(new SlideInEffect());
        loadGridView(models,-1,gridView);

        return view;
    }


    private void loadGridView(List<SeasonModel> animeList, int colsNum,GridView gridView){

        if(colsNum == 1) {
            gridView.setNumColumns(1);
        } else if(colsNum == 2) {
            gridView.setNumColumns(2);
        } else if(colsNum == 3) {
            gridView.setNumColumns(3);
        }

        SeasonDataAdapter topAnimeAdapter = new SeasonDataAdapter(getContext(), animeList);
        gridView.setAdapter(topAnimeAdapter);

    }

}
