package com.example.admin.animewatchmaster.activities.animebyletter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.model.Anime;
import com.example.admin.animewatchmaster.utils.databaseUtils.DBHelper;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.StringTokenizer;

import me.grantland.widget.AutofitTextView;

/**
 * Created by abraham on 9/6/2016.
 */
public class AnimeLetterAdapter extends ArrayAdapter<Anime> {

    private String queryText = ActivityLetters.queryText;

    public AnimeLetterAdapter(Context context, List<Anime> models) {
        super(context, 0, models);
    }



    @Override
    public View getView(int position,View convertView,ViewGroup parent) {

        final Anime model = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_anime_by_letter_row,parent,false);
        }

        AutofitTextView titlestart = (AutofitTextView)convertView.findViewById(R.id.titlestart);
        titlestart.setText(model.getTitle());
        //TextView titlemiddle = (TextView)convertView.findViewById(R.id.titlemiddle);
        //TextView titleend = (TextView)convertView.findViewById(R.id.titleend);



        /*
        String temptitle = model.getTitle().toLowerCase();
        String title = model.getTitle();
        if(queryText != null && !queryText.isEmpty()) {
            String tempqueryText = queryText;
            tempqueryText = tempqueryText.toLowerCase();

            if(temptitle.contains(tempqueryText) && (title.length() > queryText.length())) {
                //begin
                if(temptitle.startsWith(tempqueryText)) {

                    String sub = title.substring(queryText.length(),title.length());

                    titlestart.setText(queryText);
                    titlestart.setTextColor(Color.YELLOW);
                    titleend.setText(sub);
                    titleend.setTextColor(Color.WHITE);

                    titlemiddle.setText("");


                //end
                } else if(temptitle.endsWith(tempqueryText)) {

                    String sub = title.substring(0,(title.length()-queryText.length()));

                    titlestart.setText(sub);
                    titlestart.setTextColor(Color.WHITE);

                    titleend.setText(queryText);
                    titleend.setTextColor(Color.YELLOW);

                    titlemiddle.setText("");

                //middle
                } else {

                    String substart = title.substring(0,temptitle.lastIndexOf(tempqueryText));
                    String subend = title.substring(substart.length()+queryText.length(),title.length());

                    titlestart.setText(substart);
                    titlestart.setTextColor(Color.WHITE);

                    titlemiddle.setText(queryText);
                    titlemiddle.setTextColor(Color.YELLOW);

                    titleend.setText(subend);
                    titleend.setTextColor(Color.WHITE);
                }
            }
        } else {
            titlestart.setText(model.getTitle());
        }
        */


        ImageView imageView = (ImageView)convertView.findViewById(R.id.image);
        //Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.loading);
        //imageView.setImageBitmap(bitmap);

        try {

            /*

            Target target = new Target()  {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    imageView.setImageBitmap(bitmap);

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    //do stuff if...
                    Log.i("onBitmapFailed", "failed to get bitmap");
                    Bitmap errorBitmap = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ic_signal_wifi_off_white_24dp);
                    imageView.setImageDrawable(null);
                    imageView.setImageBitmap(errorBitmap);


                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            */


            Picasso.with(getContext())
                    .load(model.getImgurl())
                    .fit()
                    .into(imageView);


            int animeId = model.getId();
            final DBHelper dbHelper = DBHelper.getInstance(getContext());
            LinearLayout linearLayoutbookmark = (LinearLayout) convertView.findViewById(R.id.bookmarklinear);
            if(dbHelper.checkIfExistsInWatchlist(animeId) || dbHelper.checkIfExistsInWatchedList(animeId) || dbHelper.checkIfExistsInWatchLaterList(animeId)) {
                linearLayoutbookmark.setVisibility(View.VISIBLE);
            } else {
                linearLayoutbookmark.setVisibility(View.GONE);
            }


            final LinearLayout morelinear = (LinearLayout)convertView.findViewById(R.id.morelinear);

            titlestart.setOnClickListener(new View.OnClickListener() {
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

            final Bitmap bitmapDone = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_done_white_24dp);
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
            AutofitTextView addmore = (AutofitTextView)convertView.findViewById(R.id.addmore);


            addmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    morelinear.setVisibility(View.GONE);
                    //future staff like share..
                }
            });

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
                        dbHelper.insertIntoWatchlist(model.getId(),0,0,"");

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


        } catch (Exception ex) {

        }

        AutofitTextView textView = (AutofitTextView)convertView.findViewById(R.id.type);
        textView.setText(model.getAnimetype());

        convertView.setAlpha(0f);
        convertView.animate()
                .alpha(1f)
                .setDuration(750)
                .start();

        return convertView;

    }





}
