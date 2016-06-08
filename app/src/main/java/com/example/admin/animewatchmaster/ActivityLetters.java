package com.example.admin.animewatchmaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;

import com.example.admin.animewatchmaster.animebyletter.AnimeByLetter;
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.SlideInEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abraham on 9/6/2016.
 */
public class ActivityLetters extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_anime_by_letter);

        List<String> letters = getAllLetters();

        JazzyListView listView = (JazzyListView)findViewById(R.id.letterlist);
        listView.setTransitionEffect(new SlideInEffect());

        LettersAdapter lettersAdapter = new LettersAdapter(getApplicationContext(),letters);
        listView.setAdapter(lettersAdapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String letter = (String) parent.getItemAtPosition(position);

                Intent intent = new Intent(ActivityLetters.this, AnimeByLetter.class);
                intent.putExtra("letter",letter);
                startActivity(intent);


            }
        });

    }





    private List<String> getAllLetters() {
        List<String> letters = new ArrayList<>();
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        for(char c : alphabet) {
            letters.add(String.valueOf(c));
        }
        return letters;
    }

}
