package kioskmode.com.epoptia.app;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;
import data.com.epoptia.localstorage.prefs.components.PreferenceComponent_ClientComponent;
import data.com.epoptia.localstorage.prefs.components.PreferenceComponent_DeviceComponent;
import data.com.epoptia.localstorage.prefs.components.PreferenceComponent_UserComponent;
import data.com.epoptia.localstorage.prefs.components.PreferenceComponent_WorkStationComponent;
import data.com.epoptia.localstorage.prefs.components.PreferenceComponent_WorkerComponent;
import data.com.epoptia.localstorage.prefs.components.PreferenceComponent_WorkerPanelComponent;
import kioskmode.com.epoptia.app.utils.KioskService;
import kioskmode.com.epoptia.app.utils.LifecycleHandler;
import kioskmode.com.epoptia.di.component.DaggerAppComponent;

/**
 * Created by giannis on 26/8/2017.
 */

public class MyApplication extends Application implements HasActivityInjector, HasSupportFragmentInjector, LifecycleHandler.AppStateListener {

    //region Injections

    /**
     * DispatchingAndroidInjector<Activity> contains a map of injectors for activities.
     * Inject DispatchingAndroidInjector<Activity> giving AndroidInjection access
     * to the list of injectable activities.
     * Thanks to this class, activities don’t have to depend directly on app module
     * and MyApplication class doesn’t need to know anything about specific activities.
     */
    @Inject
    DispatchingAndroidInjector<Activity> mActivityInjector;

    @Inject
    DispatchingAndroidInjector<Fragment> mFragmentInjector;

    //endregion

    //region Private Properties

    private static final String debugTag = MyApplication.class.getSimpleName();
    private Intent intent;

    //endregion

    //region Lifecycle Methods

    @Override
    public void onCreate() {
        super.onCreate();

        injectDispatchingAndroidActivityInjector();

        this.initializePreferenceComponents();

        intent = new Intent(this, KioskService.class);
        LifecycleHandler.init(this);
        LifecycleHandler.get(this).addListener(this);
//        registerActivityLifecycleCallbacks(new LifecycleHandler());
    }

    //endregion

    //region Public Methods

    @Override
    public void onBecameForeground() {
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
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return mActivityInjector;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return mFragmentInjector;
    }

    //endregion

    //region Private Methods

    /**
     * Inject DispatchingAndroidInjector<Activity> instance into MyApplication.
     */
    private void injectDispatchingAndroidActivityInjector() {
        DaggerAppComponent
                    .builder()
                    .create(this)
                    .inject(this);
    }

    private void initializePreferenceComponents() {
        PreferenceComponent_ClientComponent.init(this);
        PreferenceComponent_UserComponent.init(this);
        PreferenceComponent_DeviceComponent.init(this);
        PreferenceComponent_WorkerPanelComponent.init(this);
        PreferenceComponent_WorkStationComponent.init(this);
        PreferenceComponent_WorkerComponent.init(this);
    }

    //endregion

}
