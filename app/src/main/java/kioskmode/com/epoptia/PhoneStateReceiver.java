package kioskmode.com.epoptia;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RemoteViews;

import java.lang.reflect.Method;

/**
 * Created by giannis on 29/8/2017.
 */

public class PhoneStateReceiver extends BroadcastReceiver {

    private static final String debugTag = PhoneStateReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.e(debugTag , "onReceive");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean locked = preferences.getBoolean("locked", false);
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")){
//            Log.e(debugTag, "phone calling");
            if (locked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) notify(context, 1, 2, 12, null);
//                final String numberCall = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
//                String telephonyName = "com.android.internal.telephony.ITelephony";
                Bundle myBundle = intent.getExtras();
                if (myBundle != null) {
                    System.out.println("--------Not null-----");
                    try {
                        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
                            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                            System.out.println("--------in state-----");
                            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                                // Incoming call
                                String incomingNumber =intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                                System.out.println("--------------my number---------"+incomingNumber);
                                // this is main section of the code,. could also be use for particular number.
                                // Get the boring old TelephonyManager.
                                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                                // Get the getITelephony() method                          Class<?> classTelephony = Class.forName(telephonyManager.getClass().getName());
                                Class<?> classTelephony = Class.forName(telephonyManager.getClass().getName());
                                Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");
                                // Ignore that the method is supposed to be private methodGetITelephony.setAccessible(true);
                                methodGetITelephony.setAccessible(true);
                                // Invoke getITelephony() to get the ITelephony interface
                                Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);
                                // Get the endCall method from ITelephony
                                Class<?> telephonyInterfaceClass = Class.forName(telephonyInterface.getClass().getName());
                                Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");
                                // Invoke endCall()
                                methodEndCall.invoke(telephonyInterface);
                            }
                        }
                    } catch (Exception ex) { // Many things can go wrong with reflection calls
                        ex.printStackTrace();
                    }
                }
            }
//            disconnectPhoneItelephony(context);
            //reject call if number is matched to our blocking number
//            if(numberCall.equals(blockingNumber)){
//                disconnectPhoneItelephony(context);
//            }
        }
    }

    public static void notify(Context context, int id, int titleResId, int textResId, PendingIntent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        String title = context.getString(titleResId);
        String title = "aaaaaaaaaaaaaaa";
//        String text = context.getString(textResId);
        String text = "kjdfkjdfjfdkfdkjfdj";
        RemoteViews bigView = new RemoteViews(context.getPackageName(), R.layout.big_notification);

        Intent dismissIntent = new Intent(context, PhoneStateReceiver.class);
        dismissIntent.setAction("DISMISS");
        PendingIntent piDismiss = PendingIntent.getService(context, 0, dismissIntent, 0);

//set intents and pending intents to call service on click of "snooze" action button of notification
        Intent snoozeIntent = new Intent(context, PhoneStateReceiver.class);
        snoozeIntent.setAction("SNOOZE");
        PendingIntent piSnooze = PendingIntent.getService(context, 0, snoozeIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
//                .setCustomBigContentView(bigView)
//                .setContentIntent(piDismiss)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setOngoing(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCustomHeadsUpContentView(bigView);
//                .setSummaryText("+3 more"));
//                .setAutoCancel(true)
//                .setWhen(System.currentTimeMillis())
//                .setTicker(title);


//        Notification foregroundNote;
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//        foregroundNote = builder.setContentTitle("some string")
//                .setContentText("Slide down on note to expand")
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .build();
//
//        foregroundNote.bigContentView = bigView;

        notificationManager.notify(id, builder.build());
        removeNotification(notificationManager, id);
    }

    private static void removeNotification(final NotificationManager notificationManager, final int id) {
        Handler handler = new Handler();
        long delayInMilliseconds = 4000;
        handler.postDelayed(new Runnable() {
            public void run() {
                notificationManager.cancel(id);
            }
        }, delayInMilliseconds);
    }
}
