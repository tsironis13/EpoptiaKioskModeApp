package kioskmode.com.epoptia.kioskmodetablet;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.databinding.DataBindingUtil;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import kioskmode.com.epoptia.splashscreen.SplashScreenActivity;
import kioskmode.com.epoptia.base.BaseActivity;
import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.pojo.UnlockDeviceRequest;
import kioskmode.com.epoptia.pojo.UnlockDeviceResponse;
import kioskmode.com.epoptia.R;
import kioskmode.com.epoptia.app.utils.KioskService;
import kioskmode.com.epoptia.databinding.ActivityKioskModeBinding;
import kioskmode.com.epoptia.kioskmodetablet.stationworkers.StationWorkersFrgmt;
import kioskmode.com.epoptia.kioskmodetablet.systemdashboard.SystemDashboardFrgmt;
import kioskmode.com.epoptia.retrofit.APIClient;
import kioskmode.com.epoptia.retrofit.APIInterface;
import kioskmode.com.epoptia.utls.SharedPrefsUtl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private int stationId, uisystemvisibility, delay = 1000;
    private String stationName, action;
    private AlertDialog mAlertDialog;
    private String cookie, url;
    private APIInterface apiInterface;
    private Handler handler;
    private Runnable runnable;
    private String workerUsername;
    private int workerId;
    private int count = 0;
    private long startMillis=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler();

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_kiosk_mode);

        setSupportActionBar(mBinding.incltoolbar.toolbar);

        apiInterface = APIClient.getClient(SharedPrefsUtl.getStringFlag(this, getResources().getString(R.string.subdomain))).create(APIInterface.class);

        mDecorView = getWindow().getDecorView();

        if (savedInstanceState != null) {
            stationId = savedInstanceState.getInt("station_id");
            stationName = savedInstanceState.getString("station_name");
            cookie = savedInstanceState.getString("cookie");
            url = savedInstanceState.getString("url");
            action = savedInstanceState.getString("action");

            if (savedInstanceState.getInt(getResources().getString(R.string.top_backstack_entry_id)) == topBackStackEntryId && !SharedPrefsUtl.getStringFlag(this, "cookie").equals("cookie")) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.kioskModeLlt, SystemDashboardFrgmt.newInstance(stationId, cookie, url, stationName, workerUsername, workerId, action), getResources().getString(R.string.system_dahsboard_frgmt))
                        .addToBackStack(getResources().getString(R.string.system_dahsboard_frgmt))
                        .commit();
            }
            action = "device";
        } else {
            action = "device";
            if (getIntent().getExtras() != null) {
                stationId = getIntent().getExtras().getInt("station_id");
                stationName = getIntent().getExtras().getString("station_name");
            }
            initializeView();
        }
//        LifecycleHandler.get(this).addListener(this);
        kioskService = new KioskService();

        SharedPrefsUtl.setBooleanPref(this, getResources().getString(R.string.device_locked), true);

        immersiveMode();

        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        uisystemvisibility = visibility;
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        preventStatusBarExpansion(this);

        handler.postDelayed(new Runnable() {
            public void run() {
                //do something
                if (uisystemvisibility == 0) immersiveMode();

                runnable=this;

                handler.postDelayed(runnable, delay);
            }
        }, delay);

        mBinding.incltoolbar.logoImgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    //get system current milliseconds
                    long time= System.currentTimeMillis();

                    //if it is the first time, or if it has been more than 3 seconds since the first tap ( so it is like a new try), we reset everything
                    if (startMillis==0 || (time-startMillis> 3000) ) {
                        startMillis=time;
                        count=1;
                    }
                    //it is not the first, and it has been less than 3 seconds since the first
                    else{ //  time-startMillis< 3000
                        count++;
                    }
                    if (count==5) {
                        SharedPrefsUtl.removeStringkey(KioskModeActivity.this, "cookie");
                        SharedPrefsUtl.setBooleanPref(KioskModeActivity.this, getResources().getString(R.string.device_locked), false);
                        Intent intent = new Intent(KioskModeActivity.this, SplashScreenActivity.class);
                        intent.putExtra(getResources().getString(R.string.action_type), 1020);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);

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
        outState.putString("cookie", cookie);
        outState.putString("station_name", stationName);
        outState.putString("url", url);
        outState.putString("action", action);
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName().equals(getResources().getString(R.string.system_dahsboard_frgmt)))
                outState.putInt(getResources().getString(R.string.top_backstack_entry_id), topBackStackEntryId);
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
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public Lifecycle.ViewModel getViewModel() {
        return null;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.kiosk_mode_tablet_menu, menu);
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

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public void setUrl(String url) {
        this.url = url;
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
                UnlockDeviceRequest request = new UnlockDeviceRequest();
                request.setAccess_token(SharedPrefsUtl.getStringFlag(KioskModeActivity.this, getResources().getString(R.string.access_token)));
                request.setAction("unlock_device");
                request.setCustomer_domain(SharedPrefsUtl.getStringFlag(KioskModeActivity.this, getResources().getString(R.string.subdomain)));
                request.setUsername(admnUsernameEdt.getText().toString());
                request.setPassword(admnPasswordEdt.getText().toString());
                Call<UnlockDeviceResponse> responseCall = apiInterface.unlockDevice(request);

                if (isNetworkAvailable()) {
                    responseCall.enqueue(new Callback<UnlockDeviceResponse>() {
                        @Override
                        public void onResponse(Call<UnlockDeviceResponse> call, Response<UnlockDeviceResponse> response) {
                            if (response.body().getCode() == 200) {
                                SharedPrefsUtl.removeStringkey(KioskModeActivity.this, "cookie");
                                SharedPrefsUtl.setBooleanPref(KioskModeActivity.this, getResources().getString(R.string.device_locked), false);
                                Intent intent = new Intent(KioskModeActivity.this, SplashScreenActivity.class);
                                intent.putExtra(getResources().getString(R.string.action_type), 1020);
                                startActivity(intent);
                                finish();
                            } else {
                                showSnackBrMsg(getResources().getString(R.string.username_password_invalid), mBinding.kioskModeLlt, Snackbar.LENGTH_SHORT);
                            }
                        }

                        @Override
                        public void onFailure(Call<UnlockDeviceResponse> call, Throwable t) {
                            showSnackBrMsg(getResources().getString(R.string.error), mBinding.kioskModeLlt, Snackbar.LENGTH_SHORT);
                        }
                    });
                } else {
                    showSnackBrMsg(getResources().getString(R.string.no_connection), mBinding.kioskModeLlt, Snackbar.LENGTH_SHORT);
                }
            }
        });
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    private void initializeView() {
        if (SharedPrefsUtl.getIntFlag(getApplicationContext(), getResources().getString(R.string.workstation_id)) != 0) {


            int inmemoryStationId = SharedPrefsUtl.getIntFlag(getApplicationContext(), getResources().getString(R.string.workstation_id));


            if (stationId == 0) stationId = inmemoryStationId;
//            if (inmemoryStationId == stationId) stationId = inmemoryStationId;
        }
        String prefsCookie = SharedPrefsUtl.getStringFlag(getApplicationContext(), "cookie");

        String prefsUrl = SharedPrefsUtl.getStringFlag(getApplicationContext(), "end_url");

        String workeruser = SharedPrefsUtl.getStringFlag(getApplicationContext(), "worker_username");

        workerId = SharedPrefsUtl.getIntFlag(getApplicationContext(), "worker_id");

        workerUsername = workeruser;

        if (!SharedPrefsUtl.getStringFlag(getApplicationContext(), "cookie").equals("cookie")) {
            cookie = prefsCookie;
            url = prefsUrl;

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.kioskModeLlt, SystemDashboardFrgmt.newInstance(stationId, cookie, url, stationName, workeruser, workerId,null), getResources().getString(R.string.system_dahsboard_frgmt))
                    .addToBackStack(getResources().getString(R.string.system_dahsboard_frgmt))
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.kioskModeLlt, StationWorkersFrgmt.newInstance(stationId, stationName), getResources().getString(R.string.station_workers_frgmt))
                    .commit();
        }
    }

    private void immersiveMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    public static void preventStatusBarExpansion(Context context) {
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

    public TextView getToolbarTextViewTitle() {
        return mBinding.incltoolbar.toolbarTitle;
    }

//    public TextView getToolbarTextViewUsernameLeft() {
//        return mBinding.incltoolbar.usernameLeftTtv;
//    }

    public TextView getToolbarTextViewUsernameRight() {
        return mBinding.incltoolbar.usernameRightTtv;
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

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm != null ? cm.getActiveNetworkInfo() : null) != null && cm.getActiveNetworkInfo().isConnected();
    }

}
