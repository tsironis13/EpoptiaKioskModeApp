package kioskmode.com.epoptia.app.utils;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import kioskmode.com.epoptia.services.ConnectivityChangeService;

public class ConnectivityChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName comp = new ComponentName(context.getPackageName(), ConnectivityChangeService.class.getName());

        intent.putExtra("networkStatus",isConnected(context));
        context.startService(intent.setComponent(comp));
    }

    public boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;



        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
}
