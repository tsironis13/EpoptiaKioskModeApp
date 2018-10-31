package kioskmode.com.epoptia.kioskmodetablet.stationworkers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import kioskmode.com.epoptia.pojo.GetWorkersRequest;
import kioskmode.com.epoptia.pojo.GetWorkersResponse;
import kioskmode.com.epoptia.pojo.StationWorker;
import kioskmode.com.epoptia.pojo.ValidateWorkerRequest;
import kioskmode.com.epoptia.pojo.ValidateWorkerResponse;
import kioskmode.com.epoptia.R;
import kioskmode.com.epoptia.adapters.RecyclerViewAdapter;
import kioskmode.com.epoptia.databinding.StationWorkersFrgmtBinding;
import kioskmode.com.epoptia.kioskmodetablet.KioskModeActivity;
import kioskmode.com.epoptia.kioskmodetablet.systemdashboard.SystemDashboardFrgmt;
import kioskmode.com.epoptia.retrofit.APIClient;
import kioskmode.com.epoptia.retrofit.APIInterface;
import kioskmode.com.epoptia.utls.SharedPrefsUtl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
    private LinearLayoutManager linearLayoutManager;
    private RecyclerViewAdapter rcvAdapter;
    private StationWorkersPresenter stationWorkersPresenter;
    private APIInterface apiInterface;
    private int stationId;
    private AlertDialog mAlertDialog;
    private String stationName, title;
    private Handler networkStatusHandler;
    private SpeedTestSocket speedTestSocket;

    public static StationWorkersFrgmt newInstance(int stationId, String stationName) {
        Bundle bundle = new Bundle();
        bundle.putInt("station_id", stationId);
        bundle.putString("station_name", stationName);
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
        if (savedInstanceState != null) {
            stationId = savedInstanceState.getInt(getResources().getString(R.string.workstation_id));
            stationName = savedInstanceState.getString("station_name");
        } else {
            if (getArguments() != null) {
                stationId = getArguments().getInt("station_id");
                stationName = getArguments().getString("station_name");
            }
        }
        if (stationName == null) {
            stationName = SharedPrefsUtl.getStringFlag(getActivity(), getResources().getString(R.string.stationame));
        } else {
            SharedPrefsUtl.setStringPref(getActivity(), getResources().getString(R.string.stationame), stationName);
        }
        setToolbarTitle();

        ((KioskModeActivity)getActivity()).getToolbarTextViewTitle().setText(title);
        ((KioskModeActivity)getActivity()).getToolbarTextViewUsernameRight().setText("");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity()!= null)
                    check();
            }
        }, 500);
        speedTestSocket = new SpeedTestSocket();
        addSpeedTestListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeView();
            }
        });
        checkNetworkStateEveryMinute();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter("networkState"));
    }

    @Override
    public void onPause() {
        super.onPause();
        networkStatusHandler.removeCallbacksAndMessages(null);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getResources().getString(R.string.workstation_id), stationId);
        outState.putString("station_name", stationName);
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
        final GetWorkersRequest request = new GetWorkersRequest();
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
                mBinding.setProcessing(false);
                if (mBinding.getHaserror()) mBinding.setHaserror(false);
                if (response.body() != null) {
                    if (getActivity() != null) {
                        if (response.body().getCode() == 200) {
                            SharedPrefsUtl.setIntPref(getActivity(), stationId, getResources().getString(R.string.workstation_id));
                            stationWorkerList = response.body().getData();
                            linearLayoutManager = new LinearLayoutManager(getActivity());
                            if (rcvAdapter == null)mBinding.rcv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                            rcvAdapter = new RecyclerViewAdapter(R.layout.station_workers_tablet_rcv_row) {
                                @Override
                                protected Object getObjForPosition(int position, ViewDataBinding mBinding) {
                                    return stationWorkerList.get(position);
                                }
                                @Override
                                protected int getLayoutIdForPosition(int position) {
                                    return R.layout.station_workers_tablet_rcv_row;
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
        final StationWorker stationWorker = stationWorkerList.get((int)view.getTag());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText edittext = new EditText(getActivity());
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setTitle(getResources().getString(R.string.enter_worker_password_dialog_title));
        builder.setView(edittext);
        builder.setPositiveButton(getResources().getString(R.string.submit), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (isNetworkAvailable()) {
                    final ValidateWorkerRequest request = new ValidateWorkerRequest();
                    request.setAction("login_worker");
                    String accessToken = SharedPrefsUtl.getStringFlag(getActivity(), getResources().getString(R.string.access_token));
                    request.setAccess_token(accessToken);
                    request.setUsername(stationWorker.getUsername());
                    request.setPassword(edittext.getText().toString());
                    request.setStation_id(stationId);

                    Call<ValidateWorkerResponse> responseCall = apiInterface.validateWorker(request);
                    responseCall.enqueue(new Callback<ValidateWorkerResponse>() {
                        @Override
                        public void onResponse(Call<ValidateWorkerResponse> call, Response<ValidateWorkerResponse> response) {
                            if (response.body().getCode() == 200) {
                                String cookie = response.headers().get("Set-Cookie");
                                SharedPrefsUtl.setStringPref(getActivity(), "cookie", response.headers().get("Set-Cookie"));
                                SharedPrefsUtl.setStringPref(getActivity(), "end_url", response.body().getWorkstation_url());
                                SharedPrefsUtl.setStringPref(getActivity(), "worker_username", stationWorker.getUsername());
                                SharedPrefsUtl.setIntPref(getActivity(), stationWorker.getUserId(), "worker_id");

                                getActivity().getSupportFragmentManager()
                                                                .beginTransaction()
                                                                .replace(R.id.kioskModeLlt, SystemDashboardFrgmt.newInstance(stationId, cookie, response.body().getWorkstation_url(), stationName, stationWorker.getUsername(), stationWorker.getUserId(),"device"), getResources().getString(R.string.system_dahsboard_frgmt))
                                                                .addToBackStack(getResources().getString(R.string.system_dahsboard_frgmt))
                                                                .commit();
                            } else {
                                showSnackBrMsg(getResources().getString(R.string.username_password_invalid), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
                            }
                        }

                        @Override
                        public void onFailure(Call<ValidateWorkerResponse> call, Throwable t) {
                            showSnackBrMsg(getResources().getString(R.string.error), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
                        }
                    });
                } else {
                    showSnackBrMsg(getResources().getString(R.string.no_connection), mBinding.containerLnlt, Snackbar.LENGTH_SHORT);
                }
            }
        });
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    private void setToolbarTitle() {
        int currentOrientation = getResources().getConfiguration().orientation;

        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            title = getResources().getString(R.string.workers_frgmt_title) + " " + stationName;
        } else {
            int stationNameLength = stationName.length();

            if (stationNameLength > 19) {
                String substr = stationName.substring(0, 19);

                title = getResources().getString(R.string.workers_frgmt_portrait_title) + " " + substr + "...";
            } else {
                title = getResources().getString(R.string.workers_frgmt_portrait_title) + " " + stationName;
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm != null ? cm.getActiveNetworkInfo() : null) != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void dismissDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) mAlertDialog.dismiss();
    }

    public void showSnackBrMsg(String msg, View container, int length) {
        Snackbar snackbar = Snackbar.make(container, msg, length);
        snackbar.show();
    }

    private void checkNetworkStateEveryMinute() {
        networkStatusHandler = new Handler();
        networkStatusHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setNetworkStatusAndShowNetworkRelatedSnackbar(isNetworkAvailable());
                networkStatusHandler.postDelayed(this, 30000);
            }
        }, 1000);
    }

    private int getNetworkSpeed() {
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager != null ? wifiManager.getConnectionInfo() : null;
        if (wifiInfo != null) {
            return wifiInfo.getLinkSpeed(); //measured using WifiInfo.LINK_SPEED_UNITS
        }
        return -1;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            boolean networkStatus = intent.getBooleanExtra("networkState", false);
            setNetworkStatusAndShowNetworkRelatedSnackbar(networkStatus);
        }
    };

    private void setNetworkStatusAndShowNetworkRelatedSnackbar(boolean networkState) {
        if (!networkState) {
            mBinding.setNetworkState("NO INTERNET CONNECTION");

            return;
        } else {
            mBinding.setNetworkState(null);
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                speedTestSocket.startDownload("http://ipv4.ikoula.testdebit.info/1M.iso");
            }
        });

        thread.start();
    }

    private void addSpeedTestListener() {
        speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

            @Override
            public void onCompletion(final SpeedTestReport report) {
                // called when download/upload is complete
                System.out.println("[COMPLETED] rate in octet/s : " + report.getTransferRateOctet());
                System.out.println("[COMPLETED] rate in bit/s   : " + report.getTransferRateBit());

                displayNetworkQuality(convertBitPerSecondToMegabitPerSecond(report.getTransferRateBit().doubleValue()));
            }

            @Override
            public void onError(final SpeedTestError speedTestError, final String errorMessage) {
                // called when a download/upload error occur
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "onError speed test " + speedTestError.toString() + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onProgress(float percent, SpeedTestReport report) {
                // called to notify download/upload progress
                System.out.println("[PROGRESS] progress : " + percent + "%");
                System.out.println("[PROGRESS] rate in octet/s : " + report.getTransferRateOctet());
                System.out.println("[PROGRESS] rate in bit/s   : " + report.getTransferRateBit());
            }
        });
    }

    private double convertBitPerSecondToMegabitPerSecond(double bitPerSecond) {
        return bitPerSecond * 0.000001;
    }

    private void displayNetworkQuality(double downloadRateInMegabitPerSecond) {
        final DecimalFormat df = new DecimalFormat("#.#");

        final String formattedRate = df.format(downloadRateInMegabitPerSecond);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "[COMPLETED] rate in Megabit/s   : " + formattedRate, Toast.LENGTH_LONG).show();
            }
        });

        if (downloadRateInMegabitPerSecond < 2) {
            mBinding.setNetworkState("LOW INTERNET CONNECTION");
        } else if (downloadRateInMegabitPerSecond < 5) {
            mBinding.setNetworkState("LOW INTERNET CONNECTION");
        } else if (downloadRateInMegabitPerSecond < 10) {
            mBinding.setNetworkState("LOW INTERNET CONNECTION");
        } else {
            mBinding.setNetworkState(null);
        }
    }
}
