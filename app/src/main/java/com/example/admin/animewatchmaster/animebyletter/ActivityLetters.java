package com.example.admin.animewatchmaster.animebyletter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

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



    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_letters);

        List<String> letters = getAllLetters();
        List<LinearLayout> linearLayouts = new ArrayList<>();


        for(final String letter : letters) {

            String linearId = letter;
            int resID = getResources().getIdentifier(linearId,"id",getPackageName());
            final LinearLayout linearLayout = (LinearLayout)findViewById(resID);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String listID = "letterlist" + letter;
                    int resListID = getResources().getIdentifier(listID, "id", getPackageName());

                    List<Anime> animes = DBHelper.getInstance(getApplicationContext()).getAllAnimeByLetter(letter);

                    JazzyListView listView = (JazzyListView) findViewById(resListID);
                    listView.setTransitionEffect(new SlideInEffect());

                    listView.setOnTouchListener(new View.OnTouchListener() {
                        // Setting on Touch Listener for handling the touch inside ScrollView
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            // Disallow the touch request for parent scroll on touch of child view
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            return false;
                        }
                    });

                    AnimeLetterAdapter animeLetterAdapter = new AnimeLetterAdapter(getApplicationContext(), animes);
                    listView.setAdapter(animeLetterAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Anime anime = (Anime) parent.getItemAtPosition(position);
                            Intent intent = new Intent(ActivityLetters.this, AnimeInfo.class);
                            intent.putExtra("anime",anime);
                            startActivity(intent);

                        }
                    });

                    animateOpenCloseView(listView);

                }
            });


            linearLayouts.add(linearLayout);
        }

        animateMainChapters(linearLayouts);



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
