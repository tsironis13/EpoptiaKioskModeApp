package kioskmode.com.epoptia;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import kioskmode.com.epoptia.app.utils.KioskService;
import kioskmode.com.epoptia.app.utils.LifecycleHandler;
import kioskmode.com.epoptia.databinding.ActivityBaseBinding;

/**
 * Created by giannis on 23/8/2017.
 */

public class KioskModeActivity extends BaseActivity {

    private static final String debugTag = KioskModeActivity.class.getSimpleName();
    private ActivityBaseBinding mBinding;
    protected static CustomViewGroup blockingView = null;
    private SharedPreferences prefs;
    private KioskService kioskService;
    private boolean viewDestroyed;
    private View mDecorView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(debugTag, "onCreate");
//        createWakeLocks();
//        saveInstanceStateCalled = false;
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_base);
        mDecorView = getWindow().getDecorView();
        if (savedInstanceState != null) {
//            viewDestroyed = savedInstanceState.getBoolean("viewdestroyed");
//            Log.e(debugTag, "savedInstanceState NOT NULL");
//            LifecycleHandler.get(this).removeListener(this);
        } else {
//            viewDestroyed = false;
//            LifecycleHandler.get().addListener(this);
//            Log.e(debugTag, "savedInstanceState NULL");
        }
//        LifecycleHandler.get(this).addListener(this);
        kioskService = new KioskService();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean("locked", true).apply();

        mBinding.unlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.edit().putBoolean("locked", false).apply();
                Intent intent = new Intent(KioskModeActivity.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(getResources().getString(R.string.action_type), 1020);
//        Bundle bundle = new Bundle();
//        bundle.putInt(getResources().getString(R.string.action_type), 1020);
//        intent.putExtras(bundle);
                startActivity(intent);
                finish();


//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                intent.addCategory(Intent.CATEGORY_DEFAULT);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                startActivity(Intent.createChooser(intent, "Set as default to enable Kiosk Mode"));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(debugTag, "onResume");
        preventStatusBarExpansion(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Log.e(debugTag, "onPause");
        if (blockingView!=null) {
            WindowManager manager = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
            manager.removeView(blockingView);
            viewDestroyed = true;
        }
        boolean isScreenOn = ((PowerManager) getSystemService(Context.POWER_SERVICE)).isScreenOn();
        KeyguardManager kgMgr = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        boolean showing = kgMgr.inKeyguardRestrictedInputMode();
        if (showing) {
//            Log.e(debugTag, "screen on");
        } else {
//            Log.e(debugTag, "screen sleeping");
        }
        if (prefs.getBoolean("locked", false)) {
//            partialWakeLock.acquire();
//            if (LifecycleHandler.isApplicationInForeground()) {
//                Log.e(debugTag, "app is in foreground");
//            } else {
//                Log.e(debugTag, "app is in background");
//            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        Log.e(debugTag, "onSaveInstanceState CALLED");
        viewDestroyed = true;
        outState.putBoolean("viewdestroyed", viewDestroyed);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        viewDestroyed = true;
        //unlocked btn clicked
        if (blockingView!=null && !viewDestroyed) {
            WindowManager manager = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
//            manager.removeView(blockingView);
        }
//        LifecycleHandler.get(this).removeListener(this);
//        Log.e(debugTag, "onDestroy");
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
//        removeListener = true;
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    public static void preventStatusBarExpansion(Context context) {
//        Log.e(debugTag, "preventStatusBarExpansion");
        WindowManager manager = ((WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE));

        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int result = 0;
        if (resId > 0) {
            result = context.getResources().getDimensionPixelSize(resId);
        } else {
            // Use Fallback size:
            result = 60; // 60px Fallback
        }

        localLayoutParams.height = result;
        localLayoutParams.format = PixelFormat.TRANSPARENT;

        blockingView = new CustomViewGroup(context);
        manager.addView(blockingView, localLayoutParams);
    }


    private static class CustomViewGroup extends ViewGroup {
        public CustomViewGroup(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            // Intercepted touch!
            return true;
        }
    }



}
