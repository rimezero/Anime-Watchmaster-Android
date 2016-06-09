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

    //animelinks
    private static final String COLUMN_ANIMEFREAKLINK = "frlink";
    private static final String COLUMN_ANIMEULTIMALINK = "ultimalink";

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
    private static final String TABLE_ANIMELINKS = "animelinks";
    private static final String TABLE_ANIMEINFO = "animeinfo";
    private static final String TABLE_WATCHLIST = "watchlist";
    private static final String TABLE_WATCHLATER = "watchlaterlist";
    private static final String TABLE_VERSION = "version";

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




    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table if not exists "+TABLE_ANIMELINKS+
                        "("+COLUMN_ID+" integer primary key, "+COLUMN_ANIMEFREAKLINK+" text, "+COLUMN_ANIMEULTIMALINK+" text)"
        );
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
        contentValues.put(COLUMN_VERSION, 0);
        db.insert(TABLE_VERSION, null, contentValues);
    }

    //kanw drop ta pada gia testing
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANIMELINKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANIMEINFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WATCHLIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WATCHLATER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VERSION);
        onCreate(db);
    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANIMELINKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANIMEINFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WATCHLIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WATCHLATER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VERSION);
        onCreate(db);
    }

    public boolean insertIntoAnimelinks(int id,String frlink,String ultimalink){
        final String TAG = CLASS_TAG+"insertAnime";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID,id);
        contentValues.put(COLUMN_ANIMEFREAKLINK,frlink);
        contentValues.put(COLUMN_ANIMEULTIMALINK,ultimalink);
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
        Log.d(TAG, "inserted anime " + title);
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

    public int getVersion(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from " + TABLE_VERSION, null);
        int version = -1;
        if(res.moveToFirst()){
            version = res.getInt(res.getColumnIndex(COLUMN_VERSION));
        }
        res.close();
        return version;
    }

    public Anime getAnimeinfo(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from " + TABLE_ANIMEINFO + " where " + COLUMN_ID + "=" + id + "", null);
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

    public Anime getAnimeInfo(int id) {

        SQLiteDatabase db = this.getReadableDatabase();
        String projection[] = {COLUMN_ID,COLUMN_TITLE,COLUMN_IMGURL,COLUMN_GENRE,COLUMN_EPISODES,COLUMN_ANIMETYPE,COLUMN_AGERATING,COLUMN_DESCRIPTION};
        Cursor res =  db.query(TABLE_ANIMEINFO,projection,"id = ?",new String[]{String.valueOf(id)},null,null,null);

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

    public int getAnimeID(String title){
        SQLiteDatabase db = this.getReadableDatabase();
        String command = "select "+COLUMN_ID+" from "+TABLE_ANIMEINFO+" where "+COLUMN_TITLE+"=?";
        Cursor res =  db.rawQuery( command, new String[] {title} );
        res.moveToFirst();
        int id = res.getInt(res.getColumnIndex(COLUMN_ID));
        res.close();
        return id;
    }

    public boolean checkIfExistsInAnimeInfo(String title){
        SQLiteDatabase db = this.getReadableDatabase();
        String command = "select title from "+TABLE_ANIMEINFO+" where "+COLUMN_TITLE+"=?";
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
        String command = "select "+COLUMN_ANIMEFREAKLINK+" from "+TABLE_ANIMELINKS+" where "+COLUMN_ID+"=?";
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

        if(rowsaffected>0) {
            Log.d(TAG,"updated info of anime: "+title);
            return true;
        }
        else {
            Log.i(TAG,"update of anime with id: "+id+" and title: "+title+" failed");
            return false;
        }
    }

    public boolean updateAnimelinks (int id, String frlink, String ultimalink)
    {
        final String TAG = CLASS_TAG+"updateAnimelinks";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ANIMEFREAKLINK,frlink);
        contentValues.put(COLUMN_ANIMEULTIMALINK,ultimalink);
        int rowsaffected = db.update(TABLE_ANIMELINKS, contentValues, COLUMN_ID+" = ? ", new String[]{Integer.toString(id)});

        if(rowsaffected>0) {
            Log.d(TAG,"updated links of anime with id: "+id);
            return true;
        }
        else {
            Log.i(TAG,"update of links for anime with id: "+id+" failed");
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

    public void databaseUpdate(){

    }

    public ArrayList<Anime> getAllAnimeByLetterWithFilters(String Letter, ArrayList<String> filterslist) {
        ArrayList<Anime> allanime = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder command = new StringBuilder();
        command.append("select * from animeinfo where "+COLUMN_TITLE+" like '"+Letter+"%'");

        for(String filter : filterslist){
            command.append(" and "+COLUMN_GENRE+" like '%"+filter+"%'");
        }

        //Cursor res = db.rawQuery("select * from animeinfo where "+COLUMN_TITLE+" like '%"+Letter+"%'",null);
        Cursor res = db.rawQuery(command.toString(),null);
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

    public ArrayList<Anime> getAllAnimeByLetter(String Letter){ //ftiaxnei para polla objects prepei na ginei alliws
        ArrayList<Anime> allanime = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        System.out.println("Command: select * from animeinfo where " + COLUMN_TITLE + " like '" + Letter + "%' ");
        //Cursor res = db.rawQuery("select * from animeinfo where "+COLUMN_TITLE+" like '%"+Letter+"%'",null);
        Cursor res = db.rawQuery("select * from animeinfo where "+COLUMN_TITLE+" like '"+Letter+"%' order by "+COLUMN_TITLE+" asc",null);
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

    public ArrayList<Anime> searchForAnimeWithFilters(String searchString, ArrayList<String> filterslist){ //ftiaxnei para polla objects prepei na ginei alliws
        ArrayList<Anime> allanime = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder command = new StringBuilder();
        command.append("select * from animeinfo where "+COLUMN_TITLE+" like '%"+searchString+"%'");

        for(String filter : filterslist){
            command.append(" and "+COLUMN_GENRE+" like '%"+filter+"%'");
        }

        //Cursor res = db.rawQuery("select * from animeinfo where "+COLUMN_TITLE+" like '%"+Letter+"%'",null);
        Cursor res = db.rawQuery(command.toString(),null);
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


    public ArrayList<Anime> searchForAnime(String searchString){ //ftiaxnei para polla objects prepei na ginei alliws
        ArrayList<Anime> allanime = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        System.out.println("Command: select * from animeinfo where " + COLUMN_TITLE + " like '%" + searchString + "%' ");
        //Cursor res = db.rawQuery("select * from animeinfo where "+COLUMN_TITLE+" like '%"+Letter+"%'",null);
        Cursor res = db.rawQuery("select * from animeinfo where "+COLUMN_TITLE+" like '%"+searchString+"%'",null);
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
