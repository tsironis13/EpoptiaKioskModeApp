package kioskmode.com.epoptia.kioskmode;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import kioskmode.com.epoptia.BaseActivity;
import kioskmode.com.epoptia.R;
import kioskmode.com.epoptia.SplashScreen;
import kioskmode.com.epoptia.app.utils.KioskService;
import kioskmode.com.epoptia.databinding.ActivityKioskModeBinding;
import kioskmode.com.epoptia.kioskmode.stationworkers.StationWorkersFrgmt;
import kioskmode.com.epoptia.kioskmode.systemdashboard.SystemDashboardFrgmt;
import kioskmode.com.epoptia.utls.SharedPrefsUtl;

/**
 * Created by giannis on 23/8/2017.
 */

public class KioskModeActivity extends BaseActivity {

    private static final String debugTag = KioskModeActivity.class.getSimpleName();
    private ActivityKioskModeBinding mBinding;
    protected static CustomViewGroup blockingView = null;
    private SharedPreferences prefs;
    private KioskService kioskService;
    private boolean viewDestroyed;
    private View mDecorView;
    private static final int topBackStackEntryId = 2060;
    private int stationId;
    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(debugTag, "onCreate");
//        createWakeLocks();
//        saveInstanceStateCalled = false;
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_kiosk_mode);
        mDecorView = getWindow().getDecorView();
        if (savedInstanceState != null) {
            stationId = savedInstanceState.getInt("station_id");
            if (savedInstanceState.getInt(getResources().getString(R.string.top_backstack_entry_id)) == topBackStackEntryId) {
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.kioskModeLlt, SystemDashboardFrgmt.newInstance(stationId), getResources().getString(R.string.system_dahsboard_frgmt))
                        .addToBackStack(getResources().getString(R.string.system_dahsboard_frgmt))
                        .commit();
            } else {
                initializeView();
            }
        } else {
            if (getIntent().getExtras() != null) {
                stationId = getIntent().getExtras().getInt("station_id");
            }
            initializeView();
        }
//        LifecycleHandler.get(this).addListener(this);
        kioskService = new KioskService();
        SharedPrefsUtl.setBooleanPref(this, getResources().getString(R.string.device_locked), true);

//        mBinding.unlockBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPrefsUtl.setBooleanPref(KioskModeActivity.this, getResources().getString(R.string.device_locked), false);
//                Intent intent = new Intent(KioskModeActivity.this, SplashScreen.class);
////                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra(getResources().getString(R.string.action_type), 1020);
////        Bundle bundle = new Bundle();
////        bundle.putInt(getResources().getString(R.string.action_type), 1020);
////        intent.putExtras(bundle);
//                startActivity(intent);
//                finish();
//
//
////                Intent intent = new Intent(Intent.ACTION_MAIN);
////                intent.addCategory(Intent.CATEGORY_HOME);
////                intent.addCategory(Intent.CATEGORY_DEFAULT);
////                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////
////                startActivity(Intent.createChooser(intent, "Set as default to enable Kiosk Mode"));
//            }
//        });
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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("station_id", stationId);
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.system_dahsboard_frgmt)))
                outState.putInt(getResources().getString(R.string.top_backstack_entry_id), topBackStackEntryId);
//            Log.e(debugTag, getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() + " aaaa");
        }
        viewDestroyed = true;
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
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
        if (!hasFocus) {
            Log.e(debugTag, "no Focus");
//            preventStatusBarExpansion(this);
        } else {
            Log.e(debugTag, "has Focus");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.kiosk_mode_menu, menu);
        menu.findItem(R.id.logoutWorkerItem).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.unlockItem:
                unlockDevice();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void unlockDevice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.enter_admin_cred_dialog_title));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText admnUsernameEdt = new EditText(this);
        admnUsernameEdt.setHint(getResources().getString(R.string.admnusername_hint));
        layout.addView(admnUsernameEdt);

        final EditText admnPasswordEdt = new EditText(this);
        admnPasswordEdt.setHint(getResources().getString(R.string.admnpassword_hint));
        layout.addView(admnPasswordEdt);

        builder.setView(layout);
        builder.setPositiveButton(getResources().getString(R.string.unlock), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
//                Log.e(debugTag, edittext.getText().toString());
                if (admnUsernameEdt.getText().toString().equals(SharedPrefsUtl.getStringFlag(KioskModeActivity.this, getResources().getString(R.string.admin_username))) &&
                        admnPasswordEdt.getText().toString().equals(SharedPrefsUtl.getStringFlag(KioskModeActivity.this, getResources().getString(R.string.admin_password)))) {
                    SharedPrefsUtl.setBooleanPref(KioskModeActivity.this, getResources().getString(R.string.device_locked), false);
                    Intent intent = new Intent(KioskModeActivity.this, SplashScreen.class);
                    intent.putExtra(getResources().getString(R.string.action_type), 1020);
                    startActivity(intent);
                    finish();
                } else {
                    showSnackBrMsg(getResources().getString(R.string.username_password_invalid), mBinding.kioskModeLlt, Snackbar.LENGTH_SHORT);
                }
            }
        });
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    private void initializeView() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.kioskModeLlt, StationWorkersFrgmt.newInstance(stationId), getResources().getString(R.string.station_workers_frgmt))
                .commit();
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

    private void dismissDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) mAlertDialog.dismiss();
    }

}
