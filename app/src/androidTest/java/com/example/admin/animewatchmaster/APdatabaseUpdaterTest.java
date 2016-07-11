package com.example.admin.animewatchmaster;

import android.test.AndroidTestCase;

import com.example.admin.animewatchmaster.utils.Asynctasks.APdatabaseUpdater;

import org.junit.Test;

/**
 * Created by abraham on 11/7/2016.
 */
public class APdatabaseUpdaterTest extends AndroidTestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testSuccessFetch() {

        new APdatabaseUpdater(getContext()) {

            protected void onPostExecute(Void v ) {
                super.onPostExecute(v);
                assertTrue(true);
            }

        }.execute(getContext().getString(R.string.base_db_url));

    }


}
