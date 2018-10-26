package kioskmode.com.epoptia.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

public class ConnectivityChangeService extends IntentService {

    public ConnectivityChangeService() {
        super(null);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle extras = intent != null ? intent.getExtras() : null;
        Intent networkIntent = new Intent("networkState");
        // You can also include some extra data.
        networkIntent.putExtra("networkState", extras != null && extras.getBoolean("networkStatus"));
        LocalBroadcastManager.getInstance(this).sendBroadcast(networkIntent);
    }
}
