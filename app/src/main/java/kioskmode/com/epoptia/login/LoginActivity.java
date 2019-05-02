package kioskmode.com.epoptia.login;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import kioskmode.com.epoptia.R;
import kioskmode.com.epoptia.base.BaseActivity;
import kioskmode.com.epoptia.viewmodel.models.DeviceViewModel;
import kioskmode.com.epoptia.workstations.WorkStationsActivity;
import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.viewmodel.models.LoginViewModel;
import kioskmode.com.epoptia.databinding.ActivityLoginAdminBinding;

/**
 * Created by giannis on 5/9/2017.
 */

public class LoginActivity extends BaseActivity implements LoginContract.View {

    //region Injections

    @Inject
    LoginViewModel loginViewModel;

    @Inject
    LoginRetainFragment mLoginRetainFragment;

    @Inject
    LoginContract.ViewModel mViewModel;

    @Inject
    DeviceViewModel deviceViewModel;

    //endregion

    //region Private Properties

    private static final String debugTag = LoginActivity.class.getSimpleName();

    private ActivityLoginAdminBinding mBinding;

    private Bundle savedInstanceState;

    //endregion

    //region Lifecycle Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //todo add usage comment
        this.savedInstanceState = savedInstanceState;

        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            loginViewModel.setDeviceViewModel(intent.getExtras().getParcelable(getResources().getString(R.string.device_view_model_parcel)));
        }

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login_admin);

        mBinding.setLoginViewModel(loginViewModel);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        retainViewModel();

        mBinding.submitBtn.setOnClickListener(view -> {
            hideSoftKeyboard();

            mViewModel.submitForm(loginViewModel);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //todo add usage comment
        super.onStartWithSavedInstanceState(this.savedInstanceState);
    }

    // This callback is called only when there is a saved instance that is previously saved by using
    // onSaveInstanceState(). We restore some state in onCreate(), while we can optionally restore
    // other state here, possibly usable after onStart() has completed.
    // The savedInstanceState Bundle is same as the one used in onCreate().
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        bindLoginViewModelToViewModel(savedInstanceState.getParcelable(getResources().getString(R.string.login_view_model_parcel)));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(getResources().getString(R.string.login_view_model_parcel), loginViewModel);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mLoginRetainFragment.retainViewModel(mViewModel);
    }

    //endregion

    //region Public Methods

    @Override
    public Lifecycle.ViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public void setProcessing(boolean isProcessing) {
        this.loginViewModel.setProcessing(isProcessing);
    }

    @Override
    public void validateClientSubDomainOnSuccess() {
        loginViewModel.setSubDomainIsValid(true);
    }

    @Override
    public void validateClientSubDomainOnError(Throwable throwable) {
        showSnackBrMsg(throwable.getMessage(), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
    }

    @Override
    public void loginAdminOnSuccess() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(getResources().getString(R.string.device_view_model_parcel), loginViewModel.getDeviceViewModel());

        Intent intent = new Intent(LoginActivity.this, WorkStationsActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);

        finish();
    }

    @Override
    public void loginAdminOnError(Throwable throwable) {
        showSnackBrMsg(throwable.getMessage(), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
    }

    //todo check why object assignment is not triggering the view bindings
    @Override
    public void bindLoginViewModelToViewModel(LoginViewModel loginViewModel) {
        if (loginViewModel == null) {
            return;
        }

        this.loginViewModel.setSubDomain(loginViewModel.getSubDomain());
        this.loginViewModel.setSubDomainIsValid(loginViewModel.isSubDomainIsValid());
        this.loginViewModel.setUsername(loginViewModel.getUsername());
        this.loginViewModel.setPassword(loginViewModel.getPassword());

        //login view model sent from loginViewModel and device model is null
        //loginVIewModel knows anything about device view model
        if (loginViewModel.getDeviceViewModel() != null) {
            this.loginViewModel.setDeviceViewModel(loginViewModel.getDeviceViewModel());
        }
    }

    //endregion

    //region Private Methods

    private void retainViewModel() {
        if (getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.login_activity_retain_fragment)) == null) {

            getSupportFragmentManager().beginTransaction().add(mLoginRetainFragment, getResources().getString(R.string.login_activity_retain_fragment)).commit();
        } else {
            mLoginRetainFragment = (LoginRetainFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.login_activity_retain_fragment));

            if (mLoginRetainFragment.getViewModel() != null) mViewModel = mLoginRetainFragment.getViewModel();
        }
    }

    //endregion

}
