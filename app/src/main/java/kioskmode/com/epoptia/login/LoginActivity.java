package kioskmode.com.epoptia.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import javax.inject.Inject;

import domain.com.epoptia.interfaces.DeviceCategory;
import kioskmode.com.epoptia.R;
import kioskmode.com.epoptia.base.BaseActivity;
import kioskmode.com.epoptia.admin.WorkStationsActivity;
import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.login.model.LoginViewModel;
import kioskmode.com.epoptia.pojo.ValidateAdminRequest;
import kioskmode.com.epoptia.pojo.ValidateAdminResponse;
import kioskmode.com.epoptia.pojo.ValidateCustomerDomainRequest;
import kioskmode.com.epoptia.pojo.ValidateCustomerDomainResponse;
import kioskmode.com.epoptia.databinding.ActivityLoginAdminBinding;
import kioskmode.com.epoptia.retrofit.APIClient;
import kioskmode.com.epoptia.retrofit.APIInterface;
import kioskmode.com.epoptia.utls.SharedPrefsUtl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by giannis on 5/9/2017.
 */

public class LoginActivity extends BaseActivity implements LoginContract.View, DeviceCategory {

    //region Injections

    @Inject
    LoginViewModel loginViewModel;

    @Inject
    LoginRetainFragment mLoginRetainFragment;

    @Inject
    LoginContract.ViewModel mViewModel;

    //endregion

    //region Private Properties

    private static final String debugTag = LoginActivity.class.getSimpleName();
    private ActivityLoginAdminBinding mBinding;
    private APIInterface apiInterface;

    //endregion

    //region Lifecycle Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login_admin);

        mBinding.setLoginViewModel(loginViewModel);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        initializeViewModel();

        mBinding.submitBtn.setOnClickListener(view -> {
            hideSoftKeyboard();

            mViewModel.submitForm(loginViewModel);
        });
    }

    // This callback is called only when there is a saved instance that is previously saved by using
    // onSaveInstanceState(). We restore some state in onCreate(), while we can optionally restore
    // other state here, possibly usable after onStart() has completed.
    // The savedInstanceState Bundle is same as the one used in onCreate().
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        bindViewModel(savedInstanceState.getParcelable("loginViewModel"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("loginViewModel", loginViewModel);

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
    public void deviceIsPhone() {
        mBinding.deviceCategoryTtv.setText(getResources().getString(R.string.device_is_phone));
    }

    @Override
    public void deviceIsTablet() {
        mBinding.deviceCategoryTtv.setText(getResources().getString(R.string.device_is_tablet));
    }

    @Override
    public void initializeSubDomain(LoginViewModel loginViewModel) {
        if (loginViewModel == null) {
            return;
        }

        loginViewModel.setSubDomain(loginViewModel.getSubDomain());
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
        startActivity(new Intent(LoginActivity.this, WorkStationsActivity.class));

        finish();
    }

    @Override
    public void loginAdminOnError(Throwable throwable) {
        showSnackBrMsg(throwable.getMessage(), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
    }

    //endregion

    //region Private Methods

    //todo check why object assignment is not triggering the view bindings
    private void bindViewModel(LoginViewModel loginViewModel) {
        if (loginViewModel == null) {
            return;
        }

        this.loginViewModel.setSubDomain(loginViewModel.getSubDomain());
        this.loginViewModel.setSubDomainIsValid(loginViewModel.isSubDomainIsValid());
        this.loginViewModel.setUsername(loginViewModel.getUsername());
        this.loginViewModel.setPassword(loginViewModel.getPassword());
    }

    private void initializeViewModel() {
        if (getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.login_activity_retain_fragment)) == null) {

            getSupportFragmentManager().beginTransaction().add(mLoginRetainFragment, getResources().getString(R.string.login_activity_retain_fragment)).commit();

        } else {
            mLoginRetainFragment = (LoginRetainFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.login_activity_retain_fragment));

            if (mLoginRetainFragment.getViewModel() != null) mViewModel = mLoginRetainFragment.getViewModel();
        }
    }

    private void submitForm() {
        hideSoftKeyboard();
        if (loginViewModel.isSubDomainIsValid()) {
            if (mBinding.usernameEdt.getText().toString().isEmpty() || mBinding.passwordEdt.getText().toString().isEmpty()) {
                showSnackBrMsg(getResources().getString(R.string.username_password_required), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
            } else {
                if (isNetworkAvailable()) {
                    //mBinding.setProcessing(true);
                    ValidateAdminRequest validateAdminRequest = new ValidateAdminRequest();
                    validateAdminRequest.setAction("validate_admin");
                    validateAdminRequest.setUsername(mBinding.usernameEdt.getText().toString());
                    validateAdminRequest.setPassword(mBinding.passwordEdt.getText().toString());
                    validateAdminRequest.setCustomer_domain(mBinding.subDomainEdt.getText().toString());
                    /**
                     GET List Resources
                     **/
                    apiInterface = APIClient.getClient(SharedPrefsUtl.getStringFlag(this, getResources().getString(R.string.subdomain))).create(APIInterface.class);
                    Call<ValidateAdminResponse> responseCall = apiInterface.validateAdmin(validateAdminRequest);
                    responseCall.enqueue(new Callback<ValidateAdminResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<ValidateAdminResponse> call, @NonNull Response<ValidateAdminResponse> response) {
                            //mBinding.setProcessing(false);
                            if (response.body() != null) {
                                if (response.body().getCode() == 200) {
                                    SharedPrefsUtl.setBooleanPref(LoginActivity.this, getResources().getString(R.string.domain_authenticated), true);
                                    SharedPrefsUtl.setStringPref(LoginActivity.this, getResources().getString(R.string.access_token), response.body().getAccess_token());
//                                    SharedPrefsUtl.setStringPref(LoginActivity.this, getResources().getString(R.string.admin_username), mBinding.admnusernameEdt.getText().toString());
//                                    SharedPrefsUtl.setStringPref(LoginActivity.this, getResources().getString(R.string.admin_password), mBinding.admnpasswordEdt.getText().toString());
                                    startActivity(new Intent(LoginActivity.this, WorkStationsActivity.class));
                                    finish();
                                } else {
                                    showSnackBrMsg(getResources().getString(R.string.error), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ValidateAdminResponse> call, @NonNull Throwable t) {
                            //mBinding.setProcessing(false);
                            showSnackBrMsg(getResources().getString(R.string.error), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
                        }
                    });
//                if (mBinding.admnusernameEdt.getText().toString().equals("provider_paths") && mBinding.admnpasswordEdt.getText().toString().equals("provider_paths")) {
//                    SharedPrefsUtl.setBooleanPref(this, getResources().getString(R.string.domain_authenticated), true);
//                    startActivity(new Intent(this, WorkStationsActivity.class));
//                    finish();
//                }
                } else {
                    showSnackBrMsg(getResources().getString(R.string.no_connection), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
                }
            }
        } else {
            //todo uncomment
            //mViewModel.validateCustomerDomain(mBinding.subdomainEdt.getText().toString());
//filesdemo.epoptia.com
  //      filesdemo
  //      filesdemo

            if (mBinding.subDomainEdt.getText().toString().isEmpty()) {
                showSnackBrMsg(getResources().getString(R.string.subdomain_required), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
            } else {
                if (isNetworkAvailable()) {
                    apiInterface = APIClient.getClient(mBinding.subDomainEdt.getText().toString()).create(APIInterface.class);
                    //mBinding.setProcessing(true);
                    ValidateCustomerDomainRequest request = new ValidateCustomerDomainRequest();
                    request.setAction("validate_customer_domain");
                    request.setCustomer_domain(mBinding.subDomainEdt.getText().toString());
                    /**
                     GET List Resources
                     **/
                    Call<ValidateCustomerDomainResponse> responseCall = apiInterface.validateCstmrDomain(request);
                    responseCall.enqueue(new Callback<ValidateCustomerDomainResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<ValidateCustomerDomainResponse> call, @NonNull Response<ValidateCustomerDomainResponse> response) {
                           // mBinding.setProcessing(false);
                            if (response.body() != null) {
                                if (response.body().getCode() == 200) {
                                    SharedPrefsUtl.setStringPref(LoginActivity.this, getResources().getString(R.string.subdomain), mBinding.subDomainEdt.getText().toString());
                                    if (response.body().getAccess_token() != null) {
                                        SharedPrefsUtl.setStringPref(LoginActivity.this, getResources().getString(R.string.access_token), response.body().getAccess_token());
                                        SharedPrefsUtl.setBooleanPref(LoginActivity.this, getResources().getString(R.string.domain_authenticated), true);
                                        startActivity(new Intent(LoginActivity.this, WorkStationsActivity.class));
                                        finish();
                                    } else {
//                                    mBinding.subdomainEdt.setEnabled(false);
                                        //todo check
                                        //mBinding.setIsdomainValid(true);
                                    }
                                } else {
                                    showSnackBrMsg(getResources().getString(R.string.subdomain_not_valid), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ValidateCustomerDomainResponse> call, @NonNull Throwable t) {
                            //mBinding.setProcessing(false);
                            showSnackBrMsg(getResources().getString(R.string.error), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
                        }
                    });
                } else {
                    showSnackBrMsg(getResources().getString(R.string.no_connection), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
                }
            }
        }
    }

    private void hideSoftKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    //endregion

}
