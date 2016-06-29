package com.example.admin.animewatchmaster.activities.animeinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    private TextView watchlisttext;

    private LinearLayout watchlaterlinear;
    private TextView watchlatertext;


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

                watchlisttext = (TextView)findViewById(R.id.watchlisttext);
                watchlisttext.setText("WatchList");

            } else {

            }

            if(dbHelper.checkIfExistsInWatchLaterList(anime.getId())) {

                watchlaterlinear = (LinearLayout)findViewById(R.id.watchlaterlinear);
                animateView(watchlaterlinear);

                watchlatertext = (TextView)findViewById(R.id.watchlatertext);
                watchlatertext.setText("WatchLater");


            } else {

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
            }else{
                double d = Double.valueOf(anime.getEpisodes().trim());
                ep = (int) d;
            }

            dbHelper.insertIntoWatchlist(anime.getId(), 0, ep, "");
            dbHelper.deleteWatchlaterlistAnime(anime.getId());

            if(doUpdateFlag)
                new WatchlistUpdater(getApplicationContext()).execute("");

            watchlistlinear = (LinearLayout)findViewById(R.id.watchlistlinear);
            animateView(watchlistlinear);

            watchlisttext = (TextView)findViewById(R.id.watchlisttext);
            watchlisttext.setText("WatchList");


        } else {
            //Toast.makeText(getApplicationContext(),"anime already in watchlist",Toast.LENGTH_SHORT).show();
        }

    }


    public void addToWatchlaterlist(final View v) {


        tempDisableView(v,500);

         dbHelper = DBHelper.getInstance(getApplicationContext());

        if(!dbHelper.checkIfExistsInWatchLaterList(anime.getId())) {


            dbHelper.insertIntoWatchlaterlist(anime.getId());

            watchlaterlinear = (LinearLayout)findViewById(R.id.watchlaterlinear);
            animateView(watchlaterlinear);

            watchlatertext = (TextView)findViewById(R.id.watchlatertext);
            watchlatertext.setText("WatchLater");


        } else {
            //Toast.makeText(getApplicationContext(),"anime already in watch later list",Toast.LENGTH_SHORT).show();
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
