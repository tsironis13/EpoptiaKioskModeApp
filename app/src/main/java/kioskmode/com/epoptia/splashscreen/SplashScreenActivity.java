package kioskmode.com.epoptia.splashscreen;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import javax.inject.Inject;

import kioskmode.com.epoptia.R;
import kioskmode.com.epoptia.base.BaseActivity;
import kioskmode.com.epoptia.databinding.SplashScreenBinding;
import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.login.LoginActivity;
import kioskmode.com.epoptia.viewmodel.models.DeviceViewModel;
import kioskmode.com.epoptia.viewmodel.models.KioskModeViewModel;
import kioskmode.com.epoptia.workstations.WorkStationsActivity;
import kioskmode.com.epoptia.kioskmodetablet.KioskModeActivity;

public class SplashScreenActivity extends BaseActivity implements SplashScreenContract.View {

    //region Injections

    @Inject
    SplashScreenContract.ViewModel mViewModel;

    @Inject
    KioskModeViewModel kioskModeViewModel;

    //endregion

    //region Private Properties

    private SplashScreenBinding mBinding;

    private Intent intent;

    private int actionType;

    //endregion

    //region Lifecycle Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.splash_screen);

        mBinding.retryBtn.setOnClickListener(view -> mViewModel.saveDeviceCategory());
    }

    @Override
    protected void onStart() {
        super.onStart();

        super.onStartWithSavedInstanceState(null);
    }

    //endregion

    //region Public Methods

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
//            actionType = intent.getExtras().getInt(getResources().getString(R.string.action_type));
            actionType = intent.getIntExtra(getResources().getString(R.string.action_type), 0);

            if (actionType == 1020) { //UNLOCK SCREEN
                PackageManager p = getPackageManager();
                ComponentName cN = new ComponentName(getApplicationContext(), KioskModeActivity.class);
                p.setComponentEnabledSetting(cN, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public Lifecycle.ViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public void onSaveDeviceCategorySuccess() {
        mViewModel.checkUserAndDeviceState();

        //initializeApp();
        intent = getIntent();

        actionType = intent.getIntExtra(getResources().getString(R.string.action_type), 0);

        if (actionType == 1020) { //UNLOCK SCREEN
            PackageManager p = getPackageManager();
            ComponentName cN = new ComponentName(getApplicationContext(), KioskModeActivity.class);
            p.setComponentEnabledSetting(cN, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }
    }

    @Override
    public void onError() {
        mBinding.setErrorOccurred(true);
    }

    @Override
    public void navigateUserToLoginScreen(DeviceViewModel deviceViewModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(getResources().getString(R.string.device_view_model_parcel), deviceViewModel);

        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void navigateUserToWorkStationsScreen(DeviceViewModel deviceViewModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(getResources().getString(R.string.device_view_model_parcel), deviceViewModel);

        Intent intent = new Intent(SplashScreenActivity.this, WorkStationsActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void navigateUserToKioskModeScreen(DeviceViewModel deviceViewModel) {
        kioskModeViewModel.setDeviceViewModel(deviceViewModel);

        Bundle bundle = new Bundle();
        bundle.putParcelable(getResources().getString(R.string.kioskmode_view_model_parcel), kioskModeViewModel);

        Intent intent = new Intent(this, kioskmode.com.epoptia.kioskmode.KioskModeActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    //endregion

}
