package com.evolsoft.animewatchmaster.activities.animebyletter;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.evolsoft.animewatchmaster.R;
import com.evolsoft.animewatchmaster.activities.animeinfo.AnimeInfo;
import com.evolsoft.animewatchmaster.activities.drawer.NavDrawerItem;
import com.evolsoft.animewatchmaster.activities.drawer.NavDrawerListAdapter;
import com.evolsoft.animewatchmaster.model.Anime;
import com.evolsoft.animewatchmaster.utils.EffectUtils;
import com.evolsoft.animewatchmaster.utils.Utils;
import com.evolsoft.animewatchmaster.utils.databaseUtils.DBHelper;
import com.facebook.appevents.AppEventsLogger;
import com.twotoasters.jazzylistview.JazzyGridView;
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.SlideInEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by abraham on 9/6/2016.
 */
public class ActivityLetters extends AppCompatActivity {


    private List<LinearLayout> linearList;
    private ArrayList<String> genres = new ArrayList<>();
    private SearchView searchView;

    private DBHelper dbHelper;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;
    private TypedArray navMenuIcons;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    //TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    public static String queryText = "";

    private List<Anime> animeListState = new ArrayList<>();


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_letters);

        List<String> letters = getAllLetters();

        linearList = new ArrayList<>();

        for(final String letter : letters) {

            String linearId = letter;
            int resID = getResources().getIdentifier(linearId,"id",getPackageName());
            final LinearLayout linearLayout = (LinearLayout)findViewById(resID);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

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

                    Intent intent = new Intent(ActivityLetters.this,AnimesByLetter.class);
                    intent.putExtra("letter",letter);
                    startActivity(intent);

                }
            });


            linearList.add(linearLayout);
        }

        dbHelper = DBHelper.getInstance(getApplicationContext());

        animateMainChapters(linearList);

        searchView = (SearchView)findViewById(R.id.searchquery);
        searchView.setOnQueryTextListener(new OnChangeListener());
        searchView.setQueryHint("search anime");

        mTitle = mDrawerTitle = getTitle();

        //mDrawerLayout.openDrawer(Gravity.LEFT);
        //clean drawer and show already saved contacts from sqlite while scanning for new
        List<String> genreslist = Utils.getAllGenres();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        for (String gen : genreslist) {
            navDrawerItems.add(new NavDrawerItem(gen, navMenuIcons.getResourceId(0, -1)));
        }

        View header = getLayoutInflater().inflate(R.layout.drawer_drawerheader,null);
        View header2 = getLayoutInflater().inflate(R.layout.drawer_drawerheader_second,null);

        TextView textView = (TextView)header.findViewById(R.id.lowtext);
        textView.setText("Genres");

        mDrawerList.addHeaderView(header);
        mDrawerList.addHeaderView(header2);

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                //meta na mpei to logo tis efarmogeis
                R.drawable.com_facebook_button_icon, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        )

        {
            public void onDrawerClosed(View view) {

                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };


        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                    JazzyGridView gridView = (JazzyGridView) findViewById(R.id.gridview);
                    gridView.setVisibility(View.GONE);
                    makeAllLinearViewVisible(linearList);

                    mDrawerLayout.closeDrawer(GravityCompat.START);

                } else if (position == 1) {

                    genres.clear();

                    for (int i = 0; i < navDrawerItems.size(); i++) {
                        navDrawerItems.get(i).setIsChecked(false);
                        navDrawerItems.get(i).setChooseicon(navMenuIcons.getResourceId(0, -1));
                    }

                    adapter = new NavDrawerListAdapter(getApplicationContext(),
                            navDrawerItems);
                    mDrawerList.setAdapter(adapter);

                    if (queryText != null && !queryText.isEmpty()) {

                        List<Anime> animes = dbHelper.getAllAnime(3, queryText, genres);

                        setupGridView(animes, 0);

                    }

                } else if (position >= 1 && position <= navDrawerItems.size()+1) {

                    NavDrawerItem navDrawerItem = (NavDrawerItem) parent.getItemAtPosition(position);
                    String genre = navDrawerItem.getTitle();

                    if (navDrawerItem.isChecked()) {

                        if (genres.contains(genre)) {
                            genres.remove(genre);
                        }

                        navDrawerItem.setIsChecked(false);
                        navDrawerItem.setChooseicon(navMenuIcons.getResourceId(0, -1));
                        ImageView checkIcon = (ImageView) view.findViewById(R.id.checkicon);
                        checkIcon.setImageResource(navMenuIcons.getResourceId(0, -1));

                    } else {

                        if (!genres.contains(genre)) {
                            genres.add(genre);
                        }

                        navDrawerItem.setIsChecked(true);
                        navDrawerItem.setChooseicon(navMenuIcons.getResourceId(1, -1));
                        ImageView checkIcon = (ImageView) view.findViewById(R.id.checkicon);
                        checkIcon.setImageResource(navMenuIcons.getResourceId(1, -1));

                    }

                    makeAllLinearViewGone(linearList);

                    if (queryText.isEmpty() && genres.isEmpty()) {
                        //do nothing
                    } else {

                        List<Anime> animes = dbHelper.getAllAnime(3, queryText, genres);

                        setupGridView(animes, 0);

                    }

                }

            }
        });


        ImageView imageView = (ImageView)findViewById(R.id.imagebtnswitch);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_list_white_24dp);
        imageView.setImageBitmap(bitmap);


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



    private void setupGridView(List<Anime> animes,int cols) {

        animeListState = animes;

        final JazzyGridView gridView = (JazzyGridView)findViewById(R.id.gridview);
        gridView.setVisibility(View.VISIBLE);

        if(cols == 1) {
            gridView.setNumColumns(1);
        } else if(cols == 2) {
            gridView.setNumColumns(2);
        } else if(cols == 3) {
            gridView.setNumColumns(3);
        }

        gridView.setLayoutAnimation(EffectUtils.getgridlayoutAnim());
        gridView.setTransitionEffect(new SlideInEffect());
        AnimeLetterAdapter animeLetterAdapter = new AnimeLetterAdapter(getApplicationContext(),animes);

        gridView.setAdapter(animeLetterAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                gridView.setEnabled(false);

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                gridView.setEnabled(true);
                            }
                        });

                    }
                }, 500);

                Anime anime = (Anime) parent.getItemAtPosition(position);
                Intent intent = new Intent(ActivityLetters.this, AnimeInfo.class);
                intent.putExtra("anime", anime);
                startActivity(intent);

            }
        });

        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm.isActive()) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }



                return false;
            }
        });

    }


    public void listgridswitch(View v) {

        JazzyGridView gridView = (JazzyGridView) findViewById(R.id.gridview);

        if(!animeListState.isEmpty() && gridView.getVisibility() == View.VISIBLE) {

            ImageView imageView = (ImageView) findViewById(R.id.imagebtnswitch);

            Bitmap bitmap;

            if(gridView.getNumColumns() == 3) {

                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_apps_white_24dp);
                imageView.setImageBitmap(bitmap);

                setupGridView(animeListState, 2);

            } else if (gridView.getNumColumns() == 2) {

                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_apps_white_24dp);
                imageView.setImageBitmap(bitmap);

                setupGridView(animeListState, 1);

            } else {

                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_list_white_24dp);
                imageView.setImageBitmap(bitmap);

                setupGridView(animeListState,3);

            }

        }


    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
    }


    /**
     * an einai anixto to drawer kai o xristis patisei to koumpi back tou kinitou apla klise to drawer
     * alios do back!
     */
    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }


    public void showGenreDrawer(View v) {

        mDrawerLayout.openDrawer(GravityCompat.START);

    }


    private class OnChangeListener implements SearchView.OnQueryTextListener {


        @Override
        public boolean onQueryTextSubmit(String query) {
            if(query.length() >= 3) {

                queryText = query;

                makeAllLinearViewGone(linearList);

                List<Anime> animes = dbHelper.getAllAnime(3,query,genres);

                setupGridView(animes,0);

            } else if(query.length() == 0) {

                queryText = "";

                JazzyGridView gridView = (JazzyGridView)findViewById(R.id.gridview);
                gridView.setVisibility(View.GONE);
                makeAllLinearViewVisible(linearList);

            }
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if(newText.length() >= 3) {

                queryText = newText;

                makeAllLinearViewGone(linearList);

                List<Anime> animes = dbHelper.getAllAnime(2,newText,null);

                setupGridView(animes,0);

            } else if(newText.length() == 0) {

                queryText = "";

                JazzyGridView gridView = (JazzyGridView)findViewById(R.id.gridview);
                gridView.setVisibility(View.GONE);
                makeAllLinearViewVisible(linearList);

            }
            return false;
        }
    }



    private void makeAllLinearViewGone(List<LinearLayout> linearLayouts) {
        if(linearLayouts != null && !linearLayouts.isEmpty()) {

            for(LinearLayout linearLayout : linearLayouts) {
                linearLayout.setVisibility(View.GONE);
            }

        }
    }


    private void makeAllLinearViewVisible(List<LinearLayout> linearLayouts) {

        if(linearLayouts != null && !linearLayouts.isEmpty()) {

            for(LinearLayout linearLayout : linearLayouts) {
                linearLayout.setVisibility(View.VISIBLE);
            }

        }

    }




    private List<String> getAllLetters() {
        List<String> letters = new ArrayList<>();
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        for(char c : alphabet) {
            letters.add(String.valueOf(c));
        }
        return letters;
    }





    private void animateMainChapters(final List<LinearLayout> linearLayoutList) {
        if(linearLayoutList != null && !linearLayoutList.isEmpty()) {
            //to close views or open views
            if(linearLayoutList.get(0).getVisibility() == View.GONE) {

                int delay = 150;
                for(final LinearLayout ll : linearLayoutList) {
                    ll.setAlpha(0f);
                    ll.setVisibility(View.VISIBLE);


                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {

                        @Override
                        public void run() {

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    ll.animate()
                                            .alpha(1f)
                                            .setDuration(250)
                                            .setListener(null)
                                            .start();

                                }
                            });
                        }
                    },delay);
                    delay += 125;
                }
            }
        }
    }


    //not used
    private void animateOpenCloseView(final JazzyListView jazz) {

            //to close views or open views
            if(jazz.getVisibility() == View.GONE) {

                int delay = 80;
                jazz.setAlpha(0f);
                jazz.setVisibility(View.VISIBLE);


                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    jazz.animate()
                                            .alpha(1f)
                                            .setDuration(250)
                                            .setListener(null)
                                            .start();
                                }
                            });
                        }
                    },delay);

            } else {

                int delay = 80;
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    jazz.animate()
                                            .alpha(0f)
                                            .setDuration(120)
                                            .setListener(null)
                                            .start();
                                }
                            });
                        }
                    },delay);

                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    jazz.setVisibility(View.GONE);
                                }
                            });

                    }
                },delay);

            }

    }


    //not used yet
    private void disableTempView(final View view) {
        view.setEnabled(false);
        Timer timer  = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.setEnabled(true);
                    }
                });
            }
        }, 150);

    }




}
