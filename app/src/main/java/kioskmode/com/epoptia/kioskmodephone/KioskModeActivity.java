package kioskmode.com.epoptia.kioskmodephone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.TextView;

import kioskmode.com.epoptia.base.BaseActivity;
import kioskmode.com.epoptia.R;
import kioskmode.com.epoptia.admin.WorkStationsActivity;
import kioskmode.com.epoptia.app.utils.KioskService;
import kioskmode.com.epoptia.databinding.ActivityKioskModeBinding;
import kioskmode.com.epoptia.kioskmodephone.stationworkers.StationWorkersFrgmt;
import kioskmode.com.epoptia.kioskmodephone.systemdashboard.SystemDashboardFrgmt;
import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.retrofit.APIClient;
import kioskmode.com.epoptia.retrofit.APIInterface;
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
    //private View mDecorView;
    private static final int topBackStackEntryId = 2060;
    private int stationId, uisystemvisibility, delay = 1000;
    private String stationName;
    private AlertDialog mAlertDialog;
    private String cookie, url;
    private APIInterface apiInterface;
    private Handler handler;
    private Runnable runnable;
    private String workerUsername;
    private int count = 0;
    private int workerId;
    private long startMillis=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        saveInstanceStateCalled = false;
        handler = new Handler();
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_kiosk_mode);
        setSupportActionBar(mBinding.incltoolbar.toolbar);
        apiInterface = APIClient.getClient(SharedPrefsUtl.getStringFlag(this, getResources().getString(R.string.subdomain))).create(APIInterface.class);
        //mDecorView = getWindow().getDecorView();
        if (savedInstanceState != null) {
            stationId = savedInstanceState.getInt("station_id");
            stationName = savedInstanceState.getString("station_name");
            cookie = savedInstanceState.getString("cookie");
            url = savedInstanceState.getString("url");
            if (savedInstanceState.getInt(getResources().getString(R.string.top_backstack_entry_id)) == topBackStackEntryId && !SharedPrefsUtl.getStringFlag(this, "cookie").equals("cookie")) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.kioskModeLlt, SystemDashboardFrgmt.newInstance(stationId, cookie, url, stationName, workerUsername, workerId), getResources().getString(R.string.system_dahsboard_frgmt))
                        .addToBackStack(getResources().getString(R.string.system_dahsboard_frgmt))
                        .commit();
            }
        } else {
            if (getIntent().getExtras() != null) {
                stationId = getIntent().getExtras().getInt("station_id");
                stationName = getIntent().getExtras().getString("station_name");
            }
            initializeView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("station_id", stationId);
        outState.putString("cookie", cookie);
        outState.putString("station_name", stationName);
        outState.putString("url", url);
//        outState.putString("url", "");
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
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

        } else {
            startActivity(new Intent(this, WorkStationsActivity.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }

    @Override
    public Lifecycle.ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.kiosk_mode_phone_menu, menu);
        menu.findItem(R.id.logoutWorkerItem).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
                    .replace(R.id.kioskModeLlt, SystemDashboardFrgmt.newInstance(stationId, cookie, url, stationName, workeruser, workerId), getResources().getString(R.string.system_dahsboard_frgmt))
                    .addToBackStack(getResources().getString(R.string.system_dahsboard_frgmt))
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.kioskModeLlt, StationWorkersFrgmt.newInstance(stationId, stationName), getResources().getString(R.string.station_workers_frgmt))
                    .commit();
        }
    }

    public TextView getToolbarTextViewTitle() {
        return mBinding.incltoolbar.toolbarTitle;
    }

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

}
