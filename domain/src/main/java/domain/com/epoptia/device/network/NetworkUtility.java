package domain.com.epoptia.device.network;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface NetworkUtility {

    /**
     * Is network available.
     *
     * @return
     */
    Completable isNetworkAvailable();

    /**
     * Get network link speed.
     */
    //todo
    Single<Integer> getNetworkLinkSpeed();

    //todo
    Single<Boolean> checkInternetConnectivity();

}
