package com.example.admin.animewatchmaster.activities.animeinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.model.Anime;
import com.example.admin.animewatchmaster.utils.Asynctasks.WatchlistUpdater;
import com.example.admin.animewatchmaster.utils.databaseUtils.DBHelper;
import com.squareup.picasso.Picasso;

import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import me.grantland.widget.AutofitTextView;

/**
 * Created by abraham on 9/6/2016.
 */
public class AnimeInfo extends AppCompatActivity {

    private Anime anime;
    private DBHelper dbHelper;

    private LinearLayout watchlistlinear;
    private ImageView watchlistplusbtn;

    private LinearLayout watchlaterlinear;
    private ImageView watchlaterplusbtn;

    private LinearLayout watchedlinear;
    private ImageView watchedplusbtn;


    @Override
    protected void onCreate(Bundle bundle) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(bundle);
        setContentView(R.layout.layout_animeinfo);

        anime = (Anime) getIntent().getSerializableExtra("anime");

        if(anime != null) {


            dbHelper = DBHelper.getInstance(getApplicationContext());

            TextView title = (TextView)findViewById(R.id.title);
            title.setText(anime.getTitle());


            ImageView imageView = (ImageView)findViewById(R.id.image);

            try {

                Picasso.with(getApplicationContext())
                        .load(anime.getImgurl())
                        .into(imageView);

            } catch (Exception ex) {

            }


            AutofitTextView autofitTextView = (AutofitTextView)findViewById(R.id.genrestext);
            autofitTextView.setText(anime.getGenre());

            TextView desc = (TextView)findViewById(R.id.desc);
            desc.setText(anime.getDescription());


            if(dbHelper.checkIfExistsInWatchlist(anime.getId())) {

                watchlistlinear = (LinearLayout)findViewById(R.id.watchlistlinear);
                animateView(watchlistlinear);

                watchlistplusbtn = (ImageView)findViewById(R.id.watchlistplusbtn);
                watchlistplusbtn.setVisibility(View.GONE);

            }

            if(dbHelper.checkIfExistsInWatchLaterList(anime.getId())) {

                watchlaterlinear = (LinearLayout)findViewById(R.id.watchlaterlinear);
                animateView(watchlaterlinear);

                watchlaterplusbtn = (ImageView)findViewById(R.id.watchlaterplusbtn);
                watchlaterplusbtn.setVisibility(View.GONE);


            }

            if(dbHelper.checkIfExistsInWatchedList(anime.getId())) {

                watchedlinear = (LinearLayout)findViewById(R.id.watched);
                animateView(watchedlinear);

                watchedplusbtn = (ImageView)findViewById(R.id.watchedplusbtn);
                watchedplusbtn.setVisibility(View.GONE);

            }


        } else {
            finish();
        }

    }


    public void goback(View v) {
        finish();
    }


    public void addToWatchlist(final View v) {


        tempDisableView(v,500);

        dbHelper = DBHelper.getInstance(getApplicationContext());

        if(!dbHelper.checkIfExistsInWatchlist(anime.getId())) {


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
                    Log.e("AnimeInfo - addToWlist"," Add to watchlist - Number format exception trying to parse string to double / episodes contains '+'");
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
            dbHelper.deleteWatchlaterlistAnime(anime.getId());

            if(doUpdateFlag)
                new WatchlistUpdater(getApplicationContext()).execute("");

            watchlistlinear = (LinearLayout)findViewById(R.id.watchlistlinear);
            animateView(watchlistlinear);

            watchlistplusbtn = (ImageView)findViewById(R.id.watchlistplusbtn);
            watchlistplusbtn.setVisibility(View.GONE);


        } else {
            //Toast.makeText(getApplicationContext(),"anime already in watchlist",Toast.LENGTH_SHORT).show();
        }

    }


    public void shareAnime(View v) {
        tempDisableView(v,1000);

        //share...
    }


    public void addToWatchlaterlist(final View v) {


        tempDisableView(v,500);

         dbHelper = DBHelper.getInstance(getApplicationContext());

        if(!dbHelper.checkIfExistsInWatchLaterList(anime.getId())) {


            dbHelper.insertIntoWatchlaterlist(anime.getId());

            watchlaterlinear = (LinearLayout)findViewById(R.id.watchlaterlinear);
            animateView(watchlaterlinear);

            watchlaterplusbtn = (ImageView)findViewById(R.id.watchlaterplusbtn);
            watchlaterplusbtn.setVisibility(View.GONE);


        } else {
            //Toast.makeText(getApplicationContext(),"anime already in watch later list",Toast.LENGTH_SHORT).show();
        }


    }


    public void addToWatched(View v) {
        tempDisableView(v,500);

        dbHelper = DBHelper.getInstance(getApplicationContext());

        if(!dbHelper.checkIfExistsInWatchedList(anime.getId())) {

            dbHelper.insertIntoWatchedlist(anime.getId());

            watchedlinear = (LinearLayout)findViewById(R.id.watched);
            animateView(watchedlinear);

            watchedplusbtn = (ImageView)findViewById(R.id.watchedplusbtn);
            watchedplusbtn.setVisibility(View.GONE);
        }

    }


    private void animateView(View v) {
        v.setAlpha(0f);
        v.setVisibility(View.VISIBLE);

        v.animate()
                .alpha(1f)
                .setDuration(1000)
                .start();
    }


    private void tempDisableView(final View v,int milli) {
        v.setEnabled(false);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        v.setEnabled(true);
                    }
                });

            }
        },milli);

    }



}
