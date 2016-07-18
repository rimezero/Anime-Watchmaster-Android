package com.cspeitch.animewatchmaster.activities.hotanime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.cspeitch.animewatchmaster.R;
import com.cspeitch.animewatchmaster.model.WatchlaterlistModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.grantland.widget.AutofitTextView;

/**
 * Created by abraham on 12/6/2016.
 */
public class AnimeHotAdapter extends ArrayAdapter<WatchlaterlistModel> {


    public AnimeHotAdapter(Context context,List<WatchlaterlistModel> models) {
        super(context,0,models);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final WatchlaterlistModel model = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_anime_hot_row,parent,false);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.image);

        try {

            Picasso.with(getContext())
                    .load(model.getImgurl())
                    .fit()
                    .into(imageView);

        }catch (Exception ex) {

        }

        AutofitTextView textView = (AutofitTextView)convertView.findViewById(R.id.title);
        textView.setText(model.getTitle());

        return convertView;
    }


}
