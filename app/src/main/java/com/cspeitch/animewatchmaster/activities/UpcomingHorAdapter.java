package com.cspeitch.animewatchmaster.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.cspeitch.animewatchmaster.R;
import com.cspeitch.animewatchmaster.model.SeasonModel;
import com.cspeitch.animewatchmaster.model.UpcomingAnime;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.grantland.widget.AutofitTextView;

/**
 * Created by abraham on 6/7/2016.
 */
public class UpcomingHorAdapter extends ArrayAdapter<SeasonModel> {


    public UpcomingHorAdapter(Context context,List<SeasonModel> models) {
        super(context,0,models);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SeasonModel model = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_anime_hot_row,parent,false);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.image);

        try {

            if(model.getImgurl() != null && !model.getImgurl().trim().isEmpty() && !model.getImgurl().equals("na")) {
                Picasso.with(getContext())
                        .load(model.getImgurl())
                        .fit()
                        .into(imageView);
            }else {
                Picasso.with(getContext())
                        .load("http://www.anime-planet.com/inc/img/blank_main.jpg")
                        .fit()
                        .into(imageView);
            }

        }catch (Exception ex) {

        }

        AutofitTextView textView = (AutofitTextView)convertView.findViewById(R.id.title);
        textView.setText(model.getTitle());

        return convertView;
    }


}
