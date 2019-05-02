package kioskmode.com.epoptia.viewmodel.observerstrategy;

import domain.com.epoptia.model.domain.DomainBaseModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.AsyncProcessor;
import io.reactivex.schedulers.Schedulers;
import kioskmode.com.epoptia.viewmodel.ViewModelObserverCreator;

public class FlowableObserverStrategy implements ObserverStrategy {

    //region Private Properties

    private AsyncProcessor<DomainBaseModel> mFlowableProcessor;

    private ViewModelObserverCreator mViewModelObserverCreator;

    //endregion

    //region Constructor

    public FlowableObserverStrategy(AsyncProcessor<DomainBaseModel> flowableProcessor, ViewModelObserverCreator viewModelObserverCreator) {
        this.mFlowableProcessor = flowableProcessor;
        this.mViewModelObserverCreator = viewModelObserverCreator;
    }

    //endregion

    //region Public Methods

    @Override
    public Disposable execute() {
        return mFlowableProcessor
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(mViewModelObserverCreator.getViewModelFlowableObserver());
    }

    //endregion

}
