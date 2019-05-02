package kioskmode.com.epoptia.workstations;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import domain.com.epoptia.Constants;
import domain.com.epoptia.model.domain.DomainWorkStationModel;
import kioskmode.com.epoptia.base.BaseActivity;
import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.kioskmodetablet.KioskModeActivity;
import kioskmode.com.epoptia.R;
import kioskmode.com.epoptia.adapters.RecyclerViewAdapter;
import kioskmode.com.epoptia.databinding.ActivityWorkStationsBinding;
import kioskmode.com.epoptia.viewmodel.models.DeviceViewModel;
import kioskmode.com.epoptia.viewmodel.models.KioskModeViewModel;
import kioskmode.com.epoptia.viewmodel.models.NetworkStateViewModel;
import kioskmode.com.epoptia.viewmodel.models.WorkStationViewModel;
import kioskmode.com.epoptia.viewmodel.models.WorkStationsViewModel;
import kioskmode.com.epoptia.workstations.viewmodel.WorkStationsContract;
import kioskmode.com.epoptia.workstations.viewmodel.WorkStationsPhoneViewModel;
import kioskmode.com.epoptia.workstations.viewmodel.WorkStationsTabletViewModel;

/**
 * Created by giannis on 5/9/2017.
 */

public class WorkStationsActivity extends BaseActivity implements WorkStationsContract.View {

    //region Injections

    @Inject
    WorkStationsRetainFragment mWorkStationsRetainFragment;

    @Inject
    WorkStationsTabletViewModel mTabletViewModel;

    @Inject
    WorkStationsPhoneViewModel mPhoneViewModel;

    @Inject
    WorkStationsViewModel workStationsViewModel;

    @Inject
    WorkStationViewModel workStationViewModel;

    @Inject
    KioskModeViewModel kioskModeViewModel;

    @Inject
    DeviceViewModel deviceViewModel;

    //endregion

    //region Private Properties

    private WorkStationsContract.ViewModel mViewModel;

    private static final String debugTag = WorkStationsActivity.class.getSimpleName();

    //todo add constant
    private static final int PHONE_STATE = 1020;

    //todo add constant
    public static final int OVERLAY_PERMISSION_REQ_CODE = 4545;

    private ActivityWorkStationsBinding mBinding;

    private RecyclerViewAdapter rcvAdapter;

    private List<DomainWorkStationModel> workStations = new ArrayList<>();

    private WorkStationsClickListener workStationsPresenter;

    //endregion

    //region Lifecycle Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            deviceViewModel = intent.getExtras().getParcelable(getResources().getString(R.string.device_view_model_parcel));
            Log.e(debugTag, "INTENT GETEXTRAS NOT NULL");
            workStationsViewModel.setDeviceViewModel(deviceViewModel);
        }

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_work_stations);

        mBinding.setWorkStationsViewModel(workStationsViewModel);

        setSupportActionBar(mBinding.incltoolbar.toolbar);

        mBinding.incltoolbar.toolbarTitle.setText(getResources().getString(R.string.work_stations_title));

        retainViewModel();

        workStationsPresenter = new WorkStationsClickListener(this);

        int actionType = getIntent().getIntExtra(getResources().getString(R.string.action_type), 0);

        if (actionType == 1020) { //UNLOCK SCREEN
            PackageManager p = getPackageManager();
            ComponentName cN = new ComponentName(getApplicationContext(), KioskModeActivity.class);
            p.setComponentEnabledSetting(cN, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }

        mBinding.retryBtn.setOnClickListener(view -> mViewModel.loadWorkStations());
    }

    @Override
    protected void onStart() {
        super.onStart();

        //todo add usage comment
        super.onStartWithSavedInstanceState(null);
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
    protected void onDestroy() {
        super.onDestroy();

        Log.e(debugTag, "VIEW MODEL INSTANCE onDestroy!!!!!! => " + mViewModel);

        mWorkStationsRetainFragment.retainViewModel(mViewModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case OVERLAY_PERMISSION_REQ_CODE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (!Settings.canDrawOverlays(this)) {
                        Toast.makeText(this, "DomainClientModel cannot access system settings without this permission!App cannot be locked", Toast.LENGTH_SHORT).show();
                    } else {
                        mViewModel.lockDevice();
                    }
                }

                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        checkAppCanDrawOverlays();
                    }
                } else {
                    Toast.makeText(this, "PHONE STATE permission denied! App cannot be locked!", Toast.LENGTH_SHORT).show();
                }

                break;
            }
        }
    }

    //endregion

    //region Public Methods

    @Override
    public Lifecycle.ViewModel getViewModel() {
        if (workStationsViewModel == null || workStationsViewModel.getDeviceViewModel() == null) {
            return mViewModel;
        }

        //for the non first time check
        //get the already created view model (tablet or phone) instance
        if (mViewModel != null) {
            return mViewModel;
        }

        if (workStationsViewModel.getDeviceViewModel().getCategory() == Constants.PHONE) {
            mViewModel = mPhoneViewModel;
        } else {
            mViewModel = mTabletViewModel;
        }

        return mViewModel;
    }

    @Override
    public void setProcessing(boolean isProcessing) {
        workStationsViewModel.setErrorMsg(null);
        workStationsViewModel.setProcessing(isProcessing);
    }

    @Override
    public void loadWorkStationsOnSuccess(List<DomainWorkStationModel> domainWorkStations) {
        workStations = domainWorkStations;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(WorkStationsActivity.this);

        if (rcvAdapter == null) {
            mBinding.rclView.addItemDecoration(new DividerItemDecoration(WorkStationsActivity.this, DividerItemDecoration.VERTICAL));
        }

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

        mBinding.rclView.setLayoutManager(linearLayoutManager);
        mBinding.rclView.setNestedScrollingEnabled(false);
        mBinding.rclView.setAdapter(rcvAdapter);
    }

    @Override
    public void loadWorkStationsOnError(Throwable throwable) {
        this.workStationsViewModel.setErrorMsg(throwable.getMessage());

        showSnackBrMsg(throwable.getMessage(), mBinding.baseLlt, Snackbar.LENGTH_SHORT);
    }

    @Override
    public void enableLockDeviceDialog() {
        openLockDeviceDialog();
    }

    @Override
    public void startKioskModeActivity() {
        deviceViewModel.setModeState(Constants.KIOSK_MODE_STATE);

        kioskModeViewModel.setDeviceViewModel(deviceViewModel);
        kioskModeViewModel.setWorkStationViewModel(workStationViewModel);

        Bundle bundle = new Bundle();
        bundle.putParcelable(getResources().getString(R.string.kioskmode_view_model_parcel), kioskModeViewModel);

        Intent intent = new Intent(this, kioskmode.com.epoptia.kioskmode.KioskModeActivity.class);

        intent.putExtras(bundle);

        startActivity(intent);

        finish();
    }

    @Override
    public void startKioskModeActivityWhenAppIsNotTheDefaultLauncher() {
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

        deviceViewModel.setModeState(Constants.KIOSK_MODE_STATE);

        kioskModeViewModel.setDeviceViewModel(deviceViewModel);
        kioskModeViewModel.setWorkStationViewModel(workStationViewModel);

        Bundle bundle = new Bundle();
        bundle.putParcelable(getResources().getString(R.string.kioskmode_view_model_parcel), kioskModeViewModel);

        selector.putExtras(bundle);

        startActivity(selector);

        finish();
    }

    @Override
    public void setNetworkState(NetworkStateViewModel networkStateViewModel) {
        mBinding.setNetworkStateViewModel(networkStateViewModel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Menu menu1 = menu;

        getMenuInflater().inflate(R.menu.work_stations_menu, menu);

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWorkStationItemClick(View view) {
        int workStationId = workStations.get((int)view.getTag()).getId();
        String workStationName = workStations.get((int)view.getTag()).getName();

        workStationViewModel.setWorkStationId(workStationId);
        workStationViewModel.setWorkStationName(workStationName);

        mViewModel.selectWorkStation(workStationViewModel);
    }

    //endregion

    //region Private Methods

    private void retainViewModel() {
        if (getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.work_stations_retain_fragment)) == null) {

            getSupportFragmentManager().beginTransaction().add(mWorkStationsRetainFragment, getResources().getString(R.string.work_stations_retain_fragment)).commit();
        } else {
            mWorkStationsRetainFragment = (WorkStationsRetainFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.work_stations_retain_fragment));

            if (mWorkStationsRetainFragment == null) {
                return;
            }

            if (mWorkStationsRetainFragment.getViewModel() != null) mViewModel = mWorkStationsRetainFragment.getViewModel();

            Log.e(debugTag, "VIEW MODEL INSTANCE !!!!!! => " + mViewModel);
        }
    }

    private void checkReadPhoneStatePermissionGranted() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mViewModel.lockDevice();

        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                //todo check if condition is redundant
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PHONE_STATE);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PHONE_STATE);
                }
            } else {
                checkAppCanDrawOverlays();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkAppCanDrawOverlays() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));

            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        } else {
            mViewModel.lockDevice();
        }
    }

    //endregion

    private void openLockDeviceDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.lock_device_dialog_title))
                .setMessage(getResources().getString(R.string.lock_device_dialog_msg))
                .setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> checkReadPhoneStatePermissionGranted())
                .setNegativeButton(getResources().getString(R.string.no), null).show();
    }

}
