package kioskmode.com.epoptia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import kioskmode.com.epoptia.kioskmodetablet.KioskModeActivity;

public class PackageReplacedReceiver extends BroadcastReceiver {

    private static final String debugTag = PackageReplacedReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
//            Log.e(debugTag, "PACKAGE REPLACED");
//
//            Intent i = new Intent(context, KioskModeActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            context.startActivity(i);
//        }
    }
}
