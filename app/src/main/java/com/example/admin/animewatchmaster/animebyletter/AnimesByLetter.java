package com.example.admin.animewatchmaster.animebyletter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;

import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.animeinfo.AnimeInfo;
import com.example.admin.animewatchmaster.databaseUtils.DBHelper;
import com.example.admin.animewatchmaster.model.Anime;
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.SlideInEffect;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by abraham on 10/6/2016.
 */
public class AnimesByLetter extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_anime_by_letter);

        String letter = (String)getIntent().getSerializableExtra("letter");

        List<Anime> animeList = DBHelper.getInstance(getApplicationContext()).getAllAnimeByLetter(letter);

        final JazzyListView jazzyListView = (JazzyListView)findViewById(R.id.letterlist);
        jazzyListView.setTransitionEffect(new SlideInEffect());

        AnimeLetterAdapter animeLetterAdapter = new AnimeLetterAdapter(getApplicationContext(),animeList);
        jazzyListView.setAdapter(animeLetterAdapter);

        jazzyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View v, int position, long id) {

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
                },500);

                Anime anime = (Anime) parent.getItemAtPosition(position);
                Intent intent = new Intent(AnimesByLetter.this, AnimeInfo.class);
                intent.putExtra("anime", anime);
                startActivity(intent);

            }
        });

    }


}
