package com.example.admin.animewatchmaster.animeinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.model.Anime;
import com.squareup.picasso.Picasso;

/**
 * Created by abraham on 9/6/2016.
 */
public class AnimeInfo extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_animeinfo);

        Anime anime = (Anime) getIntent().getSerializableExtra("anime");

        if(anime != null) {

            TextView title = (TextView)findViewById(R.id.title);
            title.setText(anime.getTitle());

            ImageView imageView = (ImageView)findViewById(R.id.image);

            try {

                Picasso.with(getApplicationContext())
                        .load(anime.getImgurl())
                        .into(imageView);

            } catch (Exception ex) {

            }

            TextView desc = (TextView)findViewById(R.id.desc);
            desc.setText(anime.getDescription());


        } else {
            finish();
        }


    }


}
