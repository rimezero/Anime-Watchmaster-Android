package com.peitch.animewatchmaster.activities.seasons;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import com.peitch.animewatchmaster.model.SeasonModel;
import com.peitch.animewatchmaster.utils.Asynctasks.WatchlistUpdater;
import com.peitch.animewatchmaster.utils.Utils;
import com.peitch.animewatchmaster.utils.databaseUtils.DBHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import me.grantland.widget.AutofitTextView;

/**
 * Created by abraham on 4/7/2016.
 */
public class SeasonDataAdapter extends ArrayAdapter<SeasonModel> {

    //stro getView to target tou picasso krataei adinami anafora kai arketes anafores sto target xanontai kai oi eikones den fortonontai pote
    //kai mia lisi ta pernaw edo
    private Map<Integer,Target> strongReferences;

    public SeasonDataAdapter(Context context,List<SeasonModel> models) {
        super(context,0,models);
        strongReferences = new HashMap<>();
    }



    @Override
    public View getView(int position,View convertView,ViewGroup parent){

        final SeasonModel model = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_season_vert_row,parent,false);
        }

        final ImageView imageView = (ImageView)convertView.findViewById(R.id.image);

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imageView.setImageBitmap(bitmap);
                strongReferences.remove(model.getAnimeinfo_id());
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        strongReferences.put(model.getAnimeinfo_id(),target);


        if(model.getImgurl() != null && !model.getImgurl().trim().isEmpty() && !model.getImgurl().equals("na") && !Utils.imgflag) {

            Picasso.with(getContext())
                    .load(model.getImgurl())
                    .into(target);
        }  else {
            Picasso.with(getContext())
                    .load("http://www.anime-planet.com/inc/img/blank_main.jpg")
                    .into(target);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Anime anime = DBHelper.getInstance(getContext()).getAnimeInfo(model.getAnimeinfo_id());

                Intent intent = new Intent(getContext(), AnimeInfo.class);
                intent.putExtra("anime", anime);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);

            }
        });


        TextView rating = (TextView)convertView.findViewById(R.id.rating);
        ImageView starimage = (ImageView)convertView.findViewById(R.id.ratingstar);
        double rat = model.getRating();

        if(rat > 0) {
            rat*=2;
            rating.setText(""+new DecimalFormat("#.#").format(rat));
            starimage.setVisibility(View.VISIBLE);
        } else {
            rating.setText("");
            starimage.setVisibility(View.GONE);
        }

        AutofitTextView textView = (AutofitTextView)convertView.findViewById(R.id.title);
        textView.setText(model.getTitle());

        TextView type = (TextView)convertView.findViewById(R.id.type);
        type.setText(model.getAnimetype());

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

        final Bitmap bitmapDone = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_done_white_24dp);
        Bitmap bitmapAdd = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ic_add_circle_outline_white_24dp);


        if(dbHelper.checkIfExistsInWatchlist(model.getAnimeinfo_id())) {
            watchlistimage.setImageBitmap(bitmapDone);
        } else {
            watchlistimage.setImageBitmap(bitmapAdd);
        }


        if(dbHelper.checkIfExistsInWatchLaterList(model.getAnimeinfo_id())) {
            watchlaterimage.setImageBitmap(bitmapDone);
        } else {
            watchlaterimage.setImageBitmap(bitmapAdd);
        }


        if(dbHelper.checkIfExistsInWatchedList(model.getAnimeinfo_id())) {
            watchedimage.setImageBitmap(bitmapDone);
        } else {
            watchedimage.setImageBitmap(bitmapAdd);
        }


        AutofitTextView addwatched = (AutofitTextView)convertView.findViewById(R.id.addwatched);
        AutofitTextView addwatchlist = (AutofitTextView)convertView.findViewById(R.id.addwatchlist);
        AutofitTextView addwatchlater = (AutofitTextView)convertView.findViewById(R.id.addwatchlater);



        addwatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                morelinear.setVisibility(View.GONE);
                //do dbstuff
                if(!dbHelper.checkIfExistsInWatchedList(model.getAnimeinfo_id())) {
                    dbHelper.insertIntoWatchedlist(model.getAnimeinfo_id());
                }

                watchedimage.setImageBitmap(bitmapDone);

            }
        });


        addwatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                morelinear.setVisibility(View.GONE);
                //do dbstuff
                if(!dbHelper.checkIfExistsInWatchlist(model.getAnimeinfo_id())) {

                    Anime anime = dbHelper.getAnimeInfo(model.getAnimeinfo_id());

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
                if(!dbHelper.checkIfExistsInWatchLaterList(model.getAnimeinfo_id())) {
                    dbHelper.insertIntoWatchlaterlist(model.getAnimeinfo_id());
                }

                watchlaterimage.setImageBitmap(bitmapDone);

            }
        });


        return convertView;
    }




}
