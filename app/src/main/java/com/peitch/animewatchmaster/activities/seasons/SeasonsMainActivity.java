package com.peitch.animewatchmaster.activities.seasons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.peitch.animewatchmaster.R;
import com.peitch.animewatchmaster.model.SeasonModel;
import com.peitch.animewatchmaster.model.SeasonsSortModel;
import com.peitch.animewatchmaster.utils.databaseUtils.DBHelper;
import com.facebook.appevents.AppEventsLogger;

import org.lucasr.twowayview.TwoWayView;

import java.util.List;

/**
 * Created by abraham on 4/7/2016.
 */
public class SeasonsMainActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    private List<SeasonsSortModel> seasonsSortModel;
    private List<SeasonModel> seasonModelsList;
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

                loadGridView(seasonModel,-1);

                twoWayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        SeasonsSortModel seasonsSortModel1 = (SeasonsSortModel) parent.getItemAtPosition(position);

                        String season = seasonsSortModel1.toString();

                        List<SeasonModel> seasonModels = dbHelper.getSeasonData(true, season);

                        seasonModelsList = seasonModels;

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


    private void loadGridView(List<SeasonModel> animeList, int colsNum){

        seasonModelsList = animeList;

        final GridView gridView = (GridView) findViewById(R.id.listview);

        if(colsNum == 1) {
            gridView.setNumColumns(1);
        } else if(colsNum == 2) {
            gridView.setNumColumns(2);
        } else if(colsNum == 3) {
            gridView.setNumColumns(3);
        }

        SeasonDataAdapter topAnimeAdapter = new SeasonDataAdapter(getApplicationContext(), animeList);
        gridView.setAdapter(topAnimeAdapter);


    }


    public void listgridswitch(View v) {


        GridView gridView = (GridView) findViewById(R.id.listview);

        if(!seasonModelsList.isEmpty() && gridView.getVisibility() == View.VISIBLE) {

            ImageView imageView = (ImageView) findViewById(R.id.imagebtnswitch);

            Bitmap bitmap;

            if(gridView.getNumColumns() == 3) {

                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_apps_black_24dp);
                imageView.setImageBitmap(bitmap);

                loadGridView(seasonModelsList, 2);

            } else if (gridView.getNumColumns() == 2) {

                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_apps_black_24dp);
                imageView.setImageBitmap(bitmap);

                loadGridView(seasonModelsList, 1);

            } else {

                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_list_black_24dp);
                imageView.setImageBitmap(bitmap);

                loadGridView(seasonModelsList,3);

            }

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
                    seasonModelsList = seasonModels;
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
                    seasonModelsList = seasonModels;
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
