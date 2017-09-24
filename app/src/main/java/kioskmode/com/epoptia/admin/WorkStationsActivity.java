package kioskmode.com.epoptia.admin;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kioskmode.com.epoptia.BaseActivity;
import kioskmode.com.epoptia.POJO.GetWorkStationsRequest;
import kioskmode.com.epoptia.POJO.GetWorkStationsResponse;
import kioskmode.com.epoptia.POJO.ValidateAdminResponse;
import kioskmode.com.epoptia.kioskmode.KioskModeActivity;
import kioskmode.com.epoptia.POJO.WorkStation;
import kioskmode.com.epoptia.R;
import kioskmode.com.epoptia.adapters.RecyclerViewAdapter;
import kioskmode.com.epoptia.databinding.ActivityWorkStationsBinding;
import kioskmode.com.epoptia.retrofit.APIClient;
import kioskmode.com.epoptia.retrofit.APIInterface;
import kioskmode.com.epoptia.utls.SharedPrefsUtl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by giannis on 5/9/2017.
 */

public class WorkStationsActivity extends BaseActivity implements WordStationsContract.View{

    private static final String debugTag = WorkStationsActivity.class.getSimpleName();
    private static final int PHONE_STATE = 1020;
    public static final int OVERLAY_PERMISSION_REQ_CODE = 4545;
    private ActivityWorkStationsBinding mBinding;
    private RecyclerViewAdapter rcvAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<WorkStation> workStations = new ArrayList<>();
    private WorkStationsPresenter workStationsPresenter;
    private Menu menu;
    private int actionType, stationId;
    private APIInterface apiInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_work_stations);
        workStationsPresenter = new WorkStationsPresenter(this);
        apiInterface = APIClient.getClient(SharedPrefsUtl.getStringFlag(this, getResources().getString(R.string.subdomain))).create(APIInterface.class);

        if (getSupportActionBar() != null) {
            setTitle(getResources().getString(R.string.work_stations_title));
        }
//        intent = getIntent();
//        Log.e(debugTag, "onCreate " + intent);

        actionType = getIntent().getIntExtra(getResources().getString(R.string.action_type), 0);
        if (actionType == 1020) { //UNLOCK SCREEN
            Log.e(debugTag, "unlock screen FROM ON CREATE");
            PackageManager p = getPackageManager();
            ComponentName cN = new ComponentName(getApplicationContext(), KioskModeActivity.class);
            p.setComponentEnabledSetting(cN, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }
        check();
        mBinding.retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });
    }

    private void check() {
        if (isNetworkAvailable()) {
            if (mBinding.getHaserror()) mBinding.setHaserror(false);
            initializeWorkStations();
        } else {
            mBinding.setHaserror(true);
            mBinding.setErrortext(getResources().getString(R.string.no_connection));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.work_stations_menu, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBaseViewClick(View view) {
        Log.e(debugTag, view.getTag() + " ID");
        stationId = workStations.get((int)view.getTag()).getId();
        lockDeviceDialog();
    }

    private void initializeWorkStations() {
        final GetWorkStationsRequest request = new GetWorkStationsRequest();
        request.setAction("get_workstations");
        String accessToken = SharedPrefsUtl.getStringFlag(this, getResources().getString(R.string.access_token));
        request.setAccess_token(accessToken);
        mBinding.setProcessing(true);
        /**
         GET List Resources
         **/
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Call<GetWorkStationsResponse> responseCall = apiInterface.getWorkStations(request);
                responseCall.enqueue(new Callback<GetWorkStationsResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<GetWorkStationsResponse> call, @NonNull Response<GetWorkStationsResponse> response) {
//                        Log.e(debugTag, response.body().getCode()+ " CODE");
                        mBinding.setProcessing(false);
                        if (response.body() != null) {
                            if (response.body().getCode() == 200) {
                                workStations = response.body().getData();
                                linearLayoutManager = new LinearLayoutManager(WorkStationsActivity.this);
                                if (rcvAdapter == null)mBinding.rcv.addItemDecoration(new DividerItemDecoration(WorkStationsActivity.this, DividerItemDecoration.VERTICAL));
                                rcvAdapter = new RecyclerViewAdapter(R.layout.work_stations_rcv_row) {
                                    @Override
                                    protected Object getObjForPosition(int position, ViewDataBinding mBinding) {
                                        return workStations.get(position);
                                    }
                                    @Override
                                    protected int getLayoutIdForPosition(int position) {
                                        return R.layout.work_stations_rcv_row;

                                    }
                                    @Override
                                    protected int getTotalItems() {
                                        return workStations.size();
                                    }

                                    @Override
                                    protected Object getClickListenerObject() {
                                        return workStationsPresenter;
                                    }
                                };
                                mBinding.rcv.setLayoutManager(linearLayoutManager);
                                mBinding.rcv.setNestedScrollingEnabled(false);
                                mBinding.rcv.setAdapter(rcvAdapter);
                            } else {
                                mBinding.setHaserror(true);
                                mBinding.setErrortext(getResources().getString(R.string.error));
//                        showSnackBrMsg(getResources().getString(R.string.error), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<GetWorkStationsResponse> call, @NonNull Throwable t) {
                        mBinding.setProcessing(false);
                        mBinding.setHaserror(true);
                        mBinding.setErrortext(getResources().getString(R.string.error));
                    }
                });
            }
        }, 600);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        canDrawOverlays();
                    }
                } else {
                    Toast.makeText(this, "PHONE STATE permission denied! App cannot be locked!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
//            case CALL_PHONE: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
//                        Toast.makeText(this, "CALL_PHONE Permission granted!", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(this, "No CALL_PHONE permission granted!", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.e(debugTag, "onActivityResult Result code => " + resultCode + " Request code => " +requestCode);
        switch (requestCode) {
            case OVERLAY_PERMISSION_REQ_CODE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(this)) {
                        Toast.makeText(this, "User cannot access system settings without this permission!App cannot be locked", Toast.LENGTH_SHORT).show();
                    } else {
                        checkAppIsDefaultLauncher();
                    }
                }
                break;
        }
    }

    private void lockDeviceDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.lock_device_dialog_title))
                .setMessage(getResources().getString(R.string.lock_device_dialog_msg))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lockDevice();
                    }
                }).setNegativeButton(getResources().getString(R.string.no), null).show();
    }

    private void lockDevice() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            checkAppIsDefaultLauncher();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PHONE_STATE);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PHONE_STATE);
                }
            } else {
                canDrawOverlays();
            }
        }
    }

    private boolean isMyAppLauncherDefault() {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_MAIN);
        filter.addCategory(Intent.CATEGORY_HOME);

        List<IntentFilter> filters = new ArrayList<IntentFilter>();
        filters.add(filter);

        final String myPackageName = getPackageName();
        List<ComponentName> activities = new ArrayList<ComponentName>();
        final PackageManager packageManager = (PackageManager) getPackageManager();

        // You can use name of your package here as third argument
        packageManager.getPreferredActivities(filters, activities, null);

        for (ComponentName activity : activities) {
//            Log.e(debugTag, activity.getPackageName() +" ACTIVITIES");
            if (myPackageName.equals(activity.getPackageName())) {
//                Log.e(debugTag, myPackageName +" MY PACKAGE");
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void canDrawOverlays() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        } else {
            checkAppIsDefaultLauncher();
        }
    }

    private void checkAppIsDefaultLauncher() {
        if (!isMyAppLauncherDefault()) {
            Log.e(debugTag, "my app is not default launcher");
            PackageManager p = getPackageManager();
            ComponentName cN = new ComponentName(getApplicationContext(), KioskModeActivity.class);
            p.setComponentEnabledSetting(cN, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

            Intent selector = new Intent(Intent.ACTION_MAIN);
            selector.addCategory(Intent.CATEGORY_HOME);

            PackageManager localPackageManager = getPackageManager();
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            String str = localPackageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY).activityInfo.packageName;

            if (!str.equals("kioskmode.com.epoptia")) {
//                Intent intnt = new Intent(Intent.ACTION_MAIN);
//                intnt.addCategory(Intent.CATEGORY_HOME);
//                intnt.addCategory(Intent.CATEGORY_DEFAULT);
//                intnt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                startActivity(Intent.createChooser(intnt, "Set as default to enable Kiosk Mode"));
            }

            Log.e("Curt launcher Package:", str);
            Bundle bundle = new Bundle();
            bundle.putInt("station_id", stationId);
            selector.putExtras(bundle);
            startActivity(selector);
        } else {
            Log.e(debugTag, "my app is default launcher");
            PackageManager p = getPackageManager();
            ComponentName cN = new ComponentName(getApplicationContext(), KioskModeActivity.class);
            p.setComponentEnabledSetting(cN, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

            Bundle bundle = new Bundle();
            bundle.putInt("station_id", stationId);
            startActivity(new Intent(this, KioskModeActivity.class).putExtras(bundle));
        }
        finish();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
