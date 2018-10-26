package kioskmode.com.epoptia.admin;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import kioskmode.com.epoptia.BaseActivity;
import kioskmode.com.epoptia.pojo.ValidateAdminRequest;
import kioskmode.com.epoptia.pojo.ValidateAdminResponse;
import kioskmode.com.epoptia.pojo.ValidateCustomerDomainRequest;
import kioskmode.com.epoptia.pojo.ValidateCustomerDomainResponse;
import kioskmode.com.epoptia.R;
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

public class LoginAdminActivity extends BaseActivity {

    private static final String debugTag = LoginAdminActivity.class.getSimpleName();
    private ActivityLoginAdminBinding mBinding;
    private boolean isDomainAuthenticated;
    private APIInterface apiInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login_admin);

        if (getResources().getConfiguration().smallestScreenWidthDp >= 600) {
            mBinding.deviceIsTabletTtv.setText(getResources().getString(R.string.device_is_tablet));
        } else {
            mBinding.deviceIsTabletTtv.setText(getResources().getString(R.string.device_is_phone));
        }

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        if (savedInstanceState != null) {
            isDomainAuthenticated = savedInstanceState.getBoolean(getResources().getString(R.string.domain_authenticated));
            mBinding.setIsdomainValid(isDomainAuthenticated);
        }

        mBinding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(getResources().getString(R.string.domain_authenticated), mBinding.getIsdomainValid());
    }

    private void submitForm() {
        hideSoftKeyboard();
        if (isDomainAuthenticated) {
            if (mBinding.admnusernameEdt.getText().toString().isEmpty() || mBinding.admnpasswordEdt.getText().toString().isEmpty()) {
                showSnackBrMsg(getResources().getString(R.string.username_password_required), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
            } else {
                if (isNetworkAvailable()) {
                    mBinding.setProcessing(true);
                    ValidateAdminRequest validateAdminRequest = new ValidateAdminRequest();
                    validateAdminRequest.setAction("validate_admin");
                    validateAdminRequest.setUsername(mBinding.admnusernameEdt.getText().toString());
                    validateAdminRequest.setPassword(mBinding.admnpasswordEdt.getText().toString());
                    validateAdminRequest.setCustomer_domain(mBinding.subdomainEdt.getText().toString());
                    /**
                     GET List Resources
                     **/
                    apiInterface = APIClient.getClient(SharedPrefsUtl.getStringFlag(this, getResources().getString(R.string.subdomain))).create(APIInterface.class);
                    Call<ValidateAdminResponse> responseCall = apiInterface.validateAdmin(validateAdminRequest);
                    responseCall.enqueue(new Callback<ValidateAdminResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<ValidateAdminResponse> call, @NonNull Response<ValidateAdminResponse> response) {
                            mBinding.setProcessing(false);
                            if (response.body() != null) {
                                if (response.body().getCode() == 200) {
                                    SharedPrefsUtl.setBooleanPref(LoginAdminActivity.this, getResources().getString(R.string.domain_authenticated), true);
                                    SharedPrefsUtl.setStringPref(LoginAdminActivity.this, getResources().getString(R.string.access_token), response.body().getAccess_token());
//                                    SharedPrefsUtl.setStringPref(LoginAdminActivity.this, getResources().getString(R.string.admin_username), mBinding.admnusernameEdt.getText().toString());
//                                    SharedPrefsUtl.setStringPref(LoginAdminActivity.this, getResources().getString(R.string.admin_password), mBinding.admnpasswordEdt.getText().toString());
                                    startActivity(new Intent(LoginAdminActivity.this, WorkStationsActivity.class));
                                    finish();
                                } else {
                                    showSnackBrMsg(getResources().getString(R.string.error), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ValidateAdminResponse> call, @NonNull Throwable t) {
                            mBinding.setProcessing(false);
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
            if (mBinding.subdomainEdt.getText().toString().isEmpty()) {
                showSnackBrMsg(getResources().getString(R.string.subdomain_required), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
            } else {
                if (isNetworkAvailable()) {
                    apiInterface = APIClient.getClient(mBinding.subdomainEdt.getText().toString()).create(APIInterface.class);
                    mBinding.setProcessing(true);
                    ValidateCustomerDomainRequest request = new ValidateCustomerDomainRequest();
                    request.setAction("validate_customer_domain");
                    request.setCustomer_domain(mBinding.subdomainEdt.getText().toString());
                    /**
                     GET List Resources
                     **/
                    Call<ValidateCustomerDomainResponse> responseCall = apiInterface.validateCstmrDomain(request);
                    responseCall.enqueue(new Callback<ValidateCustomerDomainResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<ValidateCustomerDomainResponse> call, @NonNull Response<ValidateCustomerDomainResponse> response) {
                            mBinding.setProcessing(false);
                            if (response.body() != null) {
                                if (response.body().getCode() == 200) {
                                    SharedPrefsUtl.setStringPref(LoginAdminActivity.this, getResources().getString(R.string.subdomain), mBinding.subdomainEdt.getText().toString());
                                    if (response.body().getAccess_token() != null) {
                                        SharedPrefsUtl.setStringPref(LoginAdminActivity.this, getResources().getString(R.string.access_token), response.body().getAccess_token());
                                        SharedPrefsUtl.setBooleanPref(LoginAdminActivity.this, getResources().getString(R.string.domain_authenticated), true);
                                        startActivity(new Intent(LoginAdminActivity.this, WorkStationsActivity.class));
                                        finish();
                                    } else {
//                                    mBinding.subdomainEdt.setEnabled(false);
                                        mBinding.setIsdomainValid(true);
                                        isDomainAuthenticated = true;
                                    }
                                } else {
                                    showSnackBrMsg(getResources().getString(R.string.subdomain_not_valid), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ValidateCustomerDomainResponse> call, @NonNull Throwable t) {
                            mBinding.setProcessing(false);
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
}
