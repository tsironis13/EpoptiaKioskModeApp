package kioskmode.com.epoptia.viewmodel.strategy;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.CompletableSubject;
import kioskmode.com.epoptia.viewmodel.ViewModelObserverCreator;

public class CompletableStrategy implements Strategy {

    private CompletableSubject mCompletableProcessor;

    private ViewModelObserverCreator mViewModelObserverCreator;

    public CompletableStrategy(CompletableSubject completableSubject, ViewModelObserverCreator viewModelObserverCreator) {
        this.mCompletableProcessor = completableSubject;
        this.mViewModelObserverCreator = viewModelObserverCreator;
    }

    @Override
    public Disposable execute() {
        return mCompletableProcessor
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(mViewModelObserverCreator.getViewModelCompletableObserver());
    }
}
