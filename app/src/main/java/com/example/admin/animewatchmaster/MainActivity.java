package com.example.admin.animewatchmaster;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.admin.animewatchmaster.activities.UpcomingHorAdapter;
import com.example.admin.animewatchmaster.activities.animebyletter.ActivityLetters;
import com.example.admin.animewatchmaster.activities.animeinfo.AnimeInfo;
import com.example.admin.animewatchmaster.activities.hotanime.AnimeHotAdapter;
import com.example.admin.animewatchmaster.activities.seasons.SeasonsMainActivity;
import com.example.admin.animewatchmaster.activities.share.ShareActivity;
import com.example.admin.animewatchmaster.activities.topanime.TopAnimeActivity;
import com.example.admin.animewatchmaster.activities.upcoming.UpcomingActivity;
import com.example.admin.animewatchmaster.activities.watched.WatchedAnime;
import com.example.admin.animewatchmaster.activities.watchlater.AnimeWatchLater;
import com.example.admin.animewatchmaster.activities.watchlist.WatchList;
import com.example.admin.animewatchmaster.model.Anime;
import com.example.admin.animewatchmaster.model.SeasonModel;
import com.example.admin.animewatchmaster.model.WatchlaterlistModel;
import com.example.admin.animewatchmaster.utils.Asynctasks.APdatabaseUpdater;
import com.example.admin.animewatchmaster.utils.Asynctasks.TopanimeUpdater;
import com.example.admin.animewatchmaster.utils.Asynctasks.WatchlistUpdater;
import com.example.admin.animewatchmaster.utils.Asynctasks.databaseUpdater;
import com.example.admin.animewatchmaster.utils.Asynctasks.hotanimeUpdater;
import com.example.admin.animewatchmaster.utils.databaseUtils.DBHelper;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.lucasr.twowayview.TwoWayView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new WatchlistUpdater(getApplicationContext()).execute();

        final TwoWayView twoWayView = (TwoWayView)findViewById(R.id.horizlist);
        //loadHorizontalHotanime(twoWayView); //ama to valeis fortwnei mia fora apo to database kai meta kanei update kai ksanafortwnei

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new hotanimeUpdater(getApplicationContext()).execute(getString(R.string.base_db_url)).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadHorizontalHotanime(twoWayView);
                    }
                });
            }
        }).start();


        TwoWayView upcominghorlist = (TwoWayView)findViewById(R.id.horupcoming);
        List<SeasonModel> seasonModels = DBHelper.getInstance(getApplicationContext()).getSeasonData(false, "Upcoming");


        if(seasonModels != null && !seasonModels.isEmpty()) {
            UpcomingHorAdapter upcomingAdapter = new UpcomingHorAdapter(getApplicationContext(), seasonModels);
            upcominghorlist.setAdapter(upcomingAdapter);

            upcominghorlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(MainActivity.this, UpcomingActivity.class));
                }
            });

        }


        setUpFloatingMenu();


    }


    private void setUpFloatingMenu() {

        FloatingActionMenu floatingActionMenu = (FloatingActionMenu)findViewById(R.id.floatmenu);
        floatingActionMenu.setMenuButtonColorNormal(Color.rgb(31, 66, 145));

        FloatingActionButton watchlistfloat = (FloatingActionButton)findViewById(R.id.watchlistfloat);
        FloatingActionButton watchlaterfloat = (FloatingActionButton)findViewById(R.id.watchlaterfloat);
        FloatingActionButton watchedfloat = (FloatingActionButton)findViewById(R.id.watchedfloat);

        watchlistfloat.setColorNormal(Color.rgb(31, 66, 145));
        watchlaterfloat.setColorNormal(Color.rgb(31, 66, 145));
        watchedfloat.setColorNormal(Color.rgb(31, 66, 145));

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_bookmark_white_24dp);

        watchlistfloat.setImageBitmap(bitmap);
        watchlaterfloat.setImageBitmap(bitmap);
        watchedfloat.setImageBitmap(bitmap);



    }



    private void loadHorizontalHotanime(TwoWayView twoWayView){
        List<WatchlaterlistModel> animes = DBHelper.getInstance(getApplicationContext()).getHotAnimeData();
        AnimeHotAdapter animeHotAdapter = new AnimeHotAdapter(getApplicationContext(),animes);
        twoWayView.setAdapter(animeHotAdapter);

        twoWayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                WatchlaterlistModel watchlaterlistModel = (WatchlaterlistModel) parent.getItemAtPosition(position);

                Anime anime = DBHelper.getInstance(getApplicationContext()).getAnimeInfo(watchlaterlistModel.getId());

                if (anime != null) {
                    Intent intent = new Intent(MainActivity.this, AnimeInfo.class);
                    intent.putExtra("anime", anime);
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


    //esvisa to button
    public void callit(View v){
        //Anime anime = new Anime(-1,"","","Action, Romance","","","","");

        new databaseUpdater(this).execute(getString(R.string.base_db_url));
        //new APdatabaseUpdater(this).execute(getString(R.string.base_db_url));
        //new TopanimeUpdater(this).execute(getString(R.string.base_db_url));
        //new hotanimeUpdater(this).execute(getString(R.string.base_db_url));


        /*
        List<SeasonModel> seasonModels = dbinstance.getSeasonData(false,((SeasonsSortModel)seasons.get(8)).toString());

        System.out.println("------"+seasonModels.size());

        for (SeasonModel s : seasonModels) {
            System.out.println(s.getAnimeinfo_id());
            System.out.println(s.getTitle());
            System.out.println(s.getRating());
            System.out.println(s.toString());
        }

        /*
        ArrayList<Anime> animelist = dbinstance.getAnimeWithSameGenre(anime);
        for(Anime anim : animelist){
            System.out.println(anim.getTitle());
        }*/
       // dbinstance.deleteWatchlistAnime(83);
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
        tempDisableView(v,500);
        startActivity(new Intent(this, ActivityLetters.class));
    }


    public void showWatchlist(final View v) {
        tempDisableView(v, 500);

        if(!DBHelper.getInstance(getApplicationContext()).getWatchlistData().isEmpty()) {
            startActivity(new Intent(this, WatchList.class));
        } else {
            Toast.makeText(getApplicationContext(),"watchlist is empty!",Toast.LENGTH_SHORT).show();
        }
    }


    public void watchLaterList(final View v) {
        tempDisableView(v, 500);

        if(!DBHelper.getInstance(getApplicationContext()).getWatchlaterlistData().isEmpty()) {
            startActivity(new Intent(this, AnimeWatchLater.class));
        } else {
            Toast.makeText(getApplicationContext(),"watchlist is empty!",Toast.LENGTH_SHORT).show();
        }


    }

    public void watchedList(final View v){
        tempDisableView(v, 500);

        if(!DBHelper.getInstance(getApplicationContext()).getWatchedListData().isEmpty()) {
            startActivity(new Intent(this, WatchedAnime.class));
        } else {
            Toast.makeText(getApplicationContext(),"watchlist is empty!",Toast.LENGTH_SHORT).show();
        }

    }

    public void topanime(final View v) {
        tempDisableView(v,500);
        startActivity(new Intent(this, TopAnimeActivity.class));
    }

    public void season(View v) {
        tempDisableView(v,500);
        startActivity(new Intent(this, SeasonsMainActivity.class));
    }

    public void upcoming(View v) {
        tempDisableView(v,500);
        startActivity(new Intent(this, UpcomingActivity.class));
    }


    public void share(View v) {
        tempDisableView(v,500);
        startActivity(new Intent(this, ShareActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        switch (id){
            case R.id.action_databaseUpdater:
                new databaseUpdater(this).execute(getString(R.string.base_db_url));
                return true;
            case R.id.action_APUpdater:
                new APdatabaseUpdater(this).execute(getString(R.string.base_db_url));
                return true;
            case R.id.action_TopAnimeUpdater:
                new TopanimeUpdater(this).execute(getString(R.string.base_db_url));
                return true;
            default:

        }

        return super.onOptionsItemSelected(item);
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
