package kioskmode.com.epoptia.app.utils;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import kioskmode.com.epoptia.KioskModeActivity;

/**
 * Created by giannis on 26/8/2017.
 */

public class MyApplication extends Application implements LifecycleHandler.AppStateListener{

    private static final String debugTag = MyApplication.class.getSimpleName();
    private Intent intent;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(this, KioskService.class);
        LifecycleHandler.init(this);
        LifecycleHandler.get(this).addListener(this);
//        registerActivityLifecycleCallbacks(new LifecycleHandler());
    }

    @Override
    public void onBecameForeground() {
//        Log.e(debugTag, "foreground app");
        stopService(intent);
    }

    @Override
    public void onBecameBackground() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean locked = preferences.getBoolean("locked", false);
        if (locked) {
//            Intent intent = new Intent(getApplicationContext(), KioskModeActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
            startService(intent);
        }
        Log.e(debugTag, "background app");
    }
}
