package kioskmode.com.epoptia.viewmodel.observerstrategy;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.CompletableSubject;
import kioskmode.com.epoptia.viewmodel.ViewModelObserverCreator;

public class CompletableObserverStrategy implements ObserverStrategy {

    //region Private Properties

    private CompletableSubject mCompletableProcessor;

    private ViewModelObserverCreator mViewModelObserverCreator;

    //endregion

    //region Constructor

    public CompletableObserverStrategy(CompletableSubject completableSubject, ViewModelObserverCreator viewModelObserverCreator) {
        this.mCompletableProcessor = completableSubject;
        this.mViewModelObserverCreator = viewModelObserverCreator;
    }

    //endregion

    //region Public Methods

    @Override
    public Disposable execute() {
        return mCompletableProcessor
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(mViewModelObserverCreator.getViewModelCompletableObserver());
    }

    //endregion

}
