package domain.com.epoptia.utilities.error;

import org.reactivestreams.Publisher;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import domain.com.epoptia.Constants;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class RetryWithDelay implements Function<Flowable<? extends Throwable>, Flowable<?>> {

    //region Private Properties

    private int retryCount;

    private int maxRetries;

    private int retryDelayMillis;

    //endregion

    //region Constructor

    @Inject
    public RetryWithDelay() {
        this.retryCount = 0;
        this.maxRetries = Constants.MAX_RETRIES;
        this.retryDelayMillis = Constants.RETRY_DELAY_MILLIS;
    }

    //endregion

    //region Setters

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public void setRetryDelayMillis(int retryDelayMillis) {
        this.retryDelayMillis = retryDelayMillis;
    }

    //endregion

    //region Public Methods

    @Override
    public Flowable apply(@NonNull Flowable<? extends Throwable> thowbl) {
        return thowbl.flatMap((Function<Throwable, Publisher<?>>) throwable -> {
            //TODO REMOVE
            System.out.println("RETRY HEREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE!!");

            if (++retryCount < maxRetries) {
                return Flowable.timer(retryDelayMillis, TimeUnit.MILLISECONDS);
            } else {
                retryCount = 0;

                return Flowable.error(throwable);
            }
        });
    }

    //endregion

}
