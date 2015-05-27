package com.test.keepalive;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by guichao on 2015/5/26.
 */
public class Utils {
    private static final String TAG="Utils";

    public static boolean isECNU(Context context)
    {
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.d(TAG,"SSID:"+wifiInfo.getSSID());
        return wifiInfo.getSSID().equalsIgnoreCase("\""+context.getString(R.string.SSID)+"\"");
    }
    public static boolean isWIFI(Context context) {
        ConnectivityManager manager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        Log.d(TAG,"isWIFI:"+netInfo.isConnected());
        return netInfo.isConnected();
    }

}
