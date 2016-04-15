package com.example.admin.animewatchmaster.databaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.admin.animewatchmaster.model.Anime;

import java.util.ArrayList;


/**
 * Created by admin on 4/11/2016.
 */
public class DBHelper extends SQLiteOpenHelper{

    //dbhelper
    public static final String DATABASE_NAME = "anime.db";
    public static final int DATABASE_VERSION = 1;
    private static final String CLASS_TAG = "DBHelper - ";

    //animeinfo
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_IMGURL = "imgurl";
    private static final String COLUMN_GENRE = "genre";
    private static final String COLUMN_EPISODES = "episodes";
    private static final String COLUMN_ANIMETYPE = "animetype";
    private static final String COLUMN_AGERATING = "agerating";
    private static final String COLUMN_DESCRIPTION = "description";

    //watchlist
    private static final String COLUMN_EPISODESWATCHED = "episodeswatched";
    private static final String COLUMN_CURRENTEPISODE = "currentepisode";
    private static final String COLUMN_LASTUPDATED = "lastupdated";

    //version
    private static final String COLUMN_VERSION = "version";

    //tables
    private static final String TABLE_ANIMEINFO = "animeinfo";
    private static final String TABLE_WATCHLIST = "watchlist";
    private static final String TABLE_WATCHLATER = "watchlaterlist";
    private static final String TABLE_VERSION = "version";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table if not exists "+TABLE_ANIMEINFO+
                        "("+COLUMN_ID+" integer primary key, "+COLUMN_TITLE+" text, "+COLUMN_IMGURL+" text, "+COLUMN_GENRE+" text, "+COLUMN_EPISODES+" text, "+COLUMN_ANIMETYPE+" text, "+COLUMN_AGERATING+" text, "+COLUMN_DESCRIPTION+" text)"
        );
        db.execSQL(
                "create table if not exists "+TABLE_WATCHLIST+
                        "("+COLUMN_ID+" integer primary key, "+COLUMN_EPISODESWATCHED+" integer, "+COLUMN_CURRENTEPISODE+" integer, "+COLUMN_LASTUPDATED+" text)"
        );
        db.execSQL(
                "create table if not exists "+TABLE_WATCHLATER+
                        "("+COLUMN_ID+" integer primary key)"
        );
        db.execSQL(
                "create table if not exists "+TABLE_VERSION+
                        "("+COLUMN_VERSION+" integer primary key)"
        );
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_VERSION, 0);
        db.insert(TABLE_VERSION, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ANIMEINFO);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ANIMEINFO);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_WATCHLIST);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_WATCHLATER);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_VERSION);
        onCreate(db);
    }

    public boolean insertAnime(String title, String imgurl, String genre, String episodes, String animetype, String agerating, String description){
        final String TAG = CLASS_TAG+"insertAnime";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE,title);
        contentValues.put(COLUMN_IMGURL,imgurl);
        contentValues.put(COLUMN_GENRE,genre);
        contentValues.put(COLUMN_EPISODES, episodes);
        contentValues.put(COLUMN_ANIMETYPE, animetype);
        contentValues.put(COLUMN_AGERATING, agerating);
        contentValues.put(COLUMN_DESCRIPTION, description);
        long result = db.insert(TABLE_ANIMEINFO, null, contentValues);
        if(result==-1){
            Log.i(TAG,"insert of anime with title "+title+" failed");
            return false;
        }
        return true;
    }

    public boolean insertIntoWatchlist(int id, int episodeswatched, int currentepisode, String lastupdated){
        final String TAG = CLASS_TAG+"insertIntoWatchlist";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID,id);
        contentValues.put(COLUMN_EPISODESWATCHED,episodeswatched);
        contentValues.put(COLUMN_CURRENTEPISODE,currentepisode);
        contentValues.put(COLUMN_LASTUPDATED, lastupdated);
        long result = db.insert(TABLE_WATCHLIST, null, contentValues);
        if(result==-1){
            Log.i(TAG,"insert of anime with id "+id+" into Watchlist failed");
            return false;
        }
        return true;
    }

    public boolean insertIntoWatchlaterlist(int id){
        final String TAG = CLASS_TAG+"insertIntoWatchlaterlist";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID,id);
        long result = db.insert(TABLE_WATCHLATER, null, contentValues);
        if(result==-1){
            Log.i(TAG,"insert of anime with id "+id+" into Watchlaterlist failed");
            return false;
        }
        return true;
    }

    public Cursor getAnimeinfo(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from animeinfo where "+COLUMN_ID+"="+id+"", null );
        return res;
    }

    public Cursor getAnimeinfo(String title){
        SQLiteDatabase db = this.getReadableDatabase();
        String command = "select title from animeinfo where "+COLUMN_TITLE+"=?";
        Cursor res =  db.rawQuery( command, new String[] {title} );
        return res;
    }

    public boolean checkIfExists(String title){
        SQLiteDatabase db = this.getReadableDatabase();
        String command = "select title from animeinfo where "+COLUMN_TITLE+"=?";
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

    public boolean updateAnimeinfo (Integer id, String title, String imgurl, String genre, String episodes, String animetype, String agerating, String description)
    {
        final String TAG = CLASS_TAG+"updateAnimeinfo";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE,title);
        contentValues.put(COLUMN_IMGURL,imgurl);
        contentValues.put(COLUMN_GENRE,genre);
        contentValues.put(COLUMN_EPISODES, episodes);
        contentValues.put(COLUMN_ANIMETYPE, animetype);
        contentValues.put(COLUMN_AGERATING, agerating);
        contentValues.put(COLUMN_DESCRIPTION, description);
        int rowsaffected = db.update(TABLE_ANIMEINFO, contentValues, COLUMN_ID+" = ? ", new String[]{Integer.toString(id)});

        if(rowsaffected>0)
            return true;
        else {
            Log.i(TAG,"update of anime with id: "+id+" and title: "+title+" failed");
            return false;
        }
    }

    public boolean updateVersion(int version) {
        final String TAG = CLASS_TAG+"updVersion";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_VERSION,version);
        db.execSQL("update version set "+COLUMN_VERSION+"="+version);
        Log.d(TAG,"updated version to: "+version);
        return true;
    }

    public Integer deleteAnime (Integer id)
    {
        final String TAG = CLASS_TAG+"deleteAnime";
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLE_ANIMEINFO,
                COLUMN_ID+" = ? ",
                new String[] { Integer.toString(id) });
        if(res==0)
            Log.i(TAG,"delete of anime with id: "+id+" has failed");
        return res;
    }

    public ArrayList<Anime> getAllAnimeByLetter(String Letter){ //ftiaxnei para polla objects prepei na ginei alliws
        ArrayList<Anime> allanime = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        System.out.println("Command: select * from animeinfo where "+COLUMN_TITLE+" like '"+Letter+"%' ");
        Cursor res = db.rawQuery("select * from animeinfo where "+COLUMN_TITLE+" like '%"+Letter+"%'",null);
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
            id = res.getInt(res.getColumnIndex(COLUMN_ID));
            title = res.getString(res.getColumnIndex(COLUMN_TITLE));
            imgurl = res.getString(res.getColumnIndex(COLUMN_IMGURL));
            genre = res.getString(res.getColumnIndex(COLUMN_GENRE));
            episodes = res.getString(res.getColumnIndex(COLUMN_EPISODES));
            animetype = res.getString(res.getColumnIndex(COLUMN_ANIMETYPE));
            agerating = res.getString(res.getColumnIndex(COLUMN_AGERATING));
            description = res.getString(res.getColumnIndex(COLUMN_DESCRIPTION));

            allanime.add(new Anime(id,title,imgurl,genre,episodes,animetype,agerating,description));
            res.moveToNext();
        }
        return allanime;
    }

    public int numberOfAnime(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_ANIMEINFO);
        return numRows;
    }



}
