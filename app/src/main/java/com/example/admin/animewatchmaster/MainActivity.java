package com.example.admin.animewatchmaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.admin.animewatchmaster.Jsoup.WatchlistUpdater;
import com.example.admin.animewatchmaster.animebyletter.ActivityLetters;
import com.example.admin.animewatchmaster.watchlater.AnimeWatchLater;
import com.example.admin.animewatchmaster.watchlist.WatchList;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new WatchlistUpdater(getApplicationContext()).execute();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void callit(View v){
        //new databaseUpdater(this).execute(getString(R.string.base_db_url));
        //DBHelper dbinstance = DBHelper.getInstance(this);
        /*
        dbinstance.insertIntoWatchlist(dbinstance.getAnimeID("Sousei no Onmyouji"),0,0,"");
        dbinstance.insertIntoWatchlist(dbinstance.getAnimeID("Koutetsujou no Kabaneri"),0,0,"");*/
        new WatchlistUpdater(this).execute("");
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
