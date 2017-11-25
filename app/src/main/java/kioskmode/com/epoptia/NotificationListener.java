package kioskmode.com.epoptia;

import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * Created by giannis on 29/8/2017.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationListener extends NotificationListenerService {

    private static final String debugTag = NotificationListener.class.getSimpleName();

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
//        Log.e(debugTag, "onListenerCOnnected");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
//        Log.e(debugTag, "notification posted");
        cancelAllNotifications();
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
