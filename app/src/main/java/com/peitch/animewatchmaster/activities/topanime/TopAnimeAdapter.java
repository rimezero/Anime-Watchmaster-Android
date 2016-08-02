package com.peitch.animewatchmaster.activities.topanime;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peitch.animewatchmaster.R;
import com.peitch.animewatchmaster.activities.animeinfo.AnimeInfo;
import com.peitch.animewatchmaster.model.Anime;
import com.peitch.animewatchmaster.model.TopanimeModel;
import com.peitch.animewatchmaster.utils.Asynctasks.WatchlistUpdater;
import com.peitch.animewatchmaster.utils.Utils;
import com.peitch.animewatchmaster.utils.databaseUtils.DBHelper;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.StringTokenizer;

import me.grantland.widget.AutofitTextView;

/**
 * Created by abraham on 2/7/2016.
 */
public class TopAnimeAdapter extends ArrayAdapter<TopanimeModel> {

    public TopAnimeAdapter(Context context,List<TopanimeModel> models) {
        super(context,0,models);
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent){

        final TopanimeModel model = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_top_anime_row,parent,false);
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

        TextView score = (TextView)convertView.findViewById(R.id.score);
        ImageView ratingstar = (ImageView)convertView.findViewById(R.id.ratingstar);

        if(model.getScore() > 0) {
            score.setText("" + model.getScore());
            ratingstar.setVisibility(View.VISIBLE);
        } else {
            score.setText("");
            ratingstar.setVisibility(View.GONE);
        }

        final LinearLayout morelinear = (LinearLayout)convertView.findViewById(R.id.morelinear);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(morelinear.getVisibility() == View.VISIBLE) {

                    morelinear.setVisibility(View.GONE);

                }

            }
        });


        ImageView moreimage = (ImageView)convertView.findViewById(R.id.moreimage);
        moreimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(morelinear.getVisibility() == View.VISIBLE) {

                    morelinear.setVisibility(View.GONE);

                } else {

                    morelinear.setAlpha(0f);
                    morelinear.setVisibility(View.VISIBLE);
                    morelinear.animate()
                            .alpha(1f)
                            .setDuration(400)
                            .start();
                }

            }
        });


        final ImageView watchlistimage = (ImageView)convertView.findViewById(R.id.watchlistaddimage);
        final ImageView watchlaterimage = (ImageView)convertView.findViewById(R.id.watchlaterimage);
        final ImageView watchedimage = (ImageView)convertView.findViewById(R.id.watchedimage);

        final DBHelper dbHelper = DBHelper.getInstance(getContext());

        final Bitmap bitmapDone = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ic_done_white_24dp);
        Bitmap bitmapAdd = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ic_add_circle_outline_white_24dp);


        if(dbHelper.checkIfExistsInWatchlist(model.getId())) {
            watchlistimage.setImageBitmap(bitmapDone);
        } else {
            watchlistimage.setImageBitmap(bitmapAdd);
        }


        if(dbHelper.checkIfExistsInWatchLaterList(model.getId())) {
            watchlaterimage.setImageBitmap(bitmapDone);
        } else {
            watchlaterimage.setImageBitmap(bitmapAdd);
        }


        if(dbHelper.checkIfExistsInWatchedList(model.getId())) {
            watchedimage.setImageBitmap(bitmapDone);
        } else {
            watchedimage.setImageBitmap(bitmapAdd);
        }


        AutofitTextView addwatched = (AutofitTextView)convertView.findViewById(R.id.addwatched);
        AutofitTextView addwatchlist = (AutofitTextView)convertView.findViewById(R.id.addwatchlist);
        AutofitTextView addwatchlater = (AutofitTextView)convertView.findViewById(R.id.addwatchlater);
        TextView spot = (TextView)convertView.findViewById(R.id.spot);

        spot.setText(""+model.getSpot());




        addwatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                morelinear.setVisibility(View.GONE);
                //do dbstuff
                if(!dbHelper.checkIfExistsInWatchedList(model.getId())) {
                    dbHelper.insertIntoWatchedlist(model.getId());
                }

                watchedimage.setImageBitmap(bitmapDone);

            }
        });


        addwatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                morelinear.setVisibility(View.GONE);
                //do dbstuff
                if(!dbHelper.checkIfExistsInWatchlist(model.getId())) {

                    Anime anime = dbHelper.getAnimeInfo(model.getId());

                    String episodes = anime.getEpisodes();
                    int ep = 0;
                    boolean doUpdateFlag = false;

                    if(episodes!=null && episodes.trim().equals("Ongoing")){
                        doUpdateFlag = true;
                    }else if(episodes.trim().isEmpty()){
                        //do nothing
                    }else if(episodes.contains("+")){
                        StringTokenizer eps = new StringTokenizer(episodes.trim(),"+");
                        try {
                            double d = Double.valueOf(eps.nextToken());
                            ep = (int) d;
                        }catch (NumberFormatException e){
                            Log.e("AnimeInfo - addToWlist", " Add to watchlist - Number format exception trying to parse string to double / episodes contains '+'");
                        }
                    }
                    else{
                        try {
                            double d = Double.valueOf(episodes.trim());
                            ep = (int) d;
                        }catch (NumberFormatException e){
                            Log.e("AnimeInfo - addToWlist"," Add to watchlist - Number format exception trying to parse string to double");
                        }
                    }

                    dbHelper.insertIntoWatchlist(anime.getId(), 0, ep, "");

                    if(doUpdateFlag)
                        new WatchlistUpdater(getContext()).execute("");

                    watchlistimage.setImageBitmap(bitmapDone);
                }

            }
        });


        addwatchlater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                morelinear.setVisibility(View.GONE);
                //do dbstuff
                if(!dbHelper.checkIfExistsInWatchLaterList(model.getId())) {
                    dbHelper.insertIntoWatchlaterlist(model.getId());
                }

                watchlaterimage.setImageBitmap(bitmapDone);

            }
        });


        return convertView;
    }


}
