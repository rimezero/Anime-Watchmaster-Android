package com.cspeitch.animewatchmaster.activities.upcoming;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cspeitch.animewatchmaster.R;
import com.cspeitch.animewatchmaster.model.Anime;
import com.cspeitch.animewatchmaster.model.SeasonModel;
import com.cspeitch.animewatchmaster.model.UpcomingAnime;
import com.facebook.appevents.AppEventsLogger;
import com.squareup.picasso.Picasso;

import me.grantland.widget.AutofitTextView;

/**
 * Created by admin on 7/19/2016.
 */
public class UpcomingInfo extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_upcoming_info);

        UpcomingAnime anime = (UpcomingAnime) getIntent().getSerializableExtra("anime");
        if(anime != null) {

            ImageView imageView = (ImageView) findViewById(R.id.image);

            /*if (anime.getImageurl() != null && !anime.getImageurl().trim().isEmpty() && !anime.getImageurl().equals("na")) {
                Picasso.with(getApplicationContext())
                        .load(anime.getImageurl())
                        .fit()
                        .into(imageView);
            }else{*/
                Picasso.with(getApplicationContext())
                        .load("http://www.anime-planet.com/inc/img/blank_main.jpg")
                        .fit()
                        .into(imageView);
            //}


            TextView type = (TextView)findViewById(R.id.type);
            type.setText(anime.getType());

            TextView title = (TextView)findViewById(R.id.title);
            title.setText(anime.getTitle());

            AutofitTextView genres = (AutofitTextView)findViewById(R.id.genrestext);
            genres.setText(anime.getGenres());

            TextView desc = (TextView)findViewById(R.id.desc);
            desc.setText(anime.getDesc());


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




    public void goback(View v) {
        finish();
    }


}
