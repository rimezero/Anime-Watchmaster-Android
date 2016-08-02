package com.peitch.animewatchmaster.activities.topanime;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.peitch.animewatchmaster.R;
import com.peitch.animewatchmaster.model.TopanimeModel;
import com.peitch.animewatchmaster.utils.databaseUtils.DBHelper;
import com.facebook.appevents.AppEventsLogger;
import com.twotoasters.jazzylistview.JazzyGridView;
import com.twotoasters.jazzylistview.effects.SlideInEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abraham on 2/7/2016.
 */
public class TopAnimeActivity extends AppCompatActivity {

    private List<TopanimeModel> animeListState = new ArrayList<>();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_top_anime);

        List<TopanimeModel> topanimeModelList = DBHelper.getInstance(getApplicationContext()).getTopAnimeData();

        if(topanimeModelList != null && !topanimeModelList.isEmpty()) {

            loadGridView(topanimeModelList,-1);

        } else {
            finish();
        }


    }


    private void loadGridView(List<TopanimeModel> animeList, int colsNum){
        animeListState = animeList;

        final JazzyGridView gridView = (JazzyGridView) findViewById(R.id.gridview);

        if(colsNum == 1) {
            gridView.setNumColumns(1);
        } else if(colsNum == 2) {
            gridView.setNumColumns(2);
        } else if(colsNum == 3) {
            gridView.setNumColumns(3);
        }

        TopAnimeAdapter topAnimeAdapter = new TopAnimeAdapter(getApplicationContext(), animeList);
        gridView.setTransitionEffect(new SlideInEffect());
        gridView.setAdapter(topAnimeAdapter);


    }


    public void listgridswitch(View v) {


        JazzyGridView gridView = (JazzyGridView) findViewById(R.id.gridview);

        if(!animeListState.isEmpty() && gridView.getVisibility() == View.VISIBLE) {

            ImageView imageView = (ImageView) findViewById(R.id.imagebtnswitch);

            Bitmap bitmap;

            if(gridView.getNumColumns() == 3) {

                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_apps_black_24dp);
                imageView.setImageBitmap(bitmap);

                loadGridView(animeListState, 2);

            } else if (gridView.getNumColumns() == 2) {

                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_apps_black_24dp);
                imageView.setImageBitmap(bitmap);

                loadGridView(animeListState, 1);

            } else {

                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_list_black_24dp);
                imageView.setImageBitmap(bitmap);

                loadGridView(animeListState,3);

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


}
