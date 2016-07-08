package com.example.admin.animewatchmaster.utils.databaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.admin.animewatchmaster.model.Anime;
import com.example.admin.animewatchmaster.model.SeasonModel;
import com.example.admin.animewatchmaster.model.SeasonsSortModel;
import com.example.admin.animewatchmaster.model.TopanimeModel;
import com.example.admin.animewatchmaster.model.WatchListModel;
import com.example.admin.animewatchmaster.model.WatchedModel;
import com.example.admin.animewatchmaster.model.WatchlaterlistModel;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;


/**
 * Created by admin on 4/11/2016.
 */
public class DBHelper extends SQLiteAssetHelper {

    //dbhelper
    public static final String DATABASE_NAME = "anime.db";
    public static final int DATABASE_VERSION = 1;
    private static final String CLASS_TAG = "DBHelper - ";

    //genaral
    private static final String GENERAL_COLUMN_ID = "id";

    //animelinks
    private static final String ANIMELINKS_COLUMN_ANIMEFREAKLINK = "frlink";
    private static final String ANIMELINKS_COLUMN_ANIMEULTIMALINK = "ultimalink";
    private static final String ANIMELINKS_COLUMN_MALLINK = "MALlink";

    //animeinfo
    private static final String ANIMEINFO_COLUMN_TITLE = "title";
    private static final String ANIMEINFO_COLUMN_IMGURL = "imgurl";
    private static final String ANIMEINFO_COLUMN_GENRE = "genre";
    private static final String ANIMEINFO_COLUMN_EPISODES = "episodes";
    private static final String ANIMEINFO_COLUMN_ANIMETYPE = "animetype";
    private static final String ANIMEINFO_COLUMN_AGERATING = "agerating";
    private static final String ANIMEINFO_COLUMN_DESCRIPTION = "description";

    //APanimeinfo
    private static final String AP_ANIMEINFO_COLUMN_ANIMEINFOID = "id_animeinfo";
    private static final String AP_ANIMEINFO_COLUMN_TITLE = "title";
    private static final String AP_ANIMEINFO_COLUMN_SEASON = "season";
    private static final String AP_ANIMEINFO_COLUMN_IMGURL = "imgurl";
    private static final String AP_ANIMEINFO_COLUMN_GENRE = "genre";
    private static final String AP_ANIMEINFO_COLUMN_ANIMETYPE = "animetype";
    private static final String AP_ANIMEINFO_COLUMN_DESCRIPTION = "description";
    private static final String AP_ANIMEINFO_COLUMN_RATING = "rating";

    //APcurrentseason
    private static final String AP_CURRENTSEASON_COLUMN_SEASON = "season";

    //watchlist
    private static final String WATCHLIST_COLUMN_EPISODESWATCHED = "episodeswatched";
    private static final String WATCHLIST_COLUMN_CURRENTEPISODE = "currentepisode";
    private static final String WATCHLIST_COLUMN_LASTUPDATED = "lastupdated";

    //MALtopanime
    private static final String MAL_TOPANIME_COLUMN_SPOT = "spot";
    private static final String MAL_TOPANIME_COLUMN_SCORE = "score";

    //version
    private static final String VERSION_COLUMN_VERSION = "version";

    //APversion
    private static final String AP_VERSION_COLUMN_VERSION = "version";

    //tables
    private static final String TABLE_ANIMELINKS = "animelinks";
    private static final String TABLE_ANIMEINFO = "animeinfo";
    private static final String TABLE_WATCHLIST = "watchlist";
    private static final String TABLE_WATCHLATER = "watchlaterlist";
    private static final String TABLE_WATCHED = "watchedlist";
    private static final String TABLE_AP_ANIMEINFO = "APanimeinfo";
    private static final String TABLE_HOTANIME = "hotanime";
    private static final String TABLE_MAL_TOPANIME = "MALtopanime";
    private static final String TABLE_VERSION = "version";
    private static final String TABLE_AP_VERSION = "APversion";
    private static final String TABLE_AP_CURRENTSEASON = "APcurrentseason";

    private static volatile DBHelper dbHelper;


    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //double check locking singleton
    public static DBHelper getInstance(Context context) {
        if(dbHelper == null) {
            synchronized (DBHelper.class) {
                if(dbHelper == null) {
                    dbHelper = new DBHelper(context);
                    return dbHelper;
                }
            }
        }
        return dbHelper;
    }



    /*
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table if not exists "+TABLE_ANIMELINKS+
                        "("+ GENERAL_COLUMN_ID +" integer primary key, "+ ANIMELINKS_COLUMN_ANIMEFREAKLINK +" text, "+ ANIMELINKS_COLUMN_ANIMEULTIMALINK +" text, "+ANIMELINKS_COLUMN_MALLINK+" text)"
        );
        db.execSQL(
                "create table if not exists "+TABLE_ANIMEINFO+
                        "("+ GENERAL_COLUMN_ID +" integer primary key, "+ ANIMEINFO_COLUMN_TITLE +" text, "+ ANIMEINFO_COLUMN_IMGURL +" text, "+ ANIMEINFO_COLUMN_GENRE +" text, "+ ANIMEINFO_COLUMN_EPISODES +" text, "+ ANIMEINFO_COLUMN_ANIMETYPE +" text, "+ ANIMEINFO_COLUMN_AGERATING +" text, "+ ANIMEINFO_COLUMN_DESCRIPTION +" text)"
        );
        db.execSQL(
                "create table if not exists "+TABLE_AP_ANIMEINFO+
                        "("+ GENERAL_COLUMN_ID +" integer primary key, "+ AP_ANIMEINFO_COLUMN_ANIMEINFOID +" integer, "+ AP_ANIMEINFO_COLUMN_TITLE +" text, "+ AP_ANIMEINFO_COLUMN_SEASON +" text, "+ AP_ANIMEINFO_COLUMN_IMGURL +" text, "+ AP_ANIMEINFO_COLUMN_GENRE +" text, "+ AP_ANIMEINFO_COLUMN_ANIMETYPE +" text, "+ AP_ANIMEINFO_COLUMN_DESCRIPTION +" text, "+ AP_ANIMEINFO_COLUMN_RATING +" real)"
        );
        db.execSQL(
                "create table if not exists "+TABLE_WATCHLIST+
                        "("+ GENERAL_COLUMN_ID +" integer primary key, "+ WATCHLIST_COLUMN_EPISODESWATCHED +" integer, "+ WATCHLIST_COLUMN_CURRENTEPISODE +" integer, "+ WATCHLIST_COLUMN_LASTUPDATED +" text)"
        );
        db.execSQL(
                "create table if not exists "+TABLE_WATCHLATER+
                        "("+ GENERAL_COLUMN_ID +" integer primary key)"
        );
        db.execSQL(
                "create table if not exists "+TABLE_WATCHED+
                        "("+ GENERAL_COLUMN_ID +" integer primary key)"
        );
        db.execSQL(
                "create table if not exists "+TABLE_HOTANIME+
                        "("+ GENERAL_COLUMN_ID +" integer primary key)"
        );
        db.execSQL(
                "create table if not exists "+TABLE_MAL_TOPANIME+
                        "("+ GENERAL_COLUMN_ID +" integer, "+MAL_TOPANIME_COLUMN_SPOT+" integer primary key, "+MAL_TOPANIME_COLUMN_SCORE+" real)"
        );
        db.execSQL(
                "create table if not exists "+TABLE_VERSION+
                        "("+ VERSION_COLUMN_VERSION +" integer primary key)"
        );
        db.execSQL(
                "create table if not exists "+TABLE_AP_VERSION+
                        "("+ AP_VERSION_COLUMN_VERSION +" integer primary key)"
        );
        db.execSQL(
                "create table if not exists "+TABLE_AP_CURRENTSEASON+
                        "("+ AP_CURRENTSEASON_COLUMN_SEASON +" text)"
        );
        ContentValues contentValues = new ContentValues();
        contentValues.put(VERSION_COLUMN_VERSION, 0);
        db.insert(TABLE_VERSION, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put(AP_VERSION_COLUMN_VERSION, 0);
        db.insert(TABLE_AP_VERSION, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put(AP_CURRENTSEASON_COLUMN_SEASON,"na");
        db.insert(TABLE_AP_CURRENTSEASON, null, contentValues);
    }*/

    //kanw drop ta pada gia testing
    /*
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANIMELINKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANIMEINFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WATCHLIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WATCHLATER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOTANIME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VERSION);
        onCreate(db);

        switch (oldVersion){
            case 1:
                db.execSQL(
                        "create table if not exists "+TABLE_WATCHED+
                                "("+ GENERAL_COLUMN_ID +" integer primary key)"
                );
            case 2:
                db.execSQL(
                        "alter table "+TABLE_ANIMELINKS+" add column "+ANIMELINKS_COLUMN_MALLINK+" text"
                );
                db.execSQL(
                        "create table if not exists "+TABLE_MAL_TOPANIME+
                                "("+ GENERAL_COLUMN_ID +" integer, "+MAL_TOPANIME_COLUMN_SPOT+" integer primary key, "+MAL_TOPANIME_COLUMN_SCORE+" real)"
                );
                ContentValues contentValues = new ContentValues();
                contentValues.put(VERSION_COLUMN_VERSION, 0);
                db.update(TABLE_VERSION,contentValues,null,null);
            case 3:
                db.execSQL(
                        "create table if not exists "+TABLE_AP_ANIMEINFO+
                                "("+ GENERAL_COLUMN_ID +" integer primary key, "+ AP_ANIMEINFO_COLUMN_ANIMEINFOID +" integer, "+ AP_ANIMEINFO_COLUMN_TITLE +" text, "+ AP_ANIMEINFO_COLUMN_SEASON +" text, "+ AP_ANIMEINFO_COLUMN_IMGURL +" text, "+ AP_ANIMEINFO_COLUMN_GENRE +" text, "+ AP_ANIMEINFO_COLUMN_ANIMETYPE +" text, "+ AP_ANIMEINFO_COLUMN_DESCRIPTION +" text, "+ AP_ANIMEINFO_COLUMN_RATING +" real)"
                );
                db.execSQL(
                        "create table if not exists "+TABLE_AP_VERSION+
                                "("+ AP_VERSION_COLUMN_VERSION +" integer primary key)"
                );
                contentValues = new ContentValues();
                contentValues.put(AP_VERSION_COLUMN_VERSION, 0);
                db.insert(TABLE_AP_VERSION, null, contentValues);
            case 4:
                db.execSQL(
                        "create table if not exists "+TABLE_AP_CURRENTSEASON+
                                "("+ AP_CURRENTSEASON_COLUMN_SEASON +" text)"
                );
                contentValues = new ContentValues();
                contentValues.put(AP_CURRENTSEASON_COLUMN_SEASON,"na");
                db.insert(TABLE_AP_CURRENTSEASON, null, contentValues);
            default:
                //you know ;p

        }

    }*/

    /*
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANIMELINKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANIMEINFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WATCHLIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WATCHLATER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOTANIME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VERSION);
        onCreate(db);
    }*/


    public boolean insertIntoAnimelinks(int id,String frlink,String ultimalink,String MALlink){
        final String TAG = CLASS_TAG+"insertAnime";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GENERAL_COLUMN_ID, id);
        contentValues.put(ANIMELINKS_COLUMN_ANIMEFREAKLINK, frlink);
        contentValues.put(ANIMELINKS_COLUMN_ANIMEULTIMALINK, ultimalink);
        contentValues.put(ANIMELINKS_COLUMN_MALLINK,MALlink);
        long result = db.insert(TABLE_ANIMELINKS, null, contentValues);
        if(result==-1){
            Log.i(TAG,"insert of links for anime with ID "+id+" failed");
            return false;
        }
        Log.d(TAG, "inserted links for anime with id: " + id);
        return true;
    }

    public boolean insertIntoAnimeinfo(String title, String imgurl, String genre, String episodes, String animetype, String agerating, String description){

        final String TAG = CLASS_TAG+"insertAnime";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ANIMEINFO_COLUMN_TITLE,title);
        contentValues.put(ANIMEINFO_COLUMN_IMGURL,imgurl);
        contentValues.put(ANIMEINFO_COLUMN_GENRE,genre);
        contentValues.put(ANIMEINFO_COLUMN_EPISODES, episodes);
        contentValues.put(ANIMEINFO_COLUMN_ANIMETYPE, animetype);
        contentValues.put(ANIMEINFO_COLUMN_AGERATING, agerating);
        contentValues.put(ANIMEINFO_COLUMN_DESCRIPTION, description);
        long result = db.insert(TABLE_ANIMEINFO, null, contentValues);
        if(result==-1){
            Log.i(TAG,"insert of anime with title "+title+" failed");
            return false;
        }
        Log.d(TAG, "inserted anime " + title);
        return true;
    }

    /**
     *
     * @param animeinfo_id The anime id reference to animeinfo table or -1 if the anime does not exist in animeinfo
     * @param title ...
     * @param season ...
     * @param imgurl ...
     * @param genre ...
     * @param animetype ...
     * @param description ...
     * @param rating maximum rating is 5.0 can be -1 if it was not found in imported data
     * @return The new row id or -1 if an error occured.
     */
    public boolean insertIntoAPAnimeinfo(int animeinfo_id,String title, String season, String imgurl, String genre, String animetype, String description, Double rating){

        final String TAG = CLASS_TAG+"insrAPAnime";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(AP_ANIMEINFO_COLUMN_ANIMEINFOID,animeinfo_id);
        contentValues.put(AP_ANIMEINFO_COLUMN_TITLE,title);
        contentValues.put(AP_ANIMEINFO_COLUMN_SEASON,season);
        contentValues.put(AP_ANIMEINFO_COLUMN_IMGURL,imgurl);
        contentValues.put(AP_ANIMEINFO_COLUMN_GENRE, genre);
        contentValues.put(AP_ANIMEINFO_COLUMN_ANIMETYPE, animetype);
        contentValues.put(AP_ANIMEINFO_COLUMN_DESCRIPTION, description);
        contentValues.put(AP_ANIMEINFO_COLUMN_RATING, rating);
        long result = db.insert(TABLE_AP_ANIMEINFO, null, contentValues);
        if(result==-1){
            Log.i(TAG,"insert of anime with title "+title+" failed");
            return false;
        }
        Log.d(TAG, "inserted anime " + title);
        return true;
    }

    public boolean insertIntoWatchlist(int id, int episodeswatched, int currentepisode, String lastupdated){

        final String TAG = CLASS_TAG+"insertIntoWatchlist";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GENERAL_COLUMN_ID,id);
        contentValues.put(WATCHLIST_COLUMN_EPISODESWATCHED,episodeswatched);
        contentValues.put(WATCHLIST_COLUMN_CURRENTEPISODE,currentepisode);
        contentValues.put(WATCHLIST_COLUMN_LASTUPDATED, lastupdated);
        long result = db.insert(TABLE_WATCHLIST, null, contentValues);
        Log.d("dbhelper - inswatchl", "inserted anime with id: "+id+" in the watchlist");
        if(result==-1){
            Log.i(TAG,"insert of anime with id "+id+" into Watchlist failed");
            return false;
        }
        return true;

    }

    public boolean insertIntoWatchlaterlist(int id){
        final String TAG = CLASS_TAG+"insertIntoWLlist";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GENERAL_COLUMN_ID,id);
        long result = db.insert(TABLE_WATCHLATER, null, contentValues);
        Log.d("dbhelper - inswatchlt", "inserted anime with id: "+id+" in the watchlaterlist");
        if(result==-1){
            Log.i(TAG,"insert of anime with id "+id+" into watchlaterlist failed");
            return false;
        }
        return true;
    }

    public boolean insertIntoWatchedlist(int id){
        final String TAG = CLASS_TAG+"insertIntoWDlist";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GENERAL_COLUMN_ID,id);
        long result = db.insert(TABLE_WATCHED, null, contentValues);
        Log.d("dbhelper - inswatchlt", "inserted anime with id: "+id+" in the watchedlist");
        if(result==-1){
            Log.i(TAG,"insert of anime with id "+id+" into watchedlist failed");
            return false;
        }
        return true;
    }

    public boolean insertIntoHotanime(int id){
        final String TAG = CLASS_TAG+"insertIntoHotanime";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GENERAL_COLUMN_ID,id);
        long result = db.insert(TABLE_HOTANIME, null, contentValues);
        Log.d("dbhelper - inshotanime", "inserted anime with id: "+id+" in hotanime");
        if(result==-1){
            Log.i(TAG,"insert of anime with id "+id+" into hotanime failed");
            return false;
        }
        return true;
    }

    public boolean insertIntoMALtopanime(int id, int spot, double score){
        final String TAG = CLASS_TAG+"insertIntoMALtopanime";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GENERAL_COLUMN_ID,id);
        contentValues.put(MAL_TOPANIME_COLUMN_SPOT,spot);
        contentValues.put(MAL_TOPANIME_COLUMN_SCORE,score);
        long result = db.insert(TABLE_MAL_TOPANIME, null, contentValues);
        Log.d("dbhelper - instopanime", "inserted anime with id: "+id+" in topanime");
        if(result==-1){
            Log.i(TAG,"insert of anime with id "+id+" into topanime failed");
            return false;
        }
        return true;
    }

    /**
     *
     * @return Returns all of the seasons in the database sorted. May return empty list or less seasons if an error occurs.
     */
    public List<SeasonsSortModel> getSeasons(){
        final String TAG = CLASS_TAG+"getSeasons";
        List<SeasonsSortModel> seasonslist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(true,TABLE_AP_ANIMEINFO,new String[] {AP_ANIMEINFO_COLUMN_SEASON},null,null,null,null,null,null);
        while(res.moveToNext()){
            String season = res.getString(res.getColumnIndex(AP_CURRENTSEASON_COLUMN_SEASON));
            if(season != null && !season.equals("Upcoming")) {
                StringTokenizer seasontokenizer = new StringTokenizer(season, " ");
                if (seasontokenizer.countTokens() != 2) {
                    Log.e(TAG, "Season format is incorrect: " + season);
                    return seasonslist;
                }
                try {
                    seasonslist.add(new SeasonsSortModel(seasontokenizer.nextToken(), Integer.valueOf(seasontokenizer.nextToken())));
                } catch (NumberFormatException e) {
                    Log.e(TAG, "Cannot cast year to integer for season: " + season);
                }
            }
        }
        res.close();
        try {
            Collections.sort(seasonslist);
        }catch (ClassCastException e){
            e.printStackTrace();
            Log.e(TAG,"Cannot sort list");
        }

        /* test result
        for(SeasonsSortModel model : seasonslist){
            System.out.println(model.toString());
        }*/

        return seasonslist;
    }

    /**
     *
     * @param crossCheck True will return the anime that exist in animeinfo. False will return all of the anime of the season from APanimeinfo. If the season is Upcoming this parameter is automatically set to false.
     * @param season The season to search for. Can be null to return all of the seasons.
     * @return An arraylist with the season's data. Rating can be -1 (no rating is available).
     */
    public List<SeasonModel> getSeasonData(boolean crossCheck, String season){
        List<SeasonModel> seasonData = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = new String[] {AP_ANIMEINFO_COLUMN_ANIMEINFOID,AP_ANIMEINFO_COLUMN_TITLE,AP_ANIMEINFO_COLUMN_IMGURL,AP_ANIMEINFO_COLUMN_ANIMETYPE,AP_ANIMEINFO_COLUMN_RATING};
        String whereCluse = AP_ANIMEINFO_COLUMN_SEASON+"=?";
        Cursor res;
        if(season==null){
            res = db.query(TABLE_AP_ANIMEINFO,columns,null,null,null,null,null);
        }else{
            if(season.equals("Upcoming"))
                crossCheck = false;
            res = db.query(TABLE_AP_ANIMEINFO,columns,whereCluse,new String[] {season},null,null,null);
        }

        while(res.moveToNext()){
            if(crossCheck){
                if(res.getInt(res.getColumnIndex(AP_ANIMEINFO_COLUMN_ANIMEINFOID))!=-1){
                    SeasonModel model = new SeasonModel();
                    model.setAnimeinfo_id(res.getInt(res.getColumnIndex(AP_ANIMEINFO_COLUMN_ANIMEINFOID)));
                    model.setTitle(res.getString(res.getColumnIndex(AP_ANIMEINFO_COLUMN_TITLE)));
                    model.setImgurl(res.getString(res.getColumnIndex(AP_ANIMEINFO_COLUMN_IMGURL)));
                    model.setAnimetype(res.getString(res.getColumnIndex(AP_ANIMEINFO_COLUMN_ANIMETYPE)));
                    model.setRating(res.getDouble(res.getColumnIndex(AP_ANIMEINFO_COLUMN_RATING)));
                    seasonData.add(model);
                }
            }else{
                SeasonModel model = new SeasonModel();
                model.setAnimeinfo_id(res.getInt(res.getColumnIndex(AP_ANIMEINFO_COLUMN_ANIMEINFOID)));
                model.setTitle(res.getString(res.getColumnIndex(AP_ANIMEINFO_COLUMN_TITLE)));
                model.setImgurl(res.getString(res.getColumnIndex(AP_ANIMEINFO_COLUMN_IMGURL)));
                model.setAnimetype(res.getString(res.getColumnIndex(AP_ANIMEINFO_COLUMN_ANIMETYPE)));
                model.setRating(res.getDouble(res.getColumnIndex(AP_ANIMEINFO_COLUMN_RATING)));
                seasonData.add(model);
            }

        }

        return seasonData;
    }

    public List<WatchListModel> getWatchlistData() {

        List<WatchListModel> models = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();


        String command = "select W."+ GENERAL_COLUMN_ID +", Info."+ ANIMEINFO_COLUMN_TITLE +", Info."+ ANIMEINFO_COLUMN_IMGURL +", W."+ WATCHLIST_COLUMN_EPISODESWATCHED +", W."+ WATCHLIST_COLUMN_CURRENTEPISODE +", W."+ WATCHLIST_COLUMN_LASTUPDATED +" from "+TABLE_WATCHLIST+" W inner join "+TABLE_ANIMEINFO+" Info on W."+ GENERAL_COLUMN_ID +"=Info."+ GENERAL_COLUMN_ID;
        Cursor c = db.rawQuery(command, null);

        if(c.moveToFirst()) {
            do {

                WatchListModel watchListModel = new WatchListModel();
                watchListModel.setId(c.getInt(c.getColumnIndex(GENERAL_COLUMN_ID)));
                watchListModel.setTitle(c.getString(c.getColumnIndex(ANIMEINFO_COLUMN_TITLE)));
                watchListModel.setImgurl(c.getString(c.getColumnIndex(ANIMEINFO_COLUMN_IMGURL)));
                watchListModel.setCurrentEpisode(c.getInt(c.getColumnIndex(WATCHLIST_COLUMN_CURRENTEPISODE)));
                watchListModel.setEpisodeswatched(c.getInt(c.getColumnIndex(WATCHLIST_COLUMN_EPISODESWATCHED)));
                watchListModel.setLastupdated(c.getString(c.getColumnIndex(WATCHLIST_COLUMN_LASTUPDATED)));

                models.add(watchListModel);

            } while (c.moveToNext());
        }

        c.close();

        return models;
    }

    public List<WatchlaterlistModel> getWatchlaterlistData() {

        List<WatchlaterlistModel> models = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String command = "select W."+ GENERAL_COLUMN_ID +", Info."+ ANIMEINFO_COLUMN_TITLE +", Info."+ ANIMEINFO_COLUMN_IMGURL +", Info."+ ANIMEINFO_COLUMN_GENRE +" from "+TABLE_WATCHLATER+" W inner join "+TABLE_ANIMEINFO+" Info on W."+ GENERAL_COLUMN_ID +"=Info."+ GENERAL_COLUMN_ID+" order by Info."+ANIMEINFO_COLUMN_TITLE+" collate nocase asc";
        Cursor c = db.rawQuery(command,null);

        if(c.moveToFirst()) {
            do {

                WatchlaterlistModel watchlaterlistModel = new WatchlaterlistModel();

                watchlaterlistModel.setId(c.getInt(c.getColumnIndex(GENERAL_COLUMN_ID)));
                watchlaterlistModel.setTitle(c.getString(c.getColumnIndex(ANIMEINFO_COLUMN_TITLE)));
                watchlaterlistModel.setImgurl(c.getString(c.getColumnIndex(ANIMEINFO_COLUMN_IMGURL)));
                watchlaterlistModel.setGenre(c.getString(c.getColumnIndex(ANIMEINFO_COLUMN_GENRE)));

                models.add(watchlaterlistModel);

            } while (c.moveToNext());
        }

        c.close();

        return models;
    }

    public List<WatchedModel> getWatchedListData() {

        List<WatchedModel> models = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String command = "select W."+ GENERAL_COLUMN_ID +", Info."+ ANIMEINFO_COLUMN_TITLE +", Info."+ ANIMEINFO_COLUMN_IMGURL +", Info."+ ANIMEINFO_COLUMN_GENRE +" from "+TABLE_WATCHED+" W inner join "+TABLE_ANIMEINFO+" Info on W."+ GENERAL_COLUMN_ID +"=Info."+ GENERAL_COLUMN_ID+" order by Info."+ANIMEINFO_COLUMN_TITLE+" collate nocase asc";
        Cursor c = db.rawQuery(command,null);

        if(c.moveToFirst()) {
            do {

                WatchedModel watchedmodel = new WatchedModel();

                watchedmodel.setId(c.getInt(c.getColumnIndex(GENERAL_COLUMN_ID)));
                watchedmodel.setTitle(c.getString(c.getColumnIndex(ANIMEINFO_COLUMN_TITLE)));
                watchedmodel.setImgurl(c.getString(c.getColumnIndex(ANIMEINFO_COLUMN_IMGURL)));
                watchedmodel.setGenre(c.getString(c.getColumnIndex(ANIMEINFO_COLUMN_GENRE)));

                models.add(watchedmodel);

            } while (c.moveToNext());
        }

        c.close();

        return models;
    }

    //returns the hot anime data including title imgurl and genre as WatchlaterlistModels
    public List<WatchlaterlistModel> getHotAnimeData() {

        List<WatchlaterlistModel> models = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String command = "select W."+ GENERAL_COLUMN_ID +", Info."+ ANIMEINFO_COLUMN_TITLE +", Info."+ ANIMEINFO_COLUMN_IMGURL +", Info."+ ANIMEINFO_COLUMN_GENRE +" from "+TABLE_HOTANIME+" W inner join "+TABLE_ANIMEINFO+" Info on W."+ GENERAL_COLUMN_ID +"=Info."+ GENERAL_COLUMN_ID;
        Cursor c = db.rawQuery(command,null);

        if(c.moveToFirst()) {
            do {

                WatchlaterlistModel watchlaterlistModel = new WatchlaterlistModel();

                watchlaterlistModel.setId(c.getInt(c.getColumnIndex(GENERAL_COLUMN_ID)));
                watchlaterlistModel.setTitle(c.getString(c.getColumnIndex(ANIMEINFO_COLUMN_TITLE)));
                watchlaterlistModel.setImgurl(c.getString(c.getColumnIndex(ANIMEINFO_COLUMN_IMGURL)));
                watchlaterlistModel.setGenre(c.getString(c.getColumnIndex(ANIMEINFO_COLUMN_GENRE)));

                models.add(watchlaterlistModel);

            } while (c.moveToNext());
        }

        c.close();

        return models;
    }

    public List<TopanimeModel> getTopAnimeData(){
        List<TopanimeModel> models = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.query(TABLE_MAL_TOPANIME+" T inner join "+TABLE_ANIMEINFO+" A on T."+GENERAL_COLUMN_ID+"="+"A."+GENERAL_COLUMN_ID,new String[] {"T."+MAL_TOPANIME_COLUMN_SPOT,"T."+GENERAL_COLUMN_ID,"T."+MAL_TOPANIME_COLUMN_SCORE,"A."+ANIMEINFO_COLUMN_TITLE,"A."+ANIMEINFO_COLUMN_IMGURL},null,null,null,null,null);
        while(res.moveToNext()){
            TopanimeModel model = new TopanimeModel();
            model.setSpot(res.getInt(res.getColumnIndex(MAL_TOPANIME_COLUMN_SPOT)));
            model.setId(res.getInt(res.getColumnIndex(GENERAL_COLUMN_ID)));
            model.setScore(res.getDouble(res.getColumnIndex(MAL_TOPANIME_COLUMN_SCORE)));
            model.setTitle(res.getString(res.getColumnIndex(ANIMEINFO_COLUMN_TITLE)));
            model.setImgurl(res.getString(res.getColumnIndex(ANIMEINFO_COLUMN_IMGURL)));
            models.add(model);
        }

        res.close();
        return models;
    }



    public int getVersion(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from " + TABLE_VERSION, null);
        int version = -1;
        if(res.moveToFirst()){
            version = res.getInt(res.getColumnIndex(VERSION_COLUMN_VERSION));
        }
        res.close();
        return version;
    }

    public int getAPVersion(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from " + TABLE_AP_VERSION, null);
        int version = -1;
        if(res.moveToFirst()){
            version = res.getInt(res.getColumnIndex(AP_VERSION_COLUMN_VERSION));
        }
        res.close();
        return version;
    }

    public String getCurrentSeason(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from " + TABLE_AP_CURRENTSEASON, null);
        String currentSeason = "na";
        if(res.moveToFirst()){
            currentSeason = res.getString(res.getColumnIndex(AP_CURRENTSEASON_COLUMN_SEASON));
        }
        res.close();
        return currentSeason;
    }

    public Anime getAnimeInfo(int id) {

        SQLiteDatabase db = this.getReadableDatabase();
        String projection[] = {GENERAL_COLUMN_ID, ANIMEINFO_COLUMN_TITLE, ANIMEINFO_COLUMN_IMGURL, ANIMEINFO_COLUMN_GENRE, ANIMEINFO_COLUMN_EPISODES, ANIMEINFO_COLUMN_ANIMETYPE, ANIMEINFO_COLUMN_AGERATING, ANIMEINFO_COLUMN_DESCRIPTION};
        Cursor res =  db.query(TABLE_ANIMEINFO, projection, "id = ?", new String[]{String.valueOf(id)}, null, null, null);

        Anime anime = new Anime();

        if(res.moveToFirst()) {
            anime.setId(res.getInt(0));
            anime.setTitle(res.getString(1));
            anime.setImgurl(res.getString(2));
            anime.setGenre(res.getString(3));
            anime.setEpisodes(res.getString(4));
            anime.setAnimetype(res.getString(5));
            anime.setAgerating(res.getString(6));
            anime.setDescription(res.getString(7));
        }

        res.close();

        return anime;
    }

    /**
     *
     * @param title The title of the anime
     * @return The id of the anime or -1 if the anime does not exist in the database
     */
    public int getAnimeID(String title){
        SQLiteDatabase db = this.getReadableDatabase();
        String command = "select "+ GENERAL_COLUMN_ID +" from "+TABLE_ANIMEINFO+" where "+ ANIMEINFO_COLUMN_TITLE +"=? collate nocase";
        Cursor res =  db.rawQuery(command, new String[]{title});
        int id=-1;
        if(res.moveToFirst()) {
            id = res.getInt(res.getColumnIndex(GENERAL_COLUMN_ID));
        }
        res.close();
        return id;
    }

    /**
     *
     * @param title The title of the anime
     * @return The id of the anime or -1 if the anime does not exist in APanimeinfo
     */
    public int getAPAnimeID(String title){
        SQLiteDatabase db = this.getReadableDatabase();
        String command = "select "+ GENERAL_COLUMN_ID +" from "+TABLE_AP_ANIMEINFO+" where "+ AP_ANIMEINFO_COLUMN_TITLE +"=? collate nocase";
        Cursor res =  db.rawQuery(command, new String[]{title});
        int id=-1;
        if(res.moveToFirst()) {
            id = res.getInt(res.getColumnIndex(GENERAL_COLUMN_ID));
        }
        res.close();
        return id;
    }

    /**
     *
     * @param title The title of the anime.
     * @return The id of the anime. -1 if the anime does not exist in the watchlist or -2 if the anime does not exist in the database
     */
    public int getWatchlistID(String title){
        SQLiteDatabase db = this.getReadableDatabase();
        String command = "select "+ GENERAL_COLUMN_ID +" from "+TABLE_ANIMEINFO+" where "+ ANIMEINFO_COLUMN_TITLE +"=?";
        Cursor res = db.rawQuery(command, new String[] {title} );

        if(res.moveToFirst()){
            int id = res.getInt(res.getColumnIndex(GENERAL_COLUMN_ID));
            command = "select "+ GENERAL_COLUMN_ID +" from "+TABLE_WATCHLIST+" where "+ GENERAL_COLUMN_ID +"=?";
            Cursor res2 = db.rawQuery(command, new String[] {String.valueOf(id)});

            res.close();
            if(res2.getCount()>0) {
                res2.close();
                return id;
            }
            else{
                res2.close();
                return -1;
            }
        }else{
            res.close();
            Log.i("DBHelper - ", " checkIfExistsInWatchlist: title - "+title+" does not exists in animeinfo table");
            return -2;
        }
    }

    public String getAnimefreakLink(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String command = "select "+ ANIMELINKS_COLUMN_ANIMEFREAKLINK +" from "+TABLE_ANIMELINKS+" where "+ GENERAL_COLUMN_ID +"=?";
        Cursor res =  db.rawQuery(command, new String[]{String.valueOf(id)});
        String link = "na";
        if(res.moveToFirst()){
            link = res.getString(res.getColumnIndex(ANIMELINKS_COLUMN_ANIMEFREAKLINK));
        }else{
            Log.i("dbhelper - animefrlink","Could not find a link for the anime with id: "+id);
        }
        res.close();
        return link;
    }

    public String getAnimeultimaLink(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String command = "select "+ ANIMELINKS_COLUMN_ANIMEULTIMALINK +" from "+TABLE_ANIMELINKS+" where "+ GENERAL_COLUMN_ID +"=?";
        Cursor res =  db.rawQuery(command, new String[]{String.valueOf(id)});
        String link = "na";
        if(res.moveToFirst()){
            link = res.getString(res.getColumnIndex(ANIMELINKS_COLUMN_ANIMEULTIMALINK));
        }else{
            Log.i("dbhelper - animeultlink","Could not find a link for the anime with id: "+id);
        }
        res.close();
        return link;
    }

    /**
     *
     * @param anime The anime to search for anime with same genres
     * @return An arraylist of anime that contain all of the genres of the parameter anime (results may have more genres)
     */
    public ArrayList<Anime> getAnimeWithSameGenre(Anime anime){
        ArrayList<Anime> animelist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder whereClause = new StringBuilder();
        whereClause.append(ANIMEINFO_COLUMN_GENRE+ " like ?");
        StringTokenizer animegenres = new StringTokenizer(anime.getGenre().replace(" ",""),",");

        if(animegenres.countTokens()<1){
            Log.i("DBHelper - getAnmGnr", " Anime with id: "+anime.getId()+" has no genres");
            return animelist;
        }

        String[] whereArgs = new String[animegenres.countTokens()];
        whereArgs[0] = "%"+animegenres.nextToken()+"%";

        for(int i=1; i<whereArgs.length; i++){
            whereClause.append(" and "+ANIMEINFO_COLUMN_GENRE+ " like ?");
            whereArgs[i] = "%"+animegenres.nextToken()+"%";
        }

        Cursor res = db.query(TABLE_ANIMEINFO,new String[] {GENERAL_COLUMN_ID,ANIMEINFO_COLUMN_TITLE,ANIMEINFO_COLUMN_IMGURL,ANIMEINFO_COLUMN_GENRE,ANIMEINFO_COLUMN_EPISODES,ANIMEINFO_COLUMN_ANIMETYPE,ANIMEINFO_COLUMN_AGERATING,ANIMEINFO_COLUMN_DESCRIPTION},whereClause.toString(),whereArgs,null,null,null);
        while(res.moveToNext()){
            Anime newAnime = new Anime();
            newAnime.setId(res.getInt(res.getColumnIndex(GENERAL_COLUMN_ID)));
            newAnime.setTitle(res.getString(res.getColumnIndex(ANIMEINFO_COLUMN_TITLE)));
            newAnime.setImgurl(res.getString(res.getColumnIndex(ANIMEINFO_COLUMN_IMGURL)));
            newAnime.setGenre(res.getString(res.getColumnIndex(ANIMEINFO_COLUMN_GENRE)));
            newAnime.setEpisodes(res.getString(res.getColumnIndex(ANIMEINFO_COLUMN_EPISODES)));
            newAnime.setAnimetype(res.getString(res.getColumnIndex(ANIMEINFO_COLUMN_ANIMETYPE)));
            newAnime.setAgerating(res.getString(res.getColumnIndex(ANIMEINFO_COLUMN_AGERATING)));
            newAnime.setDescription(res.getString(res.getColumnIndex(ANIMEINFO_COLUMN_DESCRIPTION)));
            animelist.add(newAnime);
        }
        res.close();

        return animelist;
    }

    /**
     *
     * @param commandType - 0 ByLetter, 1 ByLetter with filters, 2 search, 3, search with filters
     * @param searchparam - either the letter or the string to search for
     * @param filterslist - either null or a list with the filters as Strings of this type "Action"
     * @return An arraylist with Anime objects according to the parameters above
     */
    public ArrayList<Anime> getAllAnime(int commandType, String searchparam, ArrayList<String> filterslist){
        ArrayList<Anime> allanime = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder command = new StringBuilder();

        searchparam = searchparam+"%";

        switch (commandType){
            case 0:
                command.append("select * from animeinfo where "+ ANIMEINFO_COLUMN_TITLE +" like ? order by "+ ANIMEINFO_COLUMN_TITLE +" collate nocase asc");
                break;
            case 1:
                command.append("select * from animeinfo where "+ ANIMEINFO_COLUMN_TITLE +" like ?");

                if(filterslist!=null) {
                    for (String filter : filterslist) {
                        command.append(" and " + ANIMEINFO_COLUMN_GENRE + " like '%" + filter + "%'");
                    }
                }else{
                    Log.i(CLASS_TAG+"getAllAnime"," commandType is search by letter WITH FILTERS but filterslist is null");
                }

                command.append(" order by "+ ANIMEINFO_COLUMN_TITLE +" collate nocase asc");
                break;
            case 2:
                command.append("select * from animeinfo where "+ ANIMEINFO_COLUMN_TITLE +" like ? order by "+ ANIMEINFO_COLUMN_TITLE +" collate nocase asc");
                searchparam = "%"+searchparam;
                break;
            case 3:
                command.append("select * from animeinfo where "+ ANIMEINFO_COLUMN_TITLE +" like ?");

                if(filterslist!=null) {
                    for (String filter : filterslist) {
                        command.append(" and " + ANIMEINFO_COLUMN_GENRE + " like '%" + filter + "%'");
                    }
                }else{
                    Log.i(CLASS_TAG+"getAllAnime"," commandType is search WITH FILTERS but filterslist is null");
                }

                command.append(" order by "+ ANIMEINFO_COLUMN_TITLE +" collate nocase asc");
                searchparam = "%"+searchparam;
                break;
            default:
                command.append("select * from animeinfo where "+ ANIMEINFO_COLUMN_TITLE +" like ? order by "+ ANIMEINFO_COLUMN_TITLE +" collate nocase asc");
                Log.i(CLASS_TAG+"getAllAnime"," commandType is out of bounds executing search with parameter as default method");

        }

        Log.d(CLASS_TAG+"getAllAnime","executing command: "+command.toString());
        Cursor res = db.rawQuery(command.toString(),new String[] {searchparam});
        res.moveToFirst();

        int id;
        String title;
        String imgurl;
        String genre;
        String episodes;
        String animetype;
        String agerating;
        String description;

        while(!res.isAfterLast()){
            id = res.getInt(res.getColumnIndex(GENERAL_COLUMN_ID));
            title = res.getString(res.getColumnIndex(ANIMEINFO_COLUMN_TITLE));
            imgurl = res.getString(res.getColumnIndex(ANIMEINFO_COLUMN_IMGURL));
            genre = res.getString(res.getColumnIndex(ANIMEINFO_COLUMN_GENRE));
            episodes = res.getString(res.getColumnIndex(ANIMEINFO_COLUMN_EPISODES));
            animetype = res.getString(res.getColumnIndex(ANIMEINFO_COLUMN_ANIMETYPE));
            agerating = res.getString(res.getColumnIndex(ANIMEINFO_COLUMN_AGERATING));
            description = res.getString(res.getColumnIndex(ANIMEINFO_COLUMN_DESCRIPTION));

            allanime.add(new Anime(id,title,imgurl,genre,episodes,animetype,agerating,description));
            res.moveToNext();
        }
        return allanime;
    }

    /**
     *
     * @param tableNum - 0.animeinfo/1.animelinks/2.hotanime/3.watchlist/4.watchlater/5.APanimeinfo/6.MALtopanime/defalt.animeinfo
     * @return - the number of rows of the table
     */
    public int getNumberOfAnime(int tableNum){
        SQLiteDatabase db = this.getReadableDatabase();
        String tablename;
        switch (tableNum){
            case 0:
                tablename = TABLE_ANIMEINFO;
                break;
            case 1:
                tablename = TABLE_ANIMELINKS;
                break;
            case 2:
                tablename = TABLE_HOTANIME;
                break;
            case 3:
                tablename = TABLE_WATCHLIST;
                break;
            case 4:
                tablename = TABLE_WATCHLATER;
                break;
            case 5:
                tablename = TABLE_AP_ANIMEINFO;
                break;
            case 6:
                tablename = TABLE_MAL_TOPANIME;
                break;
            default:
                tablename = TABLE_ANIMEINFO;
        }
        return (int) DatabaseUtils.queryNumEntries(db, tablename);
    }

    public boolean checkIfExistsInAnimeInfo(String title){
        SQLiteDatabase db = this.getReadableDatabase();
        String command = "select title from "+TABLE_ANIMEINFO+" where "+ ANIMEINFO_COLUMN_TITLE +"=?";
        Cursor res =  db.rawQuery( command, new String[] {title} );

        if(res.getCount()>0) {
            res.close();
            return true;
        }
        else{
            res.close();
            return false;
        }

    }

    public boolean checkIfExistsInAPAnimeInfo(String title){
        SQLiteDatabase db = this.getReadableDatabase();
        String command = "select title from "+TABLE_AP_ANIMEINFO+" where "+ AP_ANIMEINFO_COLUMN_TITLE +"=?";
        Cursor res =  db.rawQuery( command, new String[] {title} );

        if(res.getCount()>0) {
            res.close();
            return true;
        }
        else{
            res.close();
            return false;
        }

    }

    public boolean checkIfExistsInAnimelinks(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String command = "select "+ ANIMELINKS_COLUMN_ANIMEFREAKLINK +" from "+TABLE_ANIMELINKS+" where "+ GENERAL_COLUMN_ID +"=?";
        Cursor res =  db.rawQuery( command, new String[] {String.valueOf(id)} );

        if(res.getCount()>0) {
            res.close();
            return true;
        }
        else{
            res.close();
            return false;
        }

    }

    public boolean checkIfExistsInWatchlist(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String command = "select "+ GENERAL_COLUMN_ID +" from "+TABLE_WATCHLIST+" where "+ GENERAL_COLUMN_ID +"=?";
        Cursor res =  db.rawQuery( command, new String[] {String.valueOf(id)} );

        if(res.getCount()>0) {
            res.close();
            return true;
        }
        else{
            res.close();
            return false;
        }
    }

    public boolean checkIfExistsInWatchLaterList(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String command = "select "+ GENERAL_COLUMN_ID +" from "+TABLE_WATCHLATER+" where "+ GENERAL_COLUMN_ID +"=?";
        Cursor res =  db.rawQuery( command, new String[] {String.valueOf(id)} );

        if(res.getCount()>0) {
            res.close();
            return true;
        }
        else{
            res.close();
            return false;
        }
    }

    public boolean checkIfExistsInWatchedList(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String command = "select "+ GENERAL_COLUMN_ID +" from "+TABLE_WATCHED+" where "+ GENERAL_COLUMN_ID +"=?";
        Cursor res =  db.rawQuery( command, new String[] {String.valueOf(id)} );

        if(res.getCount()>0) {
            res.close();
            return true;
        }
        else{
            res.close();
            return false;
        }
    }

    public boolean checkIfExistsInHotanime(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String command = "select "+ GENERAL_COLUMN_ID +" from "+TABLE_HOTANIME+" where "+ GENERAL_COLUMN_ID +"=?";
        Cursor res =  db.rawQuery( command, new String[] {String.valueOf(id)} );

        if(res.getCount()>0) {
            res.close();
            return true;
        }
        else{
            res.close();
            return false;
        }
    }

    public boolean checkIfExistsInMALtopanime(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String command = "select "+ GENERAL_COLUMN_ID +" from "+TABLE_MAL_TOPANIME+" where "+ GENERAL_COLUMN_ID +"=?";
        Cursor res =  db.rawQuery( command, new String[] {String.valueOf(id)} );

        if(res.getCount()>0) {
            res.close();
            return true;
        }
        else{
            res.close();
            return false;
        }
    }

    public boolean checkIfSpotIsFilledInMALtopanime(int spot){
        SQLiteDatabase db = this.getReadableDatabase();
        String command = "select "+ GENERAL_COLUMN_ID +" from "+TABLE_MAL_TOPANIME+" where "+ MAL_TOPANIME_COLUMN_SPOT +"=?";
        Cursor res =  db.rawQuery( command, new String[] {String.valueOf(spot)} );

        if(res.getCount()>0) {
            res.close();
            return true;
        }
        else{
            res.close();
            return false;
        }
    }

    public boolean updateAnimeinfo (Integer id, String title, String imgurl, String genre, String episodes, String animetype, String agerating, String description)
    {
        final String TAG = CLASS_TAG+"updateAnimeinfo";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ANIMEINFO_COLUMN_TITLE,title);
        contentValues.put(ANIMEINFO_COLUMN_IMGURL,imgurl);
        contentValues.put(ANIMEINFO_COLUMN_GENRE,genre);
        contentValues.put(ANIMEINFO_COLUMN_EPISODES, episodes);
        contentValues.put(ANIMEINFO_COLUMN_ANIMETYPE, animetype);
        contentValues.put(ANIMEINFO_COLUMN_AGERATING, agerating);
        contentValues.put(ANIMEINFO_COLUMN_DESCRIPTION, description);
        int rowsaffected = db.update(TABLE_ANIMEINFO, contentValues, GENERAL_COLUMN_ID +" = ? ", new String[]{Integer.toString(id)});

        if(rowsaffected>0) {
            Log.d(TAG,"updated info of anime: "+title);
            return true;
        }
        else {
            Log.i(TAG,"update of anime with id: "+id+" and title: "+title+" failed");
            return false;
        }
    }

    /**
     *
     * @param id The id of the anime in APanimeinfo table
     * @param animeinfo_id The id reference of the anime to table animeinfo
     * @param title ...
     * @param season ...
     * @param imgurl ...
     * @param genre ...
     * @param animetype ...
     * @param description ...
     * @param rating ...
     * @return True if more than one row was affected. False otherwise.
     */
    public boolean updateAPAnimeinfo (int id, int animeinfo_id, String title, String season, String imgurl, String genre, String animetype, String description, Double rating)
    {
        final String TAG = CLASS_TAG+"updateAPAnimeinfo";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(AP_ANIMEINFO_COLUMN_ANIMEINFOID,animeinfo_id);
        contentValues.put(AP_ANIMEINFO_COLUMN_TITLE,title);
        contentValues.put(AP_ANIMEINFO_COLUMN_SEASON,season);
        contentValues.put(AP_ANIMEINFO_COLUMN_IMGURL,imgurl);
        contentValues.put(AP_ANIMEINFO_COLUMN_GENRE, genre);
        contentValues.put(AP_ANIMEINFO_COLUMN_ANIMETYPE, animetype);
        contentValues.put(AP_ANIMEINFO_COLUMN_DESCRIPTION, description);
        contentValues.put(AP_ANIMEINFO_COLUMN_RATING, rating);
        int rowsaffected = db.update(TABLE_AP_ANIMEINFO, contentValues, GENERAL_COLUMN_ID +" = ? ", new String[]{Integer.toString(id)});

        if(rowsaffected>0) {
            Log.d(TAG,"updated AP info of anime: "+title);
            return true;
        }
        else {
            Log.i(TAG,"AP update of anime with id: "+id+" and title: "+title+" failed");
            return false;
        }
    }

    public boolean updateAnimelinks (int id, String frlink, String ultimalink, String MALlink)
    {
        final String TAG = CLASS_TAG+"updateAnimelinks";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ANIMELINKS_COLUMN_ANIMEFREAKLINK,frlink);
        contentValues.put(ANIMELINKS_COLUMN_ANIMEULTIMALINK,ultimalink);
        contentValues.put(ANIMELINKS_COLUMN_MALLINK,MALlink);
        int rowsaffected = db.update(TABLE_ANIMELINKS, contentValues, GENERAL_COLUMN_ID +" = ? ", new String[]{Integer.toString(id)});

        if(rowsaffected>0) {
            Log.d(TAG,"updated links of anime with id: "+id);
            return true;
        }
        else {
            Log.i(TAG,"update of links for anime with id: "+id+" failed");
            return false;
        }
    }

    public boolean updateWatchlistAnime(int id, int CurrentEpisodes, String lastupdated){
        final String TAG = CLASS_TAG+"updateWatchlistAnime";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WATCHLIST_COLUMN_CURRENTEPISODE,CurrentEpisodes);
        contentValues.put(WATCHLIST_COLUMN_LASTUPDATED,lastupdated);
        int rowsaffected = db.update(TABLE_WATCHLIST, contentValues, GENERAL_COLUMN_ID +" = ? ", new String[]{Integer.toString(id)});

        if(rowsaffected>0) {
            Log.d(TAG,"updated watchlist anime data with id: "+id);
            return true;
        }
        else {
            Log.i(TAG,"update of watchlist anime data with id: "+id+" failed");
            return false;
        }
    }

    /**
     *
     * @param id The id of the anime in the watchlist
     * @param CurrentEpisodes null not to update currentepisodes
     * @param EpisodesWatched null not to update episodeswatched
     * @return true if the update was successfull and more than one rows were affected false in any other case. Also false if both currentepisodes and episodeswatched are null.
     */
    public boolean updateWatchlistAnimeEps(int id, Integer CurrentEpisodes, Integer EpisodesWatched){
        final String TAG = CLASS_TAG+"updateWatchlistAnimeEps";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if(CurrentEpisodes==null&&EpisodesWatched==null){
            Log.i(TAG,"Both CurrentEpisodes and EpisodesWatched are null Cannot proceed");
            return false;
        }else if(CurrentEpisodes==null){
            contentValues.put(WATCHLIST_COLUMN_EPISODESWATCHED,EpisodesWatched);
        }else if(EpisodesWatched==null){
            contentValues.put(WATCHLIST_COLUMN_CURRENTEPISODE,CurrentEpisodes);
        }else{
            contentValues.put(WATCHLIST_COLUMN_CURRENTEPISODE,CurrentEpisodes);
            contentValues.put(WATCHLIST_COLUMN_EPISODESWATCHED,EpisodesWatched);
        }

        int rowsaffected = db.update(TABLE_WATCHLIST, contentValues, GENERAL_COLUMN_ID +" = ? ", new String[]{Integer.toString(id)});

        if(rowsaffected>0) {
            Log.d(TAG,"updated watchlist anime data with id: "+id);
            return true;
        }
        else {
            Log.i(TAG,"update of watchlist anime data with id: "+id+" failed");
            return false;
        }
    }

    public boolean updateMALtopanime(int spot, int id, double score){
        final String TAG = CLASS_TAG+"updateMALtopanime";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GENERAL_COLUMN_ID,id);
        contentValues.put(MAL_TOPANIME_COLUMN_SCORE,score);
        int rowsaffected = db.update(TABLE_MAL_TOPANIME, contentValues, MAL_TOPANIME_COLUMN_SPOT +" = ? ", new String[]{Integer.toString(spot)});

        if(rowsaffected>0) {
            //Log.d(TAG,"updated top anime in spot: "+spot);
            return true;
        }
        else {
            Log.i(TAG,"update of topanime in spot: "+spot+" failed");
            return false;
        }
    }

    public boolean updateVersion(int version) {
        final String TAG = CLASS_TAG+"updVersion";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_VERSION,version);
        db.execSQL("update "+TABLE_VERSION+" set "+ VERSION_COLUMN_VERSION +"="+version);
        Log.d(TAG,"updated version to: "+version);
        return true;
    }

    public boolean updateAPVersion(int version) {
        final String TAG = CLASS_TAG+"updAPVersion";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_AP_VERSION,version);
        db.execSQL("update "+TABLE_AP_VERSION+" set "+ AP_VERSION_COLUMN_VERSION +"="+version);
        Log.d(TAG,"updated APversion to: "+version);
        return true;
    }

    public boolean updateCurrentSeason(String currentSeason) {
        final String TAG = CLASS_TAG+"updCRseason";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(AP_CURRENTSEASON_COLUMN_SEASON,currentSeason);
        db.update(TABLE_AP_CURRENTSEASON,contentValues,null,null);
        Log.d(TAG,"updated Currentseason to: "+currentSeason);
        return true;
    }

    public boolean deleteAnime (Integer id)
    {
        final String TAG = CLASS_TAG+"deleteAnime";
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLE_ANIMEINFO,
                GENERAL_COLUMN_ID +" = ? ",
                new String[] { Integer.toString(id) });
        if(res==0) {
            Log.i(TAG, "delete of anime with id: " + id + " has failed");
            return false;
        }
        Log.d(TAG,"deleted anime with id: "+id);
        return true;
    }

    public boolean deleteAPAnime (Integer id)
    {
        final String TAG = CLASS_TAG+"delAPAnime";
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLE_AP_ANIMEINFO,
                GENERAL_COLUMN_ID +" = ? ",
                new String[] { Integer.toString(id) });
        if(res==0) {
            Log.i(TAG, "delete of anime with id: " + id + " has failed");
            return false;
        }
        Log.d(TAG,"deleted anime with id: "+id);
        return true;
    }

    public boolean deleteAnimelinks (Integer id)
    {
        final String TAG = CLASS_TAG+"deleteAnime";
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLE_ANIMELINKS,
                GENERAL_COLUMN_ID +" = ? ",
                new String[] { Integer.toString(id) });
        if(res==0) {
            Log.i(TAG, "delete of anime links with id: " + id + " has failed");
            return false;
        }
        return true;
    }

    public boolean deleteWatchlistAnime (Integer id)
    {
        final String TAG = CLASS_TAG+"deleteWatchlistAnime";
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLE_WATCHLIST,
                GENERAL_COLUMN_ID +" = ? ",
                new String[] { Integer.toString(id) });
        if(res==0) {
            Log.i(TAG, "delete of anime with id: " + id + " has failed");
            return false;
        }
        return true;
    }

    public boolean deleteWatchlaterlistAnime (Integer id)
    {
        final String TAG = CLASS_TAG+"deleteWatchlaterlistAnime";
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLE_WATCHLATER,
                GENERAL_COLUMN_ID +" = ? ",
                new String[] { Integer.toString(id) });
        if(res==0) {
            Log.i(TAG, "delete of anime with id: " + id + " has failed");
            return false;
        }
        return true;
    }

    public boolean deleteWatchedlistAnime (Integer id)
    {
        final String TAG = CLASS_TAG+"deleteWatchedlistAnime";
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLE_WATCHED,
                GENERAL_COLUMN_ID +" = ? ",
                new String[] { Integer.toString(id) });
        if(res==0) {
            Log.i(TAG, "delete of anime with id: " + id + " has failed");
            return false;
        }
        return true;
    }

    public boolean deleteHotAnime (Integer id)
    {
        final String TAG = CLASS_TAG+"deleteHotAnime";
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLE_HOTANIME,
                GENERAL_COLUMN_ID +" = ? ",
                new String[] { Integer.toString(id) });
        if(res==0) {
            Log.i(TAG, "delete of anime with id: " + id + " has failed");
            return false;
        }
        return true;
    }

    /**
     *
     * @param spot The spot after which every row in the table will be deleted (where spot>?)
     * @return true if more than one rows were deleted or false if 0 rows were affected
     */
    public boolean deleteMALtopanimeAfterSpot (int spot)
    {
        final String TAG = CLASS_TAG+"deleteMALtopanime";
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLE_MAL_TOPANIME,
                MAL_TOPANIME_COLUMN_SPOT +" > ? ",
                new String[] { Integer.toString(spot) });
        if(res==0) {
            Log.i(TAG, "deleted 0 topanime after spot: " + spot);
            return false;
        }
        return true;
    }

    public boolean incrementEpisodesWatched(int id){
        synchronized (DBHelper.class){
            boolean doneFlag = false;
            SQLiteDatabase db = this.getWritableDatabase();
            String[] projection = {WATCHLIST_COLUMN_EPISODESWATCHED, WATCHLIST_COLUMN_CURRENTEPISODE};
            Cursor res = db.query(TABLE_WATCHLIST,projection, GENERAL_COLUMN_ID +"=?",new String[] {String.valueOf(id)},null,null,null,null);
            if(res.moveToFirst()){
                int episodeswatched = res.getInt(res.getColumnIndex(WATCHLIST_COLUMN_EPISODESWATCHED));
                int currentepisode = res.getInt(res.getColumnIndex(WATCHLIST_COLUMN_CURRENTEPISODE));
                if(episodeswatched<currentepisode){
                    episodeswatched++;
                    ContentValues values = new ContentValues();
                    values.put(WATCHLIST_COLUMN_EPISODESWATCHED,episodeswatched);
                    db.update(TABLE_WATCHLIST,values, GENERAL_COLUMN_ID +"=?",new String[] {String.valueOf(id)});
                    doneFlag=true;
                }else{
                    Log.i("DBHelper-incrWatchl"," for the anime with ID: "+id+" episodeswatched>=currentepisode cant proceed");
                }
            }else{
                Log.i("DBHelper-incrWatchl"," ID: "+id+" not found in watchlist");
            }
            return doneFlag;
        }
    }

    public boolean decrementEpisodesWatched(int id){
        synchronized (DBHelper.class){
            boolean doneFlag = false;
            SQLiteDatabase db = this.getWritableDatabase();
            String[] projection = {WATCHLIST_COLUMN_EPISODESWATCHED};
            Cursor res = db.query(TABLE_WATCHLIST,projection, GENERAL_COLUMN_ID +"=?",new String[] {String.valueOf(id)},null,null,null,null);
            if(res.moveToFirst()){
                int episodeswatched = res.getInt(res.getColumnIndex(WATCHLIST_COLUMN_EPISODESWATCHED));
                if(episodeswatched>0){
                    episodeswatched--;
                    ContentValues values = new ContentValues();
                    values.put(WATCHLIST_COLUMN_EPISODESWATCHED,episodeswatched);
                    db.update(TABLE_WATCHLIST,values, GENERAL_COLUMN_ID +"=?",new String[] {String.valueOf(id)});
                    doneFlag=true;
                }else{
                    Log.i("DBHelper-decrWatchl"," for the anime with ID: "+id+" episodeswatched<=0 cant proceed");
                }
            }else{
                Log.i("DBHelper-decrWatchl"," ID: "+id+" not found in watchlist");
            }
            return doneFlag;
        }
    }

    /**
     *
     * @param titlestrings A list with the anime title strings of the online hotanime table
     */
    public void handleNewHotanimeUpdate(List<String> titlestrings){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res2 = db.query(TABLE_HOTANIME, new String[]{GENERAL_COLUMN_ID},null,null,null,null,null);
        ArrayList<Integer> hotanimeids = new ArrayList<>();
        ArrayList<Integer> jsoupids = new ArrayList<>();
        ArrayList<Integer> idstodelete = new ArrayList<>();
        ArrayList<Integer> idstoinsert = new ArrayList<>();
        while(res2.moveToNext()){
            hotanimeids.add(res2.getInt(res2.getColumnIndex(GENERAL_COLUMN_ID)));
        }
        res2.close();
        Cursor res;
        for(String titlestring : titlestrings) {
            res = db.query(TABLE_ANIMEINFO, new String[]{GENERAL_COLUMN_ID}, ANIMEINFO_COLUMN_TITLE + "=?", new String[]{titlestring}, null, null, null);
            if (res.moveToFirst()) {
                jsoupids.add(res.getInt(res.getColumnIndex(GENERAL_COLUMN_ID)));
            } else {
                Log.d("dbhelper -handlehotanim"," title: "+titlestring+ " not found in animeinfo");
            }
            res.close();
        }

        for(int id : jsoupids){
            if(!hotanimeids.contains(id)){
                idstoinsert.add(id);
            }
        }
        for(int id : hotanimeids){
            if(!jsoupids.contains(id)){
                idstodelete.add(id);
            }
        }
        for(int id : idstoinsert){
            this.insertIntoHotanime(id);
        }
        for(int id : idstodelete){
            this.deleteHotAnime(id);
        }
    }

    /**
     *
     * @param ids the anime ids list that watchlistUpdater found as ongoing
     */
    public void handleWatchlistRemainingUpdate(ArrayList<Integer> ids){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("DBHelper - handleWRupd"," Starting");

        String whereClause = null;
        String whereArgs[] = null;
        if(ids.size()>0){
            whereClause = "W."+GENERAL_COLUMN_ID+" not in (?";
            whereArgs =  new String[ids.size()];
            whereArgs[0] = String.valueOf(ids.get(0));
            for(int i=1; i<ids.size(); i++){
                whereClause+=",?";
                whereArgs[i] = String.valueOf(ids.get(i));
            }
            whereClause+=")";
        }

        //Log.d("DBHelper - handleWRupd"," whereClause: "+whereClause);

        ContentValues values;
        Cursor res = db.query(TABLE_WATCHLIST+" W inner join "+TABLE_ANIMEINFO+" Info on W."+GENERAL_COLUMN_ID+"=Info."+GENERAL_COLUMN_ID, new String[] {"W."+GENERAL_COLUMN_ID,"W."+WATCHLIST_COLUMN_CURRENTEPISODE,"Info."+ANIMEINFO_COLUMN_EPISODES},whereClause,whereArgs,null,null,null);
        //Log.d("DBHelper - handleWRupd", "resCount: "+res.getCount());
        while (res.moveToNext()){
            values = new ContentValues();
            values.put(WATCHLIST_COLUMN_LASTUPDATED,"");
            if(res.getInt(res.getColumnIndex(WATCHLIST_COLUMN_CURRENTEPISODE))==0) {
                try {
                    values.put(WATCHLIST_COLUMN_CURRENTEPISODE, Integer.valueOf(res.getString(res.getColumnIndex(ANIMEINFO_COLUMN_EPISODES))));
                } catch (NumberFormatException ex) {
                    //do nothing
                }
            }
            db.update(TABLE_WATCHLIST,values,GENERAL_COLUMN_ID+"=?",new String[] {String.valueOf(res.getInt(res.getColumnIndex(GENERAL_COLUMN_ID)))});
        }




    }

}
