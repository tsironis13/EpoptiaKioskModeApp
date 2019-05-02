package kioskmode.com.epoptia.app.utils;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

import kioskmode.com.epoptia.kioskmodetablet.KioskModeActivity;

/**
 * Created by giannis on 26/8/2017.
 */

public class KioskService extends Service {

    private static final String debugTag = KioskService.class.getSimpleName();
    private Context context;
    private TimerTask timerTask;
    private Timer timer;

    public KioskService() {
        super();
        context = this;
    }

//    public KioskService(Context context) {
//        super();
//        this.context = context;
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        checkAppLockState();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timerTask.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void checkAppLockState() {
        timer = new Timer();
        initializeTask();
        timer.schedule(timerTask, 10, 10);
    }

    private void initializeTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                KeyguardManager kgMgr = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
                Intent intent = new Intent(context, KioskModeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

        };
    }
}
