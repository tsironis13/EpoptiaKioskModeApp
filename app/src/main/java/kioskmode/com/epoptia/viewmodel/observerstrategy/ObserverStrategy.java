package kioskmode.com.epoptia.viewmodel.observerstrategy;

import io.reactivex.disposables.Disposable;

public interface ObserverStrategy {

    Disposable execute();

}
