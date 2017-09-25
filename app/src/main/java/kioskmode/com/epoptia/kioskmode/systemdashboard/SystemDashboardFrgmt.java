package kioskmode.com.epoptia.kioskmode.systemdashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebViewClient;

import kioskmode.com.epoptia.POJO.GetWorkersRequest;
import kioskmode.com.epoptia.POJO.GetWorkersResponse;
import kioskmode.com.epoptia.POJO.LogoutWorkerRequest;
import kioskmode.com.epoptia.POJO.ValidateAdminResponse;
import kioskmode.com.epoptia.R;
import kioskmode.com.epoptia.admin.LoginAdminActivity;
import kioskmode.com.epoptia.databinding.SystemDashboardFrgmtBinding;
import kioskmode.com.epoptia.kioskmode.KioskModeActivity;
import kioskmode.com.epoptia.kioskmode.stationworkers.StationWorkersFrgmt;
import kioskmode.com.epoptia.retrofit.APIClient;
import kioskmode.com.epoptia.retrofit.APIInterface;
import kioskmode.com.epoptia.utls.SharedPrefsUtl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by giannis on 5/9/2017.
 */

public class SystemDashboardFrgmt extends Fragment {

    private static final String debugTag = SystemDashboardFrgmt.class.getSimpleName();
    private View mView;
    private SystemDashboardFrgmtBinding mBinding;
    private int stationId;
    private String cookie, url, subdomain, end_url;
    private APIInterface apiInterface;

    public static SystemDashboardFrgmt newInstance(int stationId, String cookie, String url) {
        Bundle bundle = new Bundle();
        bundle.putInt("station_id", stationId);
        bundle.putString("cookie", cookie);
        bundle.putString("url", url);
        SystemDashboardFrgmt systemDashboardFrgmt = new SystemDashboardFrgmt();
        systemDashboardFrgmt.setArguments(bundle);
        return systemDashboardFrgmt;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null ) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.system_dashboard_frgmt, container, false);
            mView = mBinding.getRoot();
        }
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        apiInterface = APIClient.getClient(SharedPrefsUtl.getStringFlag(getActivity(), getResources().getString(R.string.subdomain))).create(APIInterface.class);
        subdomain = SharedPrefsUtl.getStringFlag(getActivity(), getResources().getString(R.string.subdomain));
        if (savedInstanceState != null) {

        } else {
//            Log.e(debugTag, "onActivityCreated");
            if (getArguments() != null) {
                cookie = getArguments().getString("cookie");
                ((KioskModeActivity)getActivity()).setCookie(cookie);
//                Log.e(debugTag, cookie + " COOKIE");
                end_url = getArguments().getString("url");
                url = "http://"+subdomain+".epoptia.com/"+end_url;
                ((KioskModeActivity)getActivity()).setUrl(end_url);
                stationId = getArguments().getInt("station_id");
            }
        }
        initializeView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.logoutWorkerItem).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutWorkerItem:
                if (isNetworkAvailable()) {
                    LogoutWorkerRequest request = new LogoutWorkerRequest();
                    request.setAction("logout_worker");
                    String accessToken = SharedPrefsUtl.getStringFlag(getActivity(), getResources().getString(R.string.access_token));
                    request.setAccess_token(accessToken);
                    /**
                     GET List Resources
                     **/
                    Call<ValidateAdminResponse> responseCall = apiInterface.logoutWorker(request);
                    responseCall.enqueue(new Callback<ValidateAdminResponse>() {
                        @Override
                        public void onResponse(Call<ValidateAdminResponse> call, Response<ValidateAdminResponse> response) {
                            if (response.body().getCode() == 200) {
                                SharedPrefsUtl.removeStringkey(getActivity(), "cookie");
                                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() == 1) {
                                    getActivity().getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.kioskModeLlt, StationWorkersFrgmt.newInstance(stationId), getResources().getString(R.string.station_workers_frgmt))
                                            .commit();
                                    getActivity().getSupportFragmentManager().popBackStack();
                                } else {
                                    getActivity().getSupportFragmentManager().popBackStack();
                                }
                            } else {
                                showSnackBrMsg(getResources().getString(R.string.error), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
                            }
                        }

                        @Override
                        public void onFailure(Call<ValidateAdminResponse> call, Throwable t) {
                            showSnackBrMsg(getResources().getString(R.string.error), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
                        }
                    });
                } else {
                    showSnackBrMsg(getResources().getString(R.string.no_connection), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
                }
//                Log.e(debugTag, "logout");
//                for (int i = 0; i < getActivity().getSupportFragmentManager().getBackStackEntryCount(); i++) {
//                    Log.e(debugTag, getActivity().getSupportFragmentManager().getBackStackEntryAt(i).getName() + "aaaaa");
//                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeView() {
        if (isNetworkAvailable()) {
            if (mBinding.getHaserror()) mBinding.setHaserror(false);
            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(mBinding.webView.getContext());
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();
            Log.e(debugTag, cookie +  " COOKIE HERE");
            cookieManager.setCookie(url, cookie);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                cookieManager.flush();
            } else {
                cookieSyncManager.sync();
            }
            mBinding.webView.setWebViewClient(new WebViewClient());
            Log.e(debugTag, url + " url");
            mBinding.webView.loadUrl(url);
        } else {
            mBinding.setHaserror(true);
            mBinding.setErrortext(getResources().getString(R.string.no_connection));
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public void showSnackBrMsg(String msg, View container, int length) {
        Snackbar snackbar = Snackbar.make(container, msg, length);
        snackbar.show();
    }
}
