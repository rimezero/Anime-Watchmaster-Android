package com.example.admin.animewatchmaster;

import android.test.AndroidTestCase;

import com.example.admin.animewatchmaster.utils.Asynctasks.APdatabaseUpdater;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * Created by abraham on 11/7/2016.
 */
public class APdatabaseUpdaterTest extends AndroidTestCase {

    private String result = null;
    private Exception exception = null;
    private CountDownLatch signal = null;

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

        APdatabaseUpdater aPdatabaseUpdater = new APdatabaseUpdater(getContext());
        aPdatabaseUpdater.setListener(new APdatabaseUpdater.APdatabaseUpdaterListener() {

            @Override
            public void onComplete(String res, Exception ex) {
                result = res;
                exception = ex;
                signal.countDown();
            }

        }).execute(getContext().getString(R.string.base_db_url));

        signal.await();

        assertEquals("1",result);
        assertNull(exception);

    }


}
