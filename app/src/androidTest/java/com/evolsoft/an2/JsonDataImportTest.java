package com.evolsoft.animewatchmaster;

import android.test.AndroidTestCase;

import com.evolsoft.animewatchmaster.utils.databaseUtils.jsonDataImport;

import org.json.JSONArray;
import org.junit.Test;

/**
 * Created by abraham on 12/7/2016.
 */
public class JsonDataImportTest extends AndroidTestCase {


    @Test
    public void testGetHotanimeData() {
        JSONArray data = jsonDataImport.getHotanimeData(getContext().getString(R.string.base_db_url));
        assertNotNull(data);
        assertTrue(data.toString().startsWith("[{\"title\":"));
    }


    @Test
    public void testGetMALtopanimeData() {
        JSONArray data = jsonDataImport.getMALtopanimeData(getContext().getString(R.string.base_db_url));
        assertNotNull(data);
        assertTrue(data.toString().startsWith("[{\"score\":"));
    }


    @Test
    public void testGetAPAnimeinfoData() {
        //to do test
    }


}
