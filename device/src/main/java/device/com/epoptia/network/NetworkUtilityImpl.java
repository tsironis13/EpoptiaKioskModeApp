package device.com.epoptia.network;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import javax.inject.Inject;

import domain.com.epoptia.Constants;
import domain.com.epoptia.device.network.NetworkUtility;
import io.reactivex.Completable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class NetworkUtilityImpl implements NetworkUtility {

    //region Private Properties

    private Context mContext;

    //endregion

    //region Constructor

    @Inject
    public NetworkUtilityImpl(Context context) {
        this.mContext = context;
    }

    //endregion

    //region Public Methods

    @Override
    public Single<Boolean> checkInternetConnectivity() {
        return Single.create(emitter -> ReactiveNetwork
                .checkInternetConnectivity()
                .subscribe(isConnectedToInternet -> {
                    if (isConnectedToInternet) {
                        emitter.onSuccess(true);

                        return;
                    }

                    emitter.onError(new Throwable());
                }));
    }

    //todo remove
    @Override
    public Completable isNetworkAvailable() {
        if (getActiveNetworkInfo() == null) {
            //todo change msg to constant
            return Completable.error(new Exception("Χωρίς σύνδεση στο δίκτυο.", new NetworkErrorException()));
        }

        return Completable.complete();
    }

    @Override
    public Single<Integer> getNetworkLinkSpeed() {
        if (mContext == null) {
            return Single.error(new NullPointerException());
        }

        //mContext refers to application context
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);

        WifiInfo wifiInfo = wifiManager != null ? wifiManager.getConnectionInfo() : null;

        if (wifiInfo != null) {
            //measured using WifiInfo.LINK_SPEED_UNITS
            return Single.just(wifiInfo.getLinkSpeed());
        }

        return Single.error(new Throwable(Constants.NO_WIFI_INFO));
    }

    //endregion

    //region Private Methods

    private NetworkInfo getActiveNetworkInfo() {
        if (mContext == null) {
            return null;
        }

        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm != null ? cm.getActiveNetworkInfo() : null;
    }

    //endregion

}
