package com.peitch.animewatchmaster;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.peitch.animewatchmaster.model.Anime;
import com.peitch.animewatchmaster.model.SeasonsSortModel;
import com.peitch.animewatchmaster.model.WatchListModel;
import com.peitch.animewatchmaster.utils.databaseUtils.DBHelper;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by abraham on 11/7/2016.
 */
public class DbHelperTest extends AndroidTestCase {

    private  DBHelper dbHelper;

    @Override
    public void setUp() throws Exception {

        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(),"test_");
        dbHelper = DBHelper.getInstance(context);

        System.setProperty("dexmaker.dexcache", getContext().getCacheDir().toString());
    }

    @Override
    public void tearDown() throws Exception {
        dbHelper.close();
        super.tearDown();
    }



    @Test
    public void testInstance(){
        assertNotNull(dbHelper);
        assertEquals(dbHelper.getClass(), DBHelper.class);
    }

    @Test
    public void testDropDB() {
        assertTrue(mContext.deleteDatabase(DBHelper.DATABASE_NAME));
    }

    @Test
    public void testCreateDB() {
        dbHelper = DBHelper.getInstance(mContext);
        assertTrue(dbHelper.getWritableDatabase().isOpen());
        dbHelper.close();
    }


    @Test
    public void testSqliteAssetHelper() {
        Anime anime =  dbHelper.getAnimeInfo(10);
        testDropDB();
        Anime sameAnime = dbHelper.getAnimeInfo(10);
        assertEquals(anime, sameAnime);
        Anime otherAnime = dbHelper.getAnimeInfo(11);
        assertNotSame(anime, otherAnime);


        anime = null;
        sameAnime = null;
        for(int i =1; i <= 10; i++) {
            testCreateDB();

            anime = dbHelper.getAnimeInfo(i);
            assertNotNull(anime);
            dbHelper.insertIntoWatchedlist(anime.getId());

            sameAnime = dbHelper.getAnimeInfo(i);
            assertEquals(anime, sameAnime);
            dbHelper.insertIntoWatchedlist(sameAnime.getId());

            int expectedWatchedListSize = 1;
            assertEquals(expectedWatchedListSize,dbHelper.getWatchedListData().size());

            otherAnime = dbHelper.getAnimeInfo((i+1));
            dbHelper.insertIntoWatchedlist(otherAnime.getId());
            int expectedWatchedListSize2 = 2;
            assertEquals(expectedWatchedListSize2,dbHelper.getWatchedListData().size());

            testDropDB();
        }

    }


    @Test
    public void testGetSeasons() {
        List<SeasonsSortModel> seasons = dbHelper.getSeasons();
        assertNotNull(seasons);
    }


    @Test
    public void testGetWatchlistData() {

        List<WatchListModel> watchListModels = new ArrayList<>();

        for(int i =1; i <= 10; i++) {

            WatchListModel watchListModel = mock(WatchListModel.class);

            when(watchListModel.getId()).thenReturn(i);
            when(watchListModel.getTitle()).thenReturn("Anime_Title_number_"+i);
            when(watchListModel.getImgurl()).thenReturn("http://www.img.com/url"+i);
            watchListModels.add(watchListModel);

        }


        for(WatchListModel watchListModel : watchListModels) {
            dbHelper.insertIntoWatchlist(watchListModel.getId(),0,0,null);
        }

        assertEquals(10,dbHelper.getWatchlistData().size());

        List<WatchListModel> actualList = dbHelper.getWatchlistData();

        assertEquals(10,actualList.size());

        for(int i =1; i < actualList.size(); i++) {
            assertEquals(watchListModels.get(i).getId(),actualList.get(i).getId());
        }


    }




}
