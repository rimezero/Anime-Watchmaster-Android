package com.cspeitch.animewatchmaster.activities.seasons;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cspeitch.animewatchmaster.R;
import com.cspeitch.animewatchmaster.model.SeasonsSortModel;

import java.util.List;

/**
 * Created by abraham on 4/7/2016.
 */
public class SeasonYearAdapter extends ArrayAdapter<SeasonsSortModel> {


    public SeasonYearAdapter(Context context,List<SeasonsSortModel> models) {
        super(context,0,models);
    }



    @Override
    public View getView(int position,View convertView,ViewGroup parent) {

        final SeasonsSortModel model = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_season_hor_row, parent, false);
        }


        TextView textViewseason = (TextView)convertView.findViewById(R.id.season);
        textViewseason.setText(model.toString());


        return convertView;
    }



}
