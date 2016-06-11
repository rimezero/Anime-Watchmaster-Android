package com.example.admin.animewatchmaster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by abraham on 10/6/2016.
 */
public class Utils {


    public static List<String> getAllGenres() {

        List<String> genres = new ArrayList<>();

        genres.add("Action");
        genres.add("Comedy");
        genres.add("Fantasy");
        genres.add("Kids");
        genres.add("Military");
        genres.add("Police");
        genres.add("Seinen");
        genres.add("Space");
        genres.add("Adventure");
        genres.add("Demons");
        genres.add("Game");
        genres.add("Romance");
        genres.add("Movie");
        genres.add("Psychological");
        genres.add("Shoujo");
        genres.add("Sports");
        genres.add("Animation");
        genres.add("Drama");
        genres.add("Harem");
        genres.add("Magic");
        genres.add("Music");
        genres.add("Samurai");
        genres.add("Shounen");
        genres.add("Super Power");
        genres.add("Bishounen");
        genres.add("Ecchi");
        genres.add("Historical");
        genres.add("Martial Arts");
        genres.add("Mystery");
        genres.add("School");
        genres.add("Shounen Ai");
        genres.add("Supernatural");
        genres.add("Cartoon");
        genres.add("English");
        genres.add("Horror");
        genres.add("Mecha");
        genres.add("Parody");
        genres.add("Sci-Fi");
        genres.add("Slice of Life");
        genres.add("Vampire");

        Collections.sort(genres);

        return genres;

    }


    public static String[] getAllGenresArr() {
        List<String> genres = getAllGenres();
        String[] genresArr = new String[genres.size()];

        for(int i =0; i < genres.size(); i++) {
            genresArr[i]  = genres.get(i);
        }

        Collections.sort(genres);

        return genresArr;
    }



}
