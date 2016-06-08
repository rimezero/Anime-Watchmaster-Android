package com.example.admin.animewatchmaster.animebyletter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.model.Anime;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by abraham on 9/6/2016.
 */
public class AnimeLetterAdapter extends ArrayAdapter<Anime> {

    public AnimeLetterAdapter(Context context, List<Anime> models) {
        super(context,0,models);
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {

        Anime model = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_anime_by_letter_row,parent,false);
        }

        TextView text = (TextView)convertView.findViewById(R.id.title);
        text.setText(model.getTitle());

        ImageView imageView = (ImageView)convertView.findViewById(R.id.image);

        Picasso.with(getContext())
                .load(model.getImgurl())
                .into(imageView);

        TextView textView = (TextView)convertView.findViewById(R.id.desc);
        textView.setText(model.getDescription());


        return convertView;

    }


}
