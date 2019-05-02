package kioskmode.com.epoptia.viewmodel;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import kioskmode.com.epoptia.viewmodel.observerstrategy.ObserverStrategy;

public class ObserverContextStrategy {

    //region Private Properties

    private ObserverStrategy observerStrategy;

    //endregion

    //region Constructor

    @Inject
    public ObserverContextStrategy() {}

    //endregion

    //region Public Methods

    public void setStrategy(ObserverStrategy observerStrategy) {
        this.observerStrategy = observerStrategy;
    }

    public Disposable executeStrategy() {
        return observerStrategy.execute();
    }

    //endregion

}
