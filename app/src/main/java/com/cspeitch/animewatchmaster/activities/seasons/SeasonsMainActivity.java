package com.cspeitch.animewatchmaster.activities.seasons;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.cspeitch.animewatchmaster.R;
import com.cspeitch.animewatchmaster.model.SeasonModel;
import com.cspeitch.animewatchmaster.model.SeasonsSortModel;
import com.cspeitch.animewatchmaster.utils.databaseUtils.DBHelper;
import com.facebook.appevents.AppEventsLogger;

import org.lucasr.twowayview.TwoWayView;

import java.util.List;

/**
 * Created by abraham on 4/7/2016.
 */
public class SeasonsMainActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    private List<SeasonsSortModel> seasonsSortModel;
    private int seasonsortPosition = 0;

    private static final int SWIPE_MIN_DISTANCE = 150;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private MyGestureListener myGestureListener;

    private GridView gridView;
    private TwoWayView twoWayView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_season_vert);

        dbHelper = DBHelper.getInstance(getApplicationContext());

        twoWayView = (TwoWayView)findViewById(R.id.horlist);
        seasonsSortModel = dbHelper.getSeasons();

        if(seasonsSortModel != null && !seasonsSortModel.isEmpty()) {

            SeasonYearAdapter seasonYearAdapter = new SeasonYearAdapter(getApplicationContext(), seasonsSortModel);
            twoWayView.setAdapter(seasonYearAdapter);

            gridView = (GridView) findViewById(R.id.listview);
            List<SeasonModel> seasonModel = dbHelper.getSeasonData(true, seasonsSortModel.get(0).toString());

                if (seasonModel.size() == 0) {
                    seasonsortPosition = 1;
                    seasonModel = dbHelper.getSeasonData(true, seasonsSortModel.get(1).toString());
                }

            if(seasonModel != null && !seasonModel.isEmpty()) {

                SeasonDataAdapter seasonDataAdapter = new SeasonDataAdapter(getApplicationContext(), seasonModel);
                gridView.setAdapter(seasonDataAdapter);


                twoWayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        SeasonsSortModel seasonsSortModel1 = (SeasonsSortModel) parent.getItemAtPosition(position);

                        String season = seasonsSortModel1.toString();

                        List<SeasonModel> seasonModels = dbHelper.getSeasonData(true, season);
                        gridView = (GridView) findViewById(R.id.listview);

                        SeasonDataAdapter seasonDataAdapter = new SeasonDataAdapter(getApplicationContext(), seasonModels);
                        gridView.setAdapter(seasonDataAdapter);

                        seasonsortPosition = position;

                    }
                });

                myGestureListener = new MyGestureListener(this);


                gridView.setOnTouchListener(myGestureListener);

            } else {
                finish();
            }

        } else {
            finish();
        }



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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // or implement in activity or component. When your not assigning to a child component.
        return myGestureListener.getDetector().onTouchEvent(event);
    }




    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {

        Context context;
        GestureDetector gDetector;

        public MyGestureListener()
        {
            super();
        }

        public MyGestureListener(Context context) {
            this(context, null);
        }

        public MyGestureListener(Context context, GestureDetector gDetector) {

            if(gDetector == null)
                gDetector = new GestureDetector(context, this);

            this.context = context;
            this.gDetector = gDetector;
        }


        @Override
        public boolean onDown(MotionEvent event) {
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1,MotionEvent e2,float velocityX,float velocityY) {

            if(e1 == null || e2 == null) {
                return false;
            }

            //right to left
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                if((seasonsortPosition + 1) < seasonsSortModel.size()) {

                    seasonsortPosition++;
                    SeasonsSortModel seasonsSortModel1 = seasonsSortModel.get(seasonsortPosition);

                    String season = seasonsSortModel1.toString();

                    List<SeasonModel> seasonModels = dbHelper.getSeasonData(true, season);
                    gridView = (GridView)findViewById(R.id.listview);

                    SeasonDataAdapter seasonDataAdapter = new SeasonDataAdapter(getApplicationContext(),seasonModels);
                    gridView.setAdapter(seasonDataAdapter);
                    twoWayView.smoothScrollToPosition(seasonsortPosition);

                    return true;

                }

                return false;
                //left to right
            } else if(e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                if((seasonsortPosition - 1) >= 0) {

                    if(seasonsortPosition > 0) {
                        seasonsortPosition--;
                    }

                    SeasonsSortModel seasonsSortModel1 = seasonsSortModel.get(seasonsortPosition);

                    String season = seasonsSortModel1.toString();

                    List<SeasonModel> seasonModels = dbHelper.getSeasonData(true, season);
                    gridView = (GridView)findViewById(R.id.listview);

                    SeasonDataAdapter seasonDataAdapter = new SeasonDataAdapter(getApplicationContext(),seasonModels);
                    gridView.setAdapter(seasonDataAdapter);
                    twoWayView.smoothScrollToPosition(seasonsortPosition);

                    return true;

                }


                return false;
            }

            return false;
        }


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            return super.onSingleTapConfirmed(e);
        }

        public GestureDetector getDetector()
        {

            return gDetector;
        }


        @Override
        public boolean onTouch(View v, MotionEvent event) {

            //if(event.getAction() == MotionEvent.ACTION_BUTTON_RELEASE) {
           //     return gDetector.onTouchEvent(event);
            //}

            return gDetector.onTouchEvent(event);
        }

    }












}
