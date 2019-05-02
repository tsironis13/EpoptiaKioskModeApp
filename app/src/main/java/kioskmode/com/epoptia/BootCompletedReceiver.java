package kioskmode.com.epoptia;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import kioskmode.com.epoptia.kioskmodetablet.KioskModeActivity;
import kioskmode.com.epoptia.utls.SharedPrefsUtl;

/**
 * Created by giannis on 31/8/2017.
 */

public class BootCompletedReceiver extends BroadcastReceiver {

    private static final String debugTag = BootCompletedReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean locked = SharedPrefsUtl.getBooleanFlag(context, context.getResources().getString(R.string.device_locked));
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            //boolean deviceAwake = SharedPrefsUtl.getBooleanFlag(context, context.getResources().getString(R.string.device_is_awake));

            if (locked && context.getResources().getConfiguration().smallestScreenWidthDp >= 600) {
                //todo check
//                SharedPrefsUtl.setBooleanPref(context, context.getResources().getString(R.string.device_is_awake), true);
//
//                PackageManager p = context.getPackageManager();
//                ComponentName cN = new ComponentName(context, KioskModeActivity.class);
//                p.setComponentEnabledSetting(cN, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//
//                Intent i = new Intent(context, KioskModeActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                context.startActivity(i);
            }
        }
    }
}
