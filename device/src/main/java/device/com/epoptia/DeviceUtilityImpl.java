package device.com.epoptia;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import device.com.epoptia.viewgroup.StatusBarOverlayCustomViewGroup;
import domain.com.epoptia.Constants;
import domain.com.epoptia.device.DeviceUtility;
import io.reactivex.Completable;
import io.reactivex.Single;

public class DeviceUtilityImpl implements DeviceUtility {

    //region Injections

    //endregion

    //region Private Properties

    private Context mContext;

    private StatusBarOverlayCustomViewGroup statusBarOverlayCustomViewGroup;

    //endregion

    //region Constructor

    @Inject
    public DeviceUtilityImpl(Context context) {
        this.mContext = context;

        statusBarOverlayCustomViewGroup = new StatusBarOverlayCustomViewGroup(mContext);
    }

    //endregion

    //region Public Methods


    @Override
    public Single<Boolean> isMyAppLauncherDefault() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MAIN);
        intentFilter.addCategory(Intent.CATEGORY_HOME);

        List<IntentFilter> filters = new ArrayList<>();
        filters.add(intentFilter);

        String packageName = mContext.getPackageName();

        List<ComponentName> activities = new ArrayList<>();

        PackageManager packageManager = mContext.getPackageManager();
        packageManager.getPreferredActivities(filters, activities, packageName);

        for (ComponentName activity: activities) {
            if (packageName.equals(activity.getPackageName())) {
                return Single.just(true);
            }
        }

        return Single.just(false);
    }

    @Override
    public Completable enableComponent(Class component) {
        if (component == null) {
            return Completable.error(new Throwable("Component not set"));
        }

        PackageManager p = mContext.getPackageManager();

        ComponentName cN = new ComponentName(mContext, component);
        p.setComponentEnabledSetting(cN, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        return Completable.complete();
    }

    @Override
    public Completable disableComponent(Class component) {
        if (component == null) {
            return Completable.error(new Throwable("Component not set"));
        }

        PackageManager p = mContext.getPackageManager();

        ComponentName cN = new ComponentName(mContext, component);
        p.setComponentEnabledSetting(cN, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        return Completable.complete();
    }

    @Override
    public Completable preventStatusBarExpansion() {
        WindowManager manager = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE));

        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();

        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

        int resId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int result = 0;

        if (resId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resId);
        } else {
            // Use Fallback size:
            result = 60; // 60px Fallback
        }

        localLayoutParams.height = result;
        localLayoutParams.format = PixelFormat.TRANSPARENT;

        //already added
        if (statusBarOverlayCustomViewGroup.getWindowToken() == null) {
            manager.addView(statusBarOverlayCustomViewGroup, localLayoutParams);
        }

        return Completable.complete();
    }

    @Override
    public Completable removeStatusBarOverlayCustomView() {
        WindowManager manager = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE));

        manager.removeView(statusBarOverlayCustomViewGroup);

        return Completable.complete();
    }

    @Override
    public Single<Integer> getDeviceCategory() {
        if (mContext.getResources().getConfiguration().smallestScreenWidthDp >= 600) {
            return Single.just(Constants.TABLET);
        }

        return Single.just(Constants.PHONE);
    }

    //#endregion

}
