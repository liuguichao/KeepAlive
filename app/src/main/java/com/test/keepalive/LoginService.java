package com.test.keepalive;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class LoginService extends IntentService {
    private static final String TAG="LoginService";
   private static final String key="%B5%C7+%C2%BC+%28Login%29";
    private Intent commIntent;
    public LoginService() {
        super("LoginService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        boolean state=intent.getBooleanExtra(getString(R.string.stateFlag), true);
        commIntent=new Intent(getString(R.string.actionFilter));

        if(state){
            boolean isSuccess=login();
            commIntent.putExtra(getString(R.string.action),"登录");
            commIntent.putExtra(getString(R.string.isSuccess),isSuccess);
        }
        else{
            Log.d(TAG,"logout service");
            boolean isSuccess=logout();
            commIntent.putExtra(getString(R.string.action),"注销");
            commIntent.putExtra(getString(R.string.isSuccess),isSuccess);
        }
        sendBroadcast(commIntent);
    }
    private  boolean login()
    {
        boolean isSuccess=false;
        try {
            URL url = new URL(getString(R.string.URL));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(3000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.prefName), Context.MODE_PRIVATE);

            String data = "DDDDD=" + URLEncoder.encode(sharedPref.getString(getString(R.string.username),""), "UTF-8") + "&upass=" +  URLEncoder.encode(sharedPref.getString(getString(R.string.password),""), "UTF-8")+"&0MKKey="
                    + key;
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36");
            //  Log.i(TAG, data);
            urlConnection.setRequestProperty("Content-Length",
                    String.valueOf(data.getBytes().length));
            OutputStream out = urlConnection.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            out.close();

            InputStream is= urlConnection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,"GB2312"));
            String line;

            while((line = rd.readLine()) != null) {
                if(line.contains(getString(R.string.loginFlag))){
                    Log.d(TAG,"login sucess");
                    isSuccess= true;
                    break;
                }
            }
            rd.close();


        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        finally {
            return isSuccess;
        }
    }
    private  boolean logout()
    {
        boolean isSuccess=false;
        try {
            URL url = new URL(getString(R.string.URL)+"F");

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(false);
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(3000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36");

            InputStream is = urlConnection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,"GB2312"));
            String line;

            while((line = rd.readLine()) != null) {
                if(line.contains(getString(R.string.logoutFlag))){
                    Log.d(TAG,"logout sucess");
                    isSuccess= true;
                    break;
                }
            }

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        finally {
            return isSuccess;
        }
    }


}

