package com.peitch.animewatchmaster.activities.animeinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.peitch.animewatchmaster.R;
import com.peitch.animewatchmaster.model.Anime;
import com.peitch.animewatchmaster.utils.Asynctasks.WatchlistUpdater;
import com.peitch.animewatchmaster.utils.Utils;
import com.peitch.animewatchmaster.utils.databaseUtils.DBHelper;
import com.facebook.appevents.AppEventsLogger;
import com.squareup.picasso.Picasso;

import org.lucasr.twowayview.TwoWayView;

import java.util.List;
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

    private String sharelink;


    @Override
    protected void onCreate(Bundle bundle) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(bundle);
        setContentView(R.layout.layout_animeinfo);

        dbHelper = DBHelper.getInstance(getApplicationContext());

        anime = (Anime) getIntent().getSerializableExtra("anime");

            TwoWayView twoWayView = (TwoWayView)findViewById(R.id.samegenrelist);
            List<Anime> animes = DBHelper.getInstance(getApplicationContext()).getAnimeWithSameGenre(anime);
            AnimeSameGenreAdapter animeHotAdapter = new AnimeSameGenreAdapter(getApplicationContext(),animes);
            twoWayView.setAdapter(animeHotAdapter);

            twoWayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Anime animepos = (Anime) parent.getItemAtPosition(position);

                    Anime anime = DBHelper.getInstance(getApplicationContext()).getAnimeInfo(animepos.getId());
                    initLayout(anime);

                }
            });

        twoWayView.setFocusable(false);

        initLayout(anime);

    }


    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }



    private void initLayout(Anime anime) {

        if(anime != null) {

            sharelink = dbHelper.getMALLink(anime.getId());

            if(sharelink.trim().equals("na")) {
                ImageView shareimg = (ImageView)findViewById(R.id.sharebutton);
                shareimg.setEnabled(false);
            }

            this.anime = anime;


            ImageView imageView = (ImageView)findViewById(R.id.image);

            try {

                if(anime.getImgurl() != null && !anime.getImgurl().trim().isEmpty() && Utils.imgflag) {
                    Picasso.with(getApplicationContext())
                            .load(anime.getImgurl())
                            .into(imageView);
                }  else {
                    Picasso.with(getApplicationContext())
                            .load("http://www.anime-planet.com/inc/img/blank_main.jpg")
                            .into(imageView);
                }

            } catch (Exception ex) {

            }


            AutofitTextView autofitTextView = (AutofitTextView)findViewById(R.id.genrestext);
            autofitTextView.setText(anime.getGenre());

            TextView desc = (TextView)findViewById(R.id.desc);
            desc.setText(anime.getDescription());


            watchlistlinear = (LinearLayout)findViewById(R.id.watchlistlinear);
            watchlistplusbtn = (ImageView)findViewById(R.id.watchlistplusbtn);
            if(dbHelper.checkIfExistsInWatchlist(anime.getId())) {

                animateView(watchlistlinear);
                watchlistplusbtn.setVisibility(View.GONE);

            } else {
                watchlistlinear.setVisibility(View.GONE);
                watchlistplusbtn.setVisibility(View.VISIBLE);
            }

            watchlaterlinear = (LinearLayout)findViewById(R.id.watchlaterlinear);
            watchlaterplusbtn = (ImageView)findViewById(R.id.watchlaterplusbtn);
            if(dbHelper.checkIfExistsInWatchLaterList(anime.getId())) {

                animateView(watchlaterlinear);
                watchlaterplusbtn.setVisibility(View.GONE);


            } else {
                watchlaterlinear.setVisibility(View.GONE);
                watchlaterplusbtn.setVisibility(View.VISIBLE);
            }


            watchedlinear = (LinearLayout)findViewById(R.id.watched);
            watchedplusbtn = (ImageView)findViewById(R.id.watchedplusbtn);
            if(dbHelper.checkIfExistsInWatchedList(anime.getId())) {

                animateView(watchedlinear);
                watchedplusbtn.setVisibility(View.GONE);

            } else {
                watchedlinear.setVisibility(View.GONE);
                watchedplusbtn.setVisibility(View.VISIBLE);
            }


            TextView animetype = (TextView)findViewById(R.id.type);
            animetype.setText(anime.getAnimetype());

            TextView agerating  = (TextView)findViewById(R.id.agerating);
            agerating.setText(anime.getAgerating());

            TextView episodes = (TextView)findViewById(R.id.episodes);
            episodes.setText(anime.getEpisodes());

            TextView title = (TextView)findViewById(R.id.title);
            title.setText(anime.getTitle());

            ScrollView scrollView = (ScrollView)findViewById(R.id.scroll);
            scrollView.fullScroll(ScrollView.FOCUS_UP);
            scrollView.smoothScrollTo(0, 0);

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
            //dbHelper.deleteWatchlaterlistAnime(anime.getId());

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

        if(!sharelink.equals("na")) {

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, sharelink);
            intent.setType("text/plain");
            this.startActivity(Intent.createChooser(intent,"share anime"));


        }


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
