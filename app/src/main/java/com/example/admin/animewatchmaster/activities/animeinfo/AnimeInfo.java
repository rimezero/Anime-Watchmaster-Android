package com.example.admin.animewatchmaster.activities.animeinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.model.Anime;
import com.example.admin.animewatchmaster.utils.Asynctasks.WatchlistUpdater;
import com.example.admin.animewatchmaster.utils.databaseUtils.DBHelper;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by abraham on 9/6/2016.
 */
public class AnimeInfo extends AppCompatActivity {

    private Anime anime;
    private DBHelper dbHelper;
    private CircularProgressButton watchlistbtn;
    private CircularProgressButton watchlaterbtn;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_animeinfo);

        anime = (Anime) getIntent().getSerializableExtra("anime");

        if(anime != null) {

            watchlistbtn = (CircularProgressButton)findViewById(R.id.watchlistbtn);
            watchlaterbtn = (CircularProgressButton)findViewById(R.id.watchlaterbtn);

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

            TextView desc = (TextView)findViewById(R.id.desc);
            desc.setText(anime.getDescription());


            if(dbHelper.getInstance(getApplicationContext()).checkIfExistsInWatchlist(anime.getId())) {

                watchlistbtn.setIndeterminateProgressMode(true);
                watchlistbtn.setProgress(0);
                watchlistbtn.setProgress(100);

            } else {

            }


            if(dbHelper.getInstance(getApplicationContext()).checkIfExistsInWatchLaterList(anime.getId())) {


                watchlaterbtn.setIndeterminateProgressMode(true);
                watchlaterbtn.setProgress(0);
                watchlaterbtn.setProgress(100);

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

        watchlistbtn = (CircularProgressButton)findViewById(R.id.watchlistbtn);

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


            watchlistbtn.setIndeterminateProgressMode(true);
            watchlistbtn.setProgress(0);
            watchlistbtn.setProgress(100);

        } else {
            //Toast.makeText(getApplicationContext(),"anime already in watchlist",Toast.LENGTH_SHORT).show();
        }

    }


    public void addToWatchlaterlist(final View v) {

        watchlaterbtn = (CircularProgressButton)findViewById(R.id.watchlaterbtn);

        tempDisableView(v,500);

         dbHelper = DBHelper.getInstance(getApplicationContext());

        if(!dbHelper.checkIfExistsInWatchLaterList(anime.getId())) {

            dbHelper.insertIntoWatchlaterlist(anime.getId());


            watchlaterbtn.setIndeterminateProgressMode(true);
            watchlaterbtn.setProgress(0);
            watchlaterbtn.setProgress(100);

        } else {
            //Toast.makeText(getApplicationContext(),"anime already in watch later list",Toast.LENGTH_SHORT).show();
        }


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
