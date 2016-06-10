package com.example.admin.animewatchmaster.animebyletter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.animeinfo.AnimeInfo;
import com.example.admin.animewatchmaster.databaseUtils.DBHelper;
import com.example.admin.animewatchmaster.model.Anime;
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

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;



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






    }



    private class OnChangeListener implements SearchView.OnQueryTextListener {


        @Override
        public boolean onQueryTextSubmit(String query) {
            if(query.length() >= 3) {

                makeAllLinearViewGone(linearList);

                List<Anime> animes = dbHelper.getAllAnime(3,query,genres);

                final JazzyListView jazzyListView = (JazzyListView)findViewById(R.id.querylist);
                jazzyListView.setVisibility(View.VISIBLE);
                AnimeLetterAdapter animeLetterAdapter = new AnimeLetterAdapter(getApplicationContext(),animes);
                jazzyListView.setTransitionEffect(new SlideInEffect());

                jazzyListView.setAdapter(animeLetterAdapter);

                jazzyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                        jazzyListView.setEnabled(false);

                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        jazzyListView.setEnabled(true);
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

            } else if(query.length() == 0) {

                JazzyListView jazzyListView = (JazzyListView)findViewById(R.id.querylist);
                jazzyListView.setVisibility(View.GONE);
                makeAllLinearViewVisible(linearList);

            }
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if(newText.length() >= 3) {

                makeAllLinearViewGone(linearList);

                List<Anime> animes = dbHelper.getAllAnime(2,newText,null);

                final JazzyListView jazzyListView = (JazzyListView)findViewById(R.id.querylist);
                jazzyListView.setVisibility(View.VISIBLE);
                AnimeLetterAdapter animeLetterAdapter = new AnimeLetterAdapter(getApplicationContext(),animes);
                jazzyListView.setTransitionEffect(new SlideInEffect());

                jazzyListView.setAdapter(animeLetterAdapter);

                jazzyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                        jazzyListView.setEnabled(false);

                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        jazzyListView.setEnabled(true);
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

            } else if(newText.length() == 0) {

                JazzyListView jazzyListView = (JazzyListView)findViewById(R.id.querylist);
                jazzyListView.setVisibility(View.GONE);
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
