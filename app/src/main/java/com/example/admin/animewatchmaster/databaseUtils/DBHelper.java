package com.example.admin.animewatchmaster.databaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.admin.animewatchmaster.model.Anime;

import java.util.ArrayList;


/**
 * Created by admin on 4/11/2016.
 */
public class DBHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "anime.db";
    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table if not exists animeinfo " +
                        "(id integer primary key, title text, imgurl text, genre text, episodes text, animetype text, agerating text, description text)"
        );
        db.execSQL(
                "create table if not exists watchlist " +
                        "(id integer primary key, episodeswatched integer, currentepisode integer, lastupdated text)"
        );
        db.execSQL(
                "create table if not exists watchlaterlist " +
                        "(id integer primary key)"
        );
        db.execSQL(
                "create table if not exists version " +
                        "(version integer primary key)"
        );
        ContentValues contentValues = new ContentValues();
        contentValues.put("version", 0);
        db.insert("version", null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS animeinfo");
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS animeinfo");
        db.execSQL("DROP TABLE IF EXISTS watchlist");
        db.execSQL("DROP TABLE IF EXISTS watchlaterlist");
        db.execSQL("DROP TABLE IF EXISTS version");
        onCreate(db);
    }

    public boolean insertAnime(String title, String imgurl, String genre, String episodes, String animetype, String agerating, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title",title);
        contentValues.put("imgurl",imgurl);
        contentValues.put("genre",genre);
        contentValues.put("episodes", episodes);
        contentValues.put("animetype", animetype);
        contentValues.put("agerating", agerating);
        contentValues.put("description", description);
        db.insert("animeinfo", null, contentValues);
        return true;
    }

    public boolean insertIntoWatchlist(int id, int episodeswatched, int currentepisode, String lastupdated){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id);
        contentValues.put("episodeswatched",episodeswatched);
        contentValues.put("currentepisode",currentepisode);
        contentValues.put("lastupdated", lastupdated);
        db.insert("watchlist", null, contentValues);
        return true;
    }

    public boolean insertIntoWatchlaterlist(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id);
        db.insert("watchlaterlist", null, contentValues);
        return true;
    }

    public Cursor getAnimeinfo(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from animeinfo where id="+id+"", null );
        return res;
    }

    public Cursor getAnimeinfo(String title){
        SQLiteDatabase db = this.getReadableDatabase();
        String command = "select title from animeinfo where title=?";
        Cursor res =  db.rawQuery( command, new String[] {title} );
        return res;
    }

    public boolean checkIfExists(String title){
        SQLiteDatabase db = this.getReadableDatabase();
        String command = "select title from animeinfo where title=?";
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
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title",title);
        contentValues.put("imgurl",imgurl);
        contentValues.put("genre",genre);
        contentValues.put("episodes",episodes);
        contentValues.put("animetype",animetype);
        contentValues.put("agerating",agerating);
        contentValues.put("description", description);
        int rowsaffected = db.update("animeinfo", contentValues, "id = ? ", new String[]{Integer.toString(id)});

        if(rowsaffected>0)
            return true;
        else
            return false;
    }

    public boolean updateVersion(int version) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("version",version);
        db.execSQL("update version set version="+version);
        return true;
    }

    public Integer deleteAnime (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("animeinfo",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<Anime> getAllAnimeByLetter(String Letter){ //ftiaxnei para polla objects prepei na ginei alliws
        ArrayList<Anime> allanime = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        System.out.println("Command: select * from animeinfo where title like '"+Letter+"%' ");
        Cursor res = db.rawQuery("select * from animeinfo where title like '%"+Letter+"%'",null);
        //Cursor res = db.rawQuery("select * from animeinfo",null);
        res.moveToFirst();

        int id;
        String title;
        String imgurl;
        String genre;
        String episodes;
        String animetype;
        String agerating;
        String description;

        while(res.isAfterLast()==false){
            id = res.getInt(res.getColumnIndex("id"));
            title = res.getString(res.getColumnIndex("title"));
            imgurl = res.getString(res.getColumnIndex("imgurl"));
            genre = res.getString(res.getColumnIndex("genre"));
            episodes = res.getString(res.getColumnIndex("episodes"));
            animetype = res.getString(res.getColumnIndex("animetype"));
            agerating = res.getString(res.getColumnIndex("agerating"));
            description = res.getString(res.getColumnIndex("description"));

            allanime.add(new Anime(id,title,imgurl,genre,/*Integer.valueOf(episodes)*/0,animetype,agerating,description)); //episodes mporei na einai string na to elenksw
            res.moveToNext();
        }
        return allanime;
    }

    public int numberOfAnime(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "animeinfo");
        return numRows;
    }



}
