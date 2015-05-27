package com.test.keepalive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.prefs.Preferences;

public class NetworkStateChanged extends BroadcastReceiver {
    private static final String TAG="NetworkStateChanged";
    public NetworkStateChanged() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        if(Utils.isWIFI(context)&&Utils.isECNU(context)){
            Log.d(TAG,"login from receiver");
            Intent it=new Intent(context,LoginService.class);
            it.putExtra(context.getString(R.string.stateFlag),true);
            context.startService(it);
        }
    }


}
