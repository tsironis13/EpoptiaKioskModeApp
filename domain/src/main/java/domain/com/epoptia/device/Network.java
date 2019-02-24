package domain.com.epoptia.device;

import io.reactivex.Completable;

public interface Network {

    /**
     * Is network available.
     *
     * @return
     */
    Completable isNetworkAvailable();

}
