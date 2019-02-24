package device.com.epoptia;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;

import domain.com.epoptia.device.Network;
import io.reactivex.Completable;

public class NetworkImpl implements Network {

    //region Private Properties

    private Context mContext;

    //endregion

    //region Constructor

    @Inject
    public NetworkImpl(Context context) {
        this.mContext = context;
    }

    //endregion

    //region Public Methods

    @Override
    public Completable isNetworkAvailable() {
        if (getActiveNetworkInfo() == null) {
            //todo change msg to constant
            return Completable.error(new Exception("Χωρίς σύνδεση στο δίκτυο.", new NetworkErrorException()));
        }

        return Completable.complete();
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
