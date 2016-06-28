package com.example.admin.animewatchmaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.example.admin.animewatchmaster.model.WatchListModel;
import com.example.admin.animewatchmaster.utils.Asynctasks.WatchlistUpdater;
import com.example.admin.animewatchmaster.activities.animebyletter.ActivityLetters;
import com.example.admin.animewatchmaster.activities.animeinfo.AnimeInfo;
import com.example.admin.animewatchmaster.utils.Asynctasks.databaseUpdater;
import com.example.admin.animewatchmaster.utils.databaseUtils.DBHelper;
import com.example.admin.animewatchmaster.activities.hotanime.AnimeHotAdapter;
import com.example.admin.animewatchmaster.model.Anime;
import com.example.admin.animewatchmaster.model.WatchlaterlistModel;
import com.example.admin.animewatchmaster.activities.watchlater.AnimeWatchLater;
import com.example.admin.animewatchmaster.activities.watchlist.WatchList;
import com.example.admin.animewatchmaster.utils.Asynctasks.hotanimeUpdater;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //removed for testing enable afterwards
        //new WatchlistUpdater(getApplicationContext()).execute();


        TwoWayView twoWayView = (TwoWayView)findViewById(R.id.horizlist);

        List<WatchlaterlistModel> animes = DBHelper.getInstance(getApplicationContext()).getHotAnimeData();
        AnimeHotAdapter animeHotAdapter = new AnimeHotAdapter(getApplicationContext(),animes);
        twoWayView.setAdapter(animeHotAdapter);

        twoWayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                WatchlaterlistModel watchlaterlistModel = (WatchlaterlistModel)parent.getItemAtPosition(position);

                Anime anime = DBHelper.getInstance(getApplicationContext()).getAnimeInfo(watchlaterlistModel.getId());

                if(anime != null) {
                    Intent intent = new Intent(MainActivity.this, AnimeInfo.class);
                    intent.putExtra("anime",anime);
                    startActivity(intent);
                }

            }
        });



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void callit(View v){
        new databaseUpdater(this).execute(getString(R.string.base_db_url));
        //new hotanimeUpdater(this).execute(getString(R.string.base_db_url));

        //DBHelper dbinstance = DBHelper.getInstance(this);
        //dbinstance.insertIntoWatchlist(dbinstance.getAnimeID("009-1"),0,0,"vaggelis");
        //dbinstance.insertIntoWatchlist(dbinstance.getAnimeID("11eyes"),0,0,"gamidia");
        /*
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(2000);
        ids.add(2001);
        dbinstance.handleWatchlistRemainingUpdate(ids);
        List<WatchListModel> walist = dbinstance.getWatchlistData();
        for(WatchListModel model : walist){
            Log.d("mainActivity - Result","Title: "+model.getTitle()+" Current episode: "+model.getCurrentEpisode());
        }*/
        /*
        dbinstance.insertIntoAnimeinfo("$:)';;;sdasd$@#!^&*(){}","adasdasd","agaeghaegaeg","arharh","ahaerhaha","aehahaeh","erhaerhaeh");
        int id = dbinstance.getAnimeID("$:)';;;sdasd$@#!^&*(){}");
        dbinstance.deleteAnime(id);*/
        //dbinstance.insertIntoWatchlist(dbinstance.getAnimeID("Sousei no Onmyouji"),0,0,"");
        //dbinstance.insertIntoWatchlist(dbinstance.getAnimeID("Koutetsujou no Kabaneri"),0,0,"");
        //dbinstance.insertIntoWatchlaterlist(dbinstance.getAnimeID("Sousei no Onmyouji"));
        //dbinstance.insertIntoWatchlaterlist(dbinstance.getAnimeID("Koutetsujou no Kabaneri"));
        //new WatchlistUpdater(this).execute("");
    }

    public void getAnimeAZ(final View v) {
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
        },500);

        startActivity(new Intent(this, ActivityLetters.class));
    }

    public void showWatchlist(final View v) {

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
        },500);

        startActivity(new Intent(this, WatchList.class));
    }


    public void watchLaterList(final View v) {
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
        },500);

        startActivity(new Intent(this, AnimeWatchLater.class));


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
