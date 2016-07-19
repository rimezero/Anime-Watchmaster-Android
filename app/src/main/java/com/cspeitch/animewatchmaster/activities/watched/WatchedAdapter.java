package com.cspeitch.animewatchmaster.activities.watched;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.cspeitch.animewatchmaster.R;
import com.cspeitch.animewatchmaster.activities.animeinfo.AnimeInfo;
import com.cspeitch.animewatchmaster.model.Anime;
import com.cspeitch.animewatchmaster.model.WatchedModel;
import com.cspeitch.animewatchmaster.utils.databaseUtils.DBHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.grantland.widget.AutofitTextView;

/**
 * Created by admin on 6/29/2016.
 */
public class WatchedAdapter extends ArrayAdapter<WatchedModel> {

    public WatchedAdapter(Context context, List<WatchedModel> models) {
        super(context,0,models);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final WatchedModel model = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_watchedlist_row,parent,false);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.image);


        if(model.getImgurl() != null && !model.getImgurl().trim().isEmpty()) {
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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Anime anime = DBHelper.getInstance(getContext()).getAnimeInfo(model.getId());

                Intent intent = new Intent(getContext(), AnimeInfo.class);
                intent.putExtra("anime", anime);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);

            }
        });

        AutofitTextView textView = (AutofitTextView)convertView.findViewById(R.id.title);
        textView.setText(model.getTitle());

        AutofitTextView txtGenre = (AutofitTextView)convertView.findViewById(R.id.genres);
        if(model.getGenre()!=null && !model.getGenre().trim().isEmpty())
            txtGenre.setText("Genre | "+model.getGenre());

        Button btn = (Button)convertView.findViewById(R.id.BtnRemove);
        btn.setOnClickListener(new RemoveOnClick(model));

        return convertView;
    }



    private class RemoveOnClick implements View.OnClickListener{
        private WatchedModel model;

        public RemoveOnClick(WatchedModel model) {
            this.model = model;
        }

        @Override
        public void onClick(View v) {
            DBHelper.getInstance(getContext()).deleteWatchedlistAnime(model.getId());
            remove(model);
        }
    }
}
