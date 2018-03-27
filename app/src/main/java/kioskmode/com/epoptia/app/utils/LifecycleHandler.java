package kioskmode.com.epoptia.app.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by giannis on 26/8/2017.
 */

public class LifecycleHandler implements Application.ActivityLifecycleCallbacks {

    private static final String debugTag = LifecycleHandler.class.getSimpleName();
    private static LifecycleHandler instance;
    private List<AppStateListener> listeners = new CopyOnWriteArrayList<>();
    private Handler handler = new Handler();
    public static final long CHECK_DELAY = 400;
    private Runnable check;
    private static int resumed;
//    private static int paused;
    private static int started;
    private static int stopped;
    private boolean foreground = false, paused = true;

    public static LifecycleHandler init(Application application){
        if (instance == null) {
            instance = new LifecycleHandler();
            application.registerActivityLifecycleCallbacks(instance);
        }
        return instance;
    }

    public static LifecycleHandler get(Application application){
        if (instance == null) {
            init(application);
        }
        return instance;
    }

    public static LifecycleHandler get(Context ctx){
        if (instance == null) {
            Context appCtx = ctx.getApplicationContext();
            if (appCtx instanceof Application) {
                init((Application)appCtx);
            }
            throw new IllegalStateException(
                    "Foreground is not initialised and " +
                            "cannot obtain the Application object");
        }
        return instance;
    }

    public static LifecycleHandler get(){
        if (instance == null) {
            throw new IllegalStateException(
                    "Foreground is not initialised - invoke " +
                            "at least once with parameterised init/get");
        }
        return instance;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {}

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
    }

    @Override
    public void onActivityResumed(Activity activity) {
//        ++resumed;
        paused = false;
        boolean wasBackground = !foreground;
        foreground = true;

        if (check != null)
            handler.removeCallbacks(check);

        if (wasBackground){
            for (AppStateListener l : listeners) {
                try {
                    l.onBecameForeground();
                } catch (Exception exc) {

                }
            }
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
//        ++paused;
        paused = true;

        if (check != null) handler.removeCallbacks(check);

        handler.postDelayed(check = new Runnable() {
            @Override
            public void run() {
                if (foreground && paused) {
                    foreground = false;
                    for (AppStateListener l : listeners) {
                        try {
                            l.onBecameBackground();
                        } catch (Exception exc) {
                        }
                    }
                }
            }
        }, CHECK_DELAY);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public static boolean isApplicationVisible() {
        return started > stopped;
    }

//    public static boolean isApplicationInForeground() {
//        return resumed > paused;
//    }
//
    public void addListener(AppStateListener listener){
        listeners.add(listener);
    }

    public void removeListener(AppStateListener listener){
        listeners.remove(listener);
    }

    public interface AppStateListener {
        void onBecameForeground();
        void onBecameBackground();
    }
}
