package com.cspeitch.animewatchmaster;

import android.test.AndroidTestCase;

import com.cspeitch.animewatchmaster.utils.Asynctasks.hotanimeUpdater;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * Created by abraham on 12/7/2016.
 */
public class HotAnimeUpdaterTest extends AndroidTestCase {

    private CountDownLatch signal = null;
    private String result = null;
    private Exception exception = null;

    protected void setUp() throws Exception {
        super.setUp();
        signal = new CountDownLatch(1);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        signal.countDown();
    }

    @Test
    public void testSuccessFetch() throws InterruptedException {

        hotanimeUpdater hau = new hotanimeUpdater(getContext());
        hau.setListener(new hotanimeUpdater.hotanimeupdaterInterface() {

            @Override
            public void onComplete(String res, Exception ex) {
                result = res;
                exception = ex;
                signal.countDown();
            }

        }).execute(getContext().getString(R.string.base_db_url));

        signal.await();


        assertEquals("false",result);
        assertNull(exception);

    }



}
