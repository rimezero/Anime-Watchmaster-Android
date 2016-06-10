package com.example.admin.animewatchmaster.databaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.admin.animewatchmaster.model.Anime;
import com.example.admin.animewatchmaster.model.WatchListModel;
import com.example.admin.animewatchmaster.model.WatchlaterlistModel;

import java.util.ArrayList;
import java.util.List;


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
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_ANIMEFREAKLINK, frlink);
        contentValues.put(COLUMN_ANIMEULTIMALINK, ultimalink);
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
        Log.d("dbhelper - inswatchl", "inserted anime with id: "+id+" in the watchlist");
        if(result==-1){
            Log.i(TAG,"insert of anime with id "+id+" into Watchlist failed");
            return false;
        }
        return true;

    }

    public boolean insertIntoWatchlaterlist(int id){
        final String TAG = CLASS_TAG+"insertIntoWatchlist";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID,id);
        long result = db.insert(TABLE_WATCHLATER, null, contentValues);
        Log.d("dbhelper - inswatchlt", "inserted anime with id: "+id+" in the watchlaterlist");
        if(result==-1){
            Log.i(TAG,"insert of anime with id "+id+" into watchlaterlist failed");
            return false;
        }
        return true;
    }


    public List<WatchListModel> getWatchlistData() {

        List<WatchListModel> models = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();


        String command = "select W."+COLUMN_ID+", Info."+COLUMN_TITLE+", Info."+COLUMN_IMGURL+", W."+COLUMN_EPISODESWATCHED+", W."+COLUMN_CURRENTEPISODE+", W."+COLUMN_LASTUPDATED+" from "+TABLE_WATCHLIST+" W inner join "+TABLE_ANIMEINFO+" Info on W."+COLUMN_ID+"=Info."+COLUMN_ID;
        Cursor c = db.rawQuery(command,null);

        if(c.moveToFirst()) {
            do {

                WatchListModel watchListModel = new WatchListModel();
                watchListModel.setId(c.getInt(c.getColumnIndex(COLUMN_ID)));
                watchListModel.setTitle(c.getString(c.getColumnIndex(COLUMN_TITLE)));
                watchListModel.setImgurl(c.getString(c.getColumnIndex(COLUMN_IMGURL)));
                watchListModel.setCurrentEpisode(c.getInt(c.getColumnIndex(COLUMN_CURRENTEPISODE)));
                watchListModel.setEpisodeswatched(c.getInt(c.getColumnIndex(COLUMN_EPISODESWATCHED)));
                watchListModel.setLastupdated(c.getString(c.getColumnIndex(COLUMN_LASTUPDATED)));

                models.add(watchListModel);

            } while (c.moveToNext());
        }

        c.close();

        return models;
    }

    public List<WatchlaterlistModel> getWatchlaterlistData() {

        List<WatchlaterlistModel> models = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String command = "select W."+COLUMN_ID+", Info."+COLUMN_TITLE+" from "+TABLE_WATCHLATER+" W inner join "+TABLE_ANIMEINFO+" Info on W."+COLUMN_ID+"=Info."+COLUMN_ID;
        Cursor c = db.rawQuery(command,null);

        if(c.moveToFirst()) {
            do {

                WatchlaterlistModel watchlaterlistModel = new WatchlaterlistModel(c.getInt(c.getColumnIndex(COLUMN_ID)),c.getString(c.getColumnIndex(COLUMN_TITLE)));

                models.add(watchlaterlistModel);

            } while (c.moveToNext());
        }

        c.close();

        return models;
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

    //returns watchlist id or -1 if the anime with this title does not exist in the watchlist or -2 if the anime does not exists in the database at all
    public int getWatchlistID(String title){
        SQLiteDatabase db = this.getReadableDatabase();
        String command = "select "+COLUMN_ID+" from "+TABLE_ANIMEINFO+" where "+COLUMN_TITLE+"=?";
        Cursor res = db.rawQuery(command, new String[] {title} );

        if(res.moveToFirst()){
            int id = res.getInt(res.getColumnIndex(COLUMN_ID));
            command = "select "+COLUMN_ID+" from "+TABLE_WATCHLIST+" where "+COLUMN_ID+"=?";
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

    public boolean checkIfExistsInWatchlist(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String command = "select "+COLUMN_ID+" from "+TABLE_WATCHLIST+" where "+COLUMN_ID+"=?";
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

    public boolean updateWatchlistAnime(int id, int CurrentEpisodes, String lastupdated){
        final String TAG = CLASS_TAG+"updateWatchlistAnime";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CURRENTEPISODE,CurrentEpisodes);
        contentValues.put(COLUMN_LASTUPDATED,lastupdated);
        int rowsaffected = db.update(TABLE_WATCHLIST, contentValues, COLUMN_ID+" = ? ", new String[]{Integer.toString(id)});

        if(rowsaffected>0) {
            Log.d(TAG,"updated watchlist anime data with id: "+id);
            return true;
        }
        else {
            Log.i(TAG,"update of watchlist anime data with id: "+id+" failed");
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

    //0 by letter, 1 by letter with filters, 2 search, 3 search with filters  for 0 and 2 filterslist can be null
    public ArrayList<Anime> getAllAnime(int commandType, String searchparam, ArrayList<String> filterslist){
        ArrayList<Anime> allanime = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder command = new StringBuilder();

        switch (commandType){
            case 0:
                command.append("select * from animeinfo where "+COLUMN_TITLE+" like '"+searchparam+"%' order by "+COLUMN_TITLE+" collate nocase asc");
                break;
            case 1:
                command.append("select * from animeinfo where "+COLUMN_TITLE+" like '"+searchparam+"%'");

                if(filterslist!=null) {
                    for (String filter : filterslist) {
                        command.append(" and " + COLUMN_GENRE + " like '%" + filter + "%'");
                    }
                }else{
                    Log.i(CLASS_TAG+"getAllAnime"," commandType is search by letter WITH FILTERS but filterslist is null");
                }

                command.append(" order by "+COLUMN_TITLE+" collate nocase asc");
                break;
            case 2:
                command.append("select * from animeinfo where "+COLUMN_TITLE+" like '%"+searchparam+"%' order by "+COLUMN_TITLE+" collate nocase asc");
                break;
            case 3:
                command.append("select * from animeinfo where "+COLUMN_TITLE+" like '%"+searchparam+"%'");

                if(filterslist!=null) {
                    for (String filter : filterslist) {
                        command.append(" and " + COLUMN_GENRE + " like '%" + filter + "%'");
                    }
                }else{
                    Log.i(CLASS_TAG+"getAllAnime"," commandType is search WITH FILTERS but filterslist is null");
                }

                command.append(" order by "+COLUMN_TITLE+" collate nocase asc");
                break;
            default:
                command.append("select * from animeinfo where "+COLUMN_TITLE+" like '%"+searchparam+"%' order by "+COLUMN_TITLE+" collate nocase asc");
                Log.i(CLASS_TAG+"getAllAnime"," commandType is out of bounds executing search with parameter as default method");

        }

        Log.d(CLASS_TAG+"getAllAnime","executing command: "+command.toString());
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

    public boolean deleteAnime (Integer id)
    {
        final String TAG = CLASS_TAG+"deleteAnime";
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLE_ANIMEINFO,
                COLUMN_ID+" = ? ",
                new String[] { Integer.toString(id) });
        if(res==0) {
            Log.i(TAG, "delete of anime with id: " + id + " has failed");
            return false;
        }
        return true;
    }

    public boolean deleteAnimelinks (Integer id)
    {
        final String TAG = CLASS_TAG+"deleteAnime";
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLE_ANIMELINKS,
                COLUMN_ID+" = ? ",
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
                COLUMN_ID+" = ? ",
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
                COLUMN_ID+" = ? ",
                new String[] { Integer.toString(id) });
        if(res==0) {
            Log.i(TAG, "delete of anime with id: " + id + " has failed");
            return false;
        }
        return true;
    }



    public int numberOfAnime(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_ANIMEINFO);
        return numRows;
    }



}
