package com.peitch.animewatchmaster;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.peitch.animewatchmaster.activities.UpcomingHorAdapter;
import com.peitch.animewatchmaster.activities.animebyletter.ActivityLetters;
import com.peitch.animewatchmaster.activities.animeinfo.AnimeInfo;
import com.peitch.animewatchmaster.activities.hotanime.AnimeHotAdapter;
import com.peitch.animewatchmaster.activities.seasons.SeasonMainFragment;
import com.peitch.animewatchmaster.activities.share.ShareActivity;
import com.peitch.animewatchmaster.activities.topanime.TopAnimeActivity;
import com.peitch.animewatchmaster.activities.upcoming.UpcomingActivity;
import com.peitch.animewatchmaster.activities.watched.WatchedAnime;
import com.peitch.animewatchmaster.activities.watchlater.AnimeWatchLater;
import com.peitch.animewatchmaster.activities.watchlist.WatchList;
import com.peitch.animewatchmaster.model.Anime;
import com.peitch.animewatchmaster.model.SeasonModel;
import com.peitch.animewatchmaster.model.WatchlaterlistModel;
import com.peitch.animewatchmaster.utils.Asynctasks.APdatabaseUpdater;
import com.peitch.animewatchmaster.utils.Asynctasks.TopanimeUpdater;
import com.peitch.animewatchmaster.utils.Asynctasks.WatchlistUpdater;
import com.peitch.animewatchmaster.utils.Asynctasks.databaseUpdater;
import com.peitch.animewatchmaster.utils.Asynctasks.hotanimeUpdater;
import com.peitch.animewatchmaster.utils.Asynctasks.imgflagUpdater;
import com.peitch.animewatchmaster.utils.databaseUtils.DBHelper;

import org.lucasr.twowayview.TwoWayView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.content_main);


        try {
            new imgflagUpdater(getApplicationContext()).execute("").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

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


        TextView upcominganimetxt = (TextView)findViewById(R.id.upcominganime);
        upcominganimetxt.setText(DBHelper.getInstance(getApplicationContext()).getCurrentSeason()+ " anime");
        TwoWayView upcominghorlist = (TwoWayView)findViewById(R.id.horupcoming);
        final List<SeasonModel> seasonModels = DBHelper.getInstance(getApplicationContext()).getSeasonData(true, DBHelper.getInstance(getApplicationContext()).getCurrentSeason());
        //final List<UpcomingAnime> upcomingAnimeList = DBHelper.getInstance(getApplicationContext()).getUpcomingAnime();


        if(seasonModels != null && !seasonModels.isEmpty()) {
            UpcomingHorAdapter upcomingAdapter = new UpcomingHorAdapter(getApplicationContext(), seasonModels);
            upcominghorlist.setAdapter(upcomingAdapter);

            upcominghorlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //startActivity(new Intent(MainActivity.this, UpcomingActivity.class));
                    SeasonModel model = (SeasonModel)parent.getItemAtPosition(position);


                    Anime anime = DBHelper.getInstance(getApplicationContext()).getAnimeInfo(model.getAnimeinfo_id());

                    Intent intent = new Intent(getApplicationContext(), AnimeInfo.class);
                    intent.putExtra("anime", anime);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
                }
            });

        }


        setUpFloatingMenu();

        Thread startingUpdaterThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //prepei prwta na teliwsei to database updater gia na ginoun apotelesmatika ta alla 2 updates
                    new databaseUpdater(getApplicationContext()).execute(getString(R.string.base_db_url)).get();
                    new APdatabaseUpdater(getApplicationContext()).execute(getString(R.string.base_db_url));
                    new TopanimeUpdater(getApplicationContext()).execute(getString(R.string.base_db_url));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });
        startingUpdaterThread.start();


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

        floatingActionMenu.bringToFront();


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


    public void getAnimeAZ(final View v) {
        tempDisableView(v,500);
        startActivity(new Intent(this, ActivityLetters.class));
    }


    public void showWatchlist(final View v) {
        tempDisableView(v, 500);

        if(!DBHelper.getInstance(getApplicationContext()).getWatchlistData().isEmpty()) {
            startActivity(new Intent(this, WatchList.class));
        } else {
            Toast.makeText(getApplicationContext(),"Watchlist is empty!",Toast.LENGTH_SHORT).show();
        }
    }


    public void watchLaterList(final View v) {
        tempDisableView(v, 500);

        if(!DBHelper.getInstance(getApplicationContext()).getWatchlaterlistData().isEmpty()) {
            startActivity(new Intent(this, AnimeWatchLater.class));
        } else {
            Toast.makeText(getApplicationContext(),"Watchlater list is empty!",Toast.LENGTH_SHORT).show();
        }


    }

    public void watchedList(final View v){
        tempDisableView(v, 500);

        if(!DBHelper.getInstance(getApplicationContext()).getWatchedListData().isEmpty()) {
            startActivity(new Intent(this, WatchedAnime.class));
        } else {
            Toast.makeText(getApplicationContext(),"Watched list is empty!",Toast.LENGTH_SHORT).show();
        }

    }

    public void topanime(final View v) {
        tempDisableView(v,500);
        startActivity(new Intent(this, TopAnimeActivity.class));
    }

    public void season(View v) {
        tempDisableView(v,1000);
        //startActivity(new Intent(this, SeasonsMainActivity.class));
        startActivity(new Intent(this, SeasonMainFragment.class));
    }

    public void upcoming(View v) {
        //tempDisableView(v,500);
        //startActivity(new Intent(this, SeasonsMainActivity.class));
    }

    public void upcomingList(View v) {
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
