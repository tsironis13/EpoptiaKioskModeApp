package kioskmode.com.epoptia.viewmodel;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import kioskmode.com.epoptia.viewmodel.strategy.Strategy;

public class ContextStr {

    private Strategy strategy;

    @Inject
    public ContextStr() {}

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public Disposable executeStrategy() {
        return strategy.execute();
    }

}
