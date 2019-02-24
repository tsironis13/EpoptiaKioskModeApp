package kioskmode.com.epoptia.viewmodel.strategy;

import io.reactivex.disposables.Disposable;

public interface Strategy {

    Disposable execute();

}
