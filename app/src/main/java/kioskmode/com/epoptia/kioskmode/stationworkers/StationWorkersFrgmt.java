package kioskmode.com.epoptia.kioskmode.stationworkers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import kioskmode.com.epoptia.POJO.GetWorkStationsRequest;
import kioskmode.com.epoptia.POJO.GetWorkersRequest;
import kioskmode.com.epoptia.POJO.GetWorkersResponse;
import kioskmode.com.epoptia.POJO.StationWorker;
import kioskmode.com.epoptia.POJO.ValidateAdminRequest;
import kioskmode.com.epoptia.POJO.ValidateAdminResponse;
import kioskmode.com.epoptia.R;
import kioskmode.com.epoptia.adapters.RecyclerViewAdapter;
import kioskmode.com.epoptia.admin.LoginAdminActivity;
import kioskmode.com.epoptia.admin.WorkStationsActivity;
import kioskmode.com.epoptia.databinding.StationWorkersFrgmtBinding;
import kioskmode.com.epoptia.kioskmode.systemdashboard.SystemDashboardFrgmt;
import kioskmode.com.epoptia.retrofit.APIClient;
import kioskmode.com.epoptia.retrofit.APIInterface;
import kioskmode.com.epoptia.utls.SharedPrefsUtl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by giannis on 5/9/2017.
 */

public class StationWorkersFrgmt extends Fragment implements StationWorkersContract.View {

    private static final String debugTag = StationWorkersFrgmt.class.getSimpleName();
    private View mView;
    private StationWorkersFrgmtBinding mBinding;
    private List<StationWorker> stationWorkerList = new ArrayList<>();
    private String[] workers = {"worker1", "worker2", "worker3", "worker4", "worker5", "worker6", "worker7", "worker8", "worker9", "worker10"};
    private LinearLayoutManager linearLayoutManager;
    private RecyclerViewAdapter rcvAdapter;
    private StationWorkersPresenter stationWorkersPresenter;
    private APIInterface apiInterface;
    private int stationId;
    private AlertDialog mAlertDialog;

    public static StationWorkersFrgmt newInstance(int stationId) {
        Bundle bundle = new Bundle();
        bundle.putInt("station_id", stationId);
        StationWorkersFrgmt stationWorkersFrgmt = new StationWorkersFrgmt();
        stationWorkersFrgmt.setArguments(bundle);
        return stationWorkersFrgmt;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null ) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.station_workers_frgmt, container, false);
            mView = mBinding.getRoot();
        }
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(getResources().getString(R.string.workers_frgmt_title));
        if (savedInstanceState != null) {

        } else {
            if (getArguments() != null) {
                stationId = getArguments().getInt("station_id");
            }
        }
        check();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }

    private void check() {
        if (isNetworkAvailable()) {
            if (mBinding.getHaserror()) mBinding.setHaserror(false);
            initializeView();
        } else {
            mBinding.setHaserror(true);
            mBinding.setErrortext(getResources().getString(R.string.no_connection));
        }
    }

    private void initializeView() {
        mBinding.setProcessing(true);

        apiInterface = APIClient.getClient(SharedPrefsUtl.getStringFlag(getActivity(), getResources().getString(R.string.subdomain))).create(APIInterface.class);
        stationWorkersPresenter = new StationWorkersPresenter(this);
        GetWorkersRequest request = new GetWorkersRequest();
        request.setAction("get_workers");
        String accessToken = SharedPrefsUtl.getStringFlag(getActivity(), getResources().getString(R.string.access_token));
        request.setAccess_token(accessToken);
        request.setWorkstation_id(stationId);
        /**
         GET List Resources
         **/
        Call<GetWorkersResponse> responseCall = apiInterface.getWorkers(request);
        responseCall.enqueue(new Callback<GetWorkersResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetWorkersResponse> call, @NonNull Response<GetWorkersResponse> response) {
//                       Log.e(debugTag, response.body().getCode()+ " CODE");
                mBinding.setProcessing(false);
                if (response.body() != null) {
                    if (getActivity() != null) {
                        if (response.body().getCode() == 200) {
                            stationWorkerList = response.body().getData();
                            linearLayoutManager = new LinearLayoutManager(getActivity());
                            Log.e(debugTag, mBinding.rcv + " rcv" + getActivity() + " context");
                            if (rcvAdapter == null)mBinding.rcv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                            rcvAdapter = new RecyclerViewAdapter(R.layout.station_workers_rcv_row) {
                                @Override
                                protected Object getObjForPosition(int position, ViewDataBinding mBinding) {
                                    return stationWorkerList.get(position);
                                }
                                @Override
                                protected int getLayoutIdForPosition(int position) {
                                    return R.layout.station_workers_rcv_row;
                                }
                                @Override
                                protected int getTotalItems() {
                                    return stationWorkerList.size();
                                }

                                @Override
                                protected Object getClickListenerObject() {
                                    return stationWorkersPresenter;
                                }
                            };
                            mBinding.rcv.setLayoutManager(linearLayoutManager);
                            mBinding.rcv.setNestedScrollingEnabled(false);
                            mBinding.rcv.setAdapter(rcvAdapter);
                        } else {
                            mBinding.setHaserror(true);
                            mBinding.setErrortext(getResources().getString(R.string.error));
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetWorkersResponse> call, @NonNull Throwable t) {
                mBinding.setProcessing(false);
                mBinding.setHaserror(true);
                mBinding.setErrortext(getResources().getString(R.string.error));
            }
        });
    }

    @Override
    public void onBaseViewClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText edittext = new EditText(getActivity());
        builder.setTitle(getResources().getString(R.string.enter_worker_password_dialog_title));
        builder.setView(edittext);
        builder.setPositiveButton(getResources().getString(R.string.submit), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.e(debugTag, edittext.getText().toString());
                if (edittext.getText().toString().equals("test")) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.kioskModeLlt, SystemDashboardFrgmt.newInstance(stationId), getResources().getString(R.string.system_dahsboard_frgmt))
                            .addToBackStack(getResources().getString(R.string.system_dahsboard_frgmt))
                            .commit();
                }
            }
        });
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void dismissDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) mAlertDialog.dismiss();
    }
}
