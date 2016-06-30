package com.example.admin.animewatchmaster.activities.watchlist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.activities.animeinfo.AnimeInfo;
import com.example.admin.animewatchmaster.model.Anime;
import com.example.admin.animewatchmaster.model.WatchListModel;
import com.example.admin.animewatchmaster.utils.databaseUtils.DBHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.grantland.widget.AutofitTextView;

/**
 * Created by abraham on 10/6/2016.
 */
public class WatchListAdapter extends ArrayAdapter<WatchListModel> {


    public WatchListAdapter(Context context,List<WatchListModel> models) {
        super(context,0,models);
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        final WatchListModel model = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_watchlist_row,parent,false);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.image);

        AutofitTextView title = (AutofitTextView)convertView.findViewById(R.id.title);
        title.setText(model.getTitle());


        if(model.getImgurl() != null && !model.getImgurl().trim().isEmpty()) {
            Picasso.with(getContext())
                    .load(model.getImgurl())
                    .into(imageView);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Anime anime = DBHelper.getInstance(getContext()).getAnimeInfo(model.getId());

                Intent intent = new Intent(getContext(), AnimeInfo.class);
                intent.putExtra("anime",anime);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);

            }
        });

        final TextView textView = (TextView)convertView.findViewById(R.id.episodes);

        textView.setText("Episodes: "+model.getEpisodeswatched()+"/"+model.getCurrentEpisode());

        if(model.getCurrentEpisode() > model.getEpisodeswatched())
            textView.setTextColor(Color.RED);
        else
            textView.setTextColor(Color.BLACK);


        Button btn = (Button)convertView.findViewById(R.id.BtnRemove);
        btn.setOnClickListener(new RemoveOnClick(model));

        ImageView btninc = (ImageView)convertView.findViewById(R.id.btnIncrement);
        ImageView btndec = (ImageView)convertView.findViewById(R.id.btnDecrement);

        btninc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!DBHelper.getInstance(getContext()).incrementEpisodesWatched(model.getId()))
                    return;


                model.setEpisodeswatched(model.getEpisodeswatched() + 1);
                textView.setText("Episodes: " + model.getEpisodeswatched() + "/" + model.getCurrentEpisode());
                if(model.getCurrentEpisode() > model.getEpisodeswatched())
                    textView.setTextColor(Color.RED);
                else
                    textView.setTextColor(Color.BLACK);


            }
        });

        btndec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!DBHelper.getInstance(getContext()).decrementEpisodesWatched(model.getId()))
                    return;


                model.setEpisodeswatched(model.getEpisodeswatched()-1);
                textView.setText("Episodes: " + model.getEpisodeswatched() + "/" + model.getCurrentEpisode());
                if(model.getCurrentEpisode() > model.getEpisodeswatched())
                    textView.setTextColor(Color.RED);
                else
                    textView.setTextColor(Color.BLACK);


            }
        });

        return convertView;
    }

    private class RemoveOnClick implements View.OnClickListener{
        private WatchListModel model;

        public RemoveOnClick(WatchListModel model) {
            this.model = model;
        }

        @Override
        public void onClick(View v) {

            DBHelper.getInstance(getContext()).deleteWatchlistAnime(model.getId());
            remove(model);
            if(!DBHelper.getInstance(getContext()).checkIfExistsInWatchedList(model.getId()))
                DBHelper.getInstance(getContext()).insertIntoWatchedlist(model.getId());

        }
    }


}
