package kioskmode.com.epoptia.splashscreen;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import javax.inject.Inject;

import kioskmode.com.epoptia.R;
import kioskmode.com.epoptia.base.BaseActivity;
import kioskmode.com.epoptia.databinding.SplashScreenBinding;
import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.login.LoginActivity;
import kioskmode.com.epoptia.admin.WorkStationsActivity;
import kioskmode.com.epoptia.kioskmodetablet.KioskModeActivity;
import kioskmode.com.epoptia.utls.SharedPrefsUtl;

public class SplashScreenActivity extends BaseActivity implements SplashScreenContract.View {

    //region Injections

    @Inject
    SplashScreenContract.ViewModel mViewModel;

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
        mBinding.setErrorOccurred(false);

        mViewModel.saveDeviceCategory();

        mBinding.retryBtn.setOnClickListener(view -> mViewModel.saveDeviceCategory());
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
        mViewModel.checkUserIsAuthenticated();

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
    public void navigateUserToLoginScreen() {
        startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
    }

    @Override
    public void navigateUserToWorkStationsScreen() {
        startActivity(new Intent(SplashScreenActivity.this, WorkStationsActivity.class));
    }

    @Override
    public void navigateUserToKioskModeScreen() {
        //todo change
        if (getResources().getConfiguration().smallestScreenWidthDp >= 600) {
            startActivity(new Intent(this, KioskModeActivity.class));
        } else {
            startActivity(new Intent(this, kioskmode.com.epoptia.kioskmodephone.KioskModeActivity.class));
        }
    }

    //endregion

}
