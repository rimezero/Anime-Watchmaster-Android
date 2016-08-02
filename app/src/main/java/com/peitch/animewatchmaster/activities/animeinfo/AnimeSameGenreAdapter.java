package com.peitch.animewatchmaster.activities.animeinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.peitch.animewatchmaster.R;
import com.peitch.animewatchmaster.model.Anime;
import com.peitch.animewatchmaster.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.grantland.widget.AutofitTextView;

/**
 * Created by abraham on 3/7/2016.
 */
public class AnimeSameGenreAdapter extends ArrayAdapter<Anime> {


    public AnimeSameGenreAdapter(Context context,List<Anime> models) {
        super(context,0,models);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Anime model = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_same_genre_anime_row,parent,false);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.image);

        if(model.getImgurl() != null && !model.getImgurl().trim().isEmpty() && Utils.imgflag) {
            Picasso.with(getContext())
                    .load(model.getImgurl())
                    .fit()
                    .into(imageView);
        }  else {
            Picasso.with(getContext())
                    .load("http://www.anime-planet.com/inc/img/blank_main.jpg")
                    .fit()
                    .into(imageView);
        }

        AutofitTextView textView = (AutofitTextView)convertView.findViewById(R.id.title);
        textView.setText(model.getTitle());

        return convertView;
    }



}
