package kioskmode.com.epoptia.kioskmode.systemdashboard;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;

import kioskmode.com.epoptia.R;
import kioskmode.com.epoptia.databinding.SystemDashboardFrgmtBinding;
import kioskmode.com.epoptia.kioskmode.stationworkers.StationWorkersFrgmt;

/**
 * Created by giannis on 5/9/2017.
 */

public class SystemDashboardFrgmt extends Fragment {

    private static final String debugTag = SystemDashboardFrgmt.class.getSimpleName();
    private View mView;
    private SystemDashboardFrgmtBinding mBinding;
    private int stationId;

    public static SystemDashboardFrgmt newInstance(int stationId) {
        Bundle bundle = new Bundle();
        bundle.putInt("station_id", stationId);
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

        if (savedInstanceState != null) {

        } else {
            if (getArguments() != null) {
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
//                Log.e(debugTag, "logout");
//                for (int i = 0; i < getActivity().getSupportFragmentManager().getBackStackEntryCount(); i++) {
//                    Log.e(debugTag, getActivity().getSupportFragmentManager().getBackStackEntryAt(i).getName() + "aaaaa");
//                }
                getActivity().getSupportFragmentManager().popBackStack();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeView() {
        if (isNetworkAvailable()) {
            if (mBinding.getHaserror()) mBinding.setHaserror(false);
            mBinding.webView.setWebViewClient(new WebViewClient());
            mBinding.webView.loadUrl("http://dev.epoptia.com/se.php?se_id=4");
        } else {
            mBinding.setHaserror(true);
            mBinding.setErrortext(getResources().getString(R.string.no_connection));
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
