package com.example.admin.animewatchmaster.animebyletter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;

import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.databaseUtils.DBHelper;
import com.example.admin.animewatchmaster.model.Anime;
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.SlideInEffect;

import java.util.List;

/**
 * Created by abraham on 9/6/2016.
 */
public class AnimeByLetter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_letters);

        String letter = (String) getIntent().getSerializableExtra("letter");

        List<Anime> animes = DBHelper.getInstance(getApplicationContext()).getAllAnimeByLetter(letter);
        JazzyListView listView = (JazzyListView)findViewById(R.id.listanimeletter);
        listView.setTransitionEffect(new SlideInEffect());

        AnimeLetterAdapter animeLetterAdapter = new AnimeLetterAdapter(getApplicationContext(),animes);
        listView.setAdapter(animeLetterAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Anime anime = (Anime)parent.getItemAtPosition(position);

                //do stuff!

            }
        });

    }

}
