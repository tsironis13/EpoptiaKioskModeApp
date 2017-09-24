package kioskmode.com.epoptia;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by giannis on 31/8/2017.
 */

public class BootCompletedReceiver extends BroadcastReceiver {

    private static final String debugTag = BootCompletedReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(debugTag, "boot completed");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean locked = preferences.getBoolean("locked", false);
//        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
//            if (locked) {
//                PackageManager p = context.getPackageManager();
//                ComponentName cN = new ComponentName(context, KioskModeActivity.class);
//                p.setComponentEnabledSetting(cN, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//
//                Intent i = new Intent(context, KioskModeActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                context.startActivity(i);
//            }

    }
}
