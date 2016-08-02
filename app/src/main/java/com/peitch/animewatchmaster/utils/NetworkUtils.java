package com.peitch.animewatchmaster.utils;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.NetworkOnMainThreadException;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by abraham on 10/6/2016.
 */
public class NetworkUtils {


    /**
     *
     * @param systemService
     * @return true false if is connected to network eg a wifi
     */
    public static boolean isNetworkAvailable(Object systemService) {
        ConnectivityManager connectivityManager = (ConnectivityManager)systemService;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    /**
     *
     * @param systemService
     * @return true false if internet if accessible
     * @throws NetworkOnMainThreadException
     *
     * eg isInternetConnectionActive(getSystemService(Context.CONNECTIVITY_SERVICE))
     */
    public static boolean isInternetConnectionActive(Object systemService) throws NetworkOnMainThreadException {
        if(isNetworkAvailable(systemService)) {

            try {

                HttpURLConnection httpURLConnection = (HttpURLConnection)(new URL("http://costaskafkas.com/").openConnection());
                httpURLConnection.setRequestProperty("User-Agent","Test");
                httpURLConnection.setRequestProperty("Connection","close");
                httpURLConnection.setConnectTimeout(2500);
                httpURLConnection.connect();
                return (httpURLConnection.getResponseCode() == 200);

            } catch (Throwable th) {
                th.printStackTrace();
            }
        }

        return false;
    }

    public static boolean isInternetConnectionActiveAnimeFreak(Object systemService) throws NetworkOnMainThreadException {
        if(isNetworkAvailable(systemService)) {

            try {

                HttpURLConnection httpURLConnection = (HttpURLConnection)(new URL("http://www.animefreak.tv").openConnection());
                httpURLConnection.setRequestProperty("User-Agent","Test");
                httpURLConnection.setRequestProperty("Connection","close");
                httpURLConnection.setConnectTimeout(2500);
                httpURLConnection.connect();
                return (httpURLConnection.getResponseCode() == 200);

            } catch (Throwable th) {
                th.printStackTrace();
            }
        }

        return false;
    }


}
