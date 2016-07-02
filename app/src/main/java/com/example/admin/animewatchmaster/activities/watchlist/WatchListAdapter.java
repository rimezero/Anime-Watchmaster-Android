package com.example.admin.animewatchmaster.activities.watchlist;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.andexert.library.RippleView;
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

        final EditText watchedepisodes = (EditText)convertView.findViewById(R.id.episodeswatched);
        watchedepisodes.setText(""+model.getEpisodeswatched());

        final EditText currentepisodes = (EditText)convertView.findViewById(R.id.currentepisode);
        currentepisodes.setText(""+model.getCurrentEpisode());

        //if(model.getCurrentEpisode() > model.getEpisodeswatched())
        //    watchedepisodes.setTextColor(Color.RED);
        //else
         //   watchedepisodes.setTextColor(Color.BLACK);


        Button btn = (Button)convertView.findViewById(R.id.BtnRemove);
        btn.setOnClickListener(new RemoveOnClick(model));

        RippleView btninc = (RippleView)convertView.findViewById(R.id.btnIncrement);
        RippleView btndec = (RippleView)convertView.findViewById(R.id.btnDecrement);

        btninc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!DBHelper.getInstance(getContext()).incrementEpisodesWatched(model.getId()))
                    return;

                model.setEpisodeswatched(model.getEpisodeswatched() + 1);
                watchedepisodes.setText(""+model.getEpisodeswatched());
                currentepisodes.setText(""+model.getCurrentEpisode());
               // if(model.getCurrentEpisode() > model.getEpisodeswatched())
                 //   watchedepisodes.setTextColor(Color.RED);
               // else
                 //   watchedepisodes.setTextColor(Color.BLACK);

            }
        });

        btndec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!DBHelper.getInstance(getContext()).decrementEpisodesWatched(model.getId()))
                    return;


                model.setEpisodeswatched(model.getEpisodeswatched() - 1);
                watchedepisodes.setText("" + model.getEpisodeswatched());
                currentepisodes.setText("" + model.getCurrentEpisode());
                // if(model.getCurrentEpisode() > model.getEpisodeswatched())
                //    watchedepisodes.setTextColor(Color.RED);
                // else
                //    watchedepisodes.setTextColor(Color.BLACK);

            }
        });


        watchedepisodes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newwatchedepisodevalue = s.toString();
                try {
                    int watchedepisodenum = Integer.valueOf(newwatchedepisodevalue);
                    if(watchedepisodenum>model.getCurrentEpisode()){
                        Log.i("WatchlistAdapter","Episodeswatched value greater than currentepisodes");
                        watchedepisodes.setText(String.valueOf(model.getEpisodeswatched()));
                        return;
                    }
                    if(watchedepisodenum>999||watchedepisodenum<0){
                        Log.i("WatchlistAdapter","Episodeswatched index out of bounds");
                        watchedepisodes.setText(String.valueOf(model.getEpisodeswatched()));
                        return;
                    }

                    model.setEpisodeswatched(watchedepisodenum);
                    DBHelper.getInstance(getContext()).updateWatchlistAnimeEps(model.getId(),null,watchedepisodenum);

                }catch (NumberFormatException ex) {
                    Log.e("WatchlistAdapter","NumberFormatExcpetion trying to parse episodeswatched input to Integer");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        currentepisodes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newcurrentepisodes = s.toString();

                try {
                    int currentepisodesnum = Integer.valueOf(newcurrentepisodes);
                    if(currentepisodesnum<model.getEpisodeswatched()){
                        Log.i("WatchlistAdapter","Currentepisodes value lower than episodeswatched");
                        currentepisodes.setText(String.valueOf(model.getCurrentEpisode()));
                        return;
                    }
                    if(currentepisodesnum>999||currentepisodesnum<0){
                        Log.i("WatchlistAdapter","Currentepisode index out of bounds");
                        currentepisodes.setText(String.valueOf(model.getCurrentEpisode()));
                        return;
                    }

                    model.setCurrentEpisode(currentepisodesnum);
                    DBHelper.getInstance(getContext()).updateWatchlistAnimeEps(model.getId(),currentepisodesnum,null);


                }catch (NumberFormatException ex) {
                    Log.e("WatchlistAdapter","NumberFormatExcpetion trying to parse currentepisodes input to Integer");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
