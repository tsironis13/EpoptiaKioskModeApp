package kioskmode.com.epoptia.kioskmode;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import domain.com.epoptia.Constants;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kioskmode.com.epoptia.R;
import kioskmode.com.epoptia.base.BaseActivity;
import kioskmode.com.epoptia.databinding.ActivityTodochangenamekioskmodeBinding;
import kioskmode.com.epoptia.kioskmode.viewmodel.KioskModeContract;
import kioskmode.com.epoptia.kioskmode.viewmodel.KioskModePhoneViewModel;
import kioskmode.com.epoptia.kioskmode.viewmodel.KioskModeTabletViewModel;
import kioskmode.com.epoptia.kioskmode.workers.StationWorkersFragment;
import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.splashscreen.SplashScreenActivity;
import kioskmode.com.epoptia.viewmodel.models.AdminDetailsViewModel;
import kioskmode.com.epoptia.viewmodel.models.DeviceViewModel;
import kioskmode.com.epoptia.viewmodel.models.KioskModeViewModel;
import kioskmode.com.epoptia.viewmodel.models.StationWorkersViewModel;
import kioskmode.com.epoptia.viewmodel.models.WorkStationViewModel;

public class KioskModeActivity extends BaseActivity implements KioskModeContract.View {

    //region Injections

    @Inject
    KioskModeRetainFragment mKioskModeRetainFragment;

    @Inject
    KioskModeTabletViewModel mTabletViewModel;

    @Inject
    KioskModePhoneViewModel mPhoneViewModel;

    @Inject
    KioskModeViewModel kioskModeViewModel;

    @Inject
    DeviceViewModel deviceViewModel;

    @Inject
    AdminDetailsViewModel adminDetailsViewModel;

    @Inject
    StationWorkersViewModel stationWorkersViewModel;

    //endregion

    //region Private Properties

    private KioskModeContract.ViewModel mViewModel;

    private ActivityTodochangenamekioskmodeBinding mBinding;

    private Disposable immersiveModeDisposable;

    private int uiSystemVisibility;

    private AlertDialog mAlertDialog;

    //endregion

    //region Lifecycle Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            kioskModeViewModel = intent.getExtras().getParcelable(getResources().getString(R.string.kioskmode_view_model_parcel));

        }

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_todochangenamekioskmode);

        setSupportActionBar(mBinding.incltoolbar.toolbar);

        retainViewModel();

        Completable.complete()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(this::enableImmersiveMode)
                .subscribe();

        getWindow()
                .getDecorView()
                .setOnSystemUiVisibilityChangeListener
                    (visibility -> uiSystemVisibility = visibility);
    }

    @Override
    protected void onStart() {
        super.onStart();

        super.onStartWithSavedInstanceState(null);
    }

    @Override
    protected void onResume() {
        super.onResume();

        immersiveModeDisposable = Observable
                                        .interval(1, TimeUnit.SECONDS)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(internal -> {
                                            if (uiSystemVisibility == 0) enableImmersiveMode();
                                        });

        mBinding.incltoolbar.logoImgv.setOnClickListener(v -> mViewModel.unlockDeviceFromAppLogo());
    }

    @Override
    protected void onPause() {
        super.onPause();

        immersiveModeDisposable.dispose();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        dismissDialog();

        mKioskModeRetainFragment.retainViewModel(mViewModel);
    }

    //do nothing
    @Override
    public void onBackPressed() {}

    //on Recents button pressed
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(mViewModel.getKioskModeMenuLayout(), menu);

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

    //endregion

    //region Public Methods

    public void setToolbarTitle(String title) {
        mBinding.incltoolbar.toolbarTitle.setText(title);
    }

    public void setToolbarUsernameTitle(String username) {
        mBinding.incltoolbar.usernameRightTtv.setText(username);
    }

    @Override
    public Lifecycle.ViewModel getViewModel() {
        if (kioskModeViewModel == null || kioskModeViewModel.getDeviceViewModel() == null) {
            return mViewModel;
        }

        //for the non first time check
        //get the already created view model (tablet or phone) instance
        if (mViewModel != null) {
            return mViewModel;
        }

        if (kioskModeViewModel.getDeviceViewModel().getCategory() == Constants.PHONE) {
            mViewModel = mPhoneViewModel;
        } else {
            mViewModel = mTabletViewModel;
        }

        return mViewModel;
    }

    @Override
    public void deviceUnlocked() {
        Intent intent = new Intent(KioskModeActivity.this, SplashScreenActivity.class);

        startActivity(intent);

        finish();
    }

    @Override
    public void navigateUserToStationWorkersScreen(WorkStationViewModel workStationViewModel) {
        if (workStationViewModel == null) {
            return;
        }

        stationWorkersViewModel.setWorkStationViewModel(workStationViewModel);

        getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.kioskModeLlt, StationWorkersFragment.newInstance(stationWorkersViewModel), getResources().getString(R.string.station_workers_frgmt))
                            .commit();
    }

    @Override
    public void navigateUserToWorkerPanelScreen() {



    }

    //endregion

    //region Private Methods

    private void retainViewModel() {
        if (getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.kioskmode_retain_fragment)) == null) {

            getSupportFragmentManager().beginTransaction().add(mKioskModeRetainFragment, getResources().getString(R.string.kioskmode_retain_fragment)).commit();
        } else {
            mKioskModeRetainFragment = (KioskModeRetainFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.kioskmode_retain_fragment));

            if (mKioskModeRetainFragment == null) {
                return;
            }

            if (mKioskModeRetainFragment.getViewModel() != null) mViewModel = mKioskModeRetainFragment.getViewModel();
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

        builder.setPositiveButton(getResources().getString(R.string.unlock), (dialog, whichButton) -> {
            adminDetailsViewModel.setUsername(admnUsernameEdt.getText().toString());
            adminDetailsViewModel.setPassword(admnPasswordEdt.getText().toString());

            mViewModel.unlockDevice(adminDetailsViewModel);
        });

        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    private void dismissDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }

    //endregion

}
