package kioskmode.com.epoptia.kioskmode.workers.viewmodel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import domain.com.epoptia.Constants;
import domain.com.epoptia.interactor.worker.GetStationWorkersUseCase;
import domain.com.epoptia.model.domain.DomainBaseModel;
import domain.com.epoptia.model.domain.DomainWorkerModel;
import domain.com.epoptia.model.dto.post.GetStationWorkersPostDto;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.AsyncProcessor;
import io.reactivex.schedulers.Schedulers;
import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.mappers.DomainWorkerModelToWorkerViewModelMapper;
import kioskmode.com.epoptia.viewmodel.ObserverContextStrategy;
import kioskmode.com.epoptia.viewmodel.ViewModelObserverCreator;
import kioskmode.com.epoptia.viewmodel.models.WorkStationViewModel;
import kioskmode.com.epoptia.viewmodel.observercreators.GetStationWorkersViewModelObserverCreator;
import kioskmode.com.epoptia.viewmodel.observerstrategy.FlowableObserverStrategy;

public class StationWorkersViewModel implements StationWorkersContract.ViewModel {

    //region Injections

    @Inject
    GetStationWorkersPostDto getStationWorkersPostDto;

    @Inject
    GetStationWorkersUseCase getStationWorkersUseCase;

    @Inject
    ObserverContextStrategy observerContextStrategy;

    @Inject
    DomainWorkerModelToWorkerViewModelMapper domainWorkerModelToWorkerViewModelMapper;

    //endregion

    //region Public Properties

    public StationWorkersContract.View mViewCallback;

    //endregion

    //region Private Properties

    //todo remove
    private static final String debugTag = StationWorkersViewModel.class.getSimpleName();

    private AsyncProcessor<DomainBaseModel> mFlowableProcessor;

    private ViewModelObserverCreator mViewModelObserverCreator;

    private Disposable mObserverDisposable;

    //10 -> running
    //0 -> none
    private int requestState;

    //1 -> activity created
    //2 -> activity resumed
    //3 -> activity detached
    private int viewState;

    private int subjectType;

    //endregion

    //region Constructor

    @Inject
    public StationWorkersViewModel() {}

    //endregion

    //region Public Methods

    @Override
    public void onViewAttached(Bundle savedInstanceState, Lifecycle.View viewCallback) {
        mViewCallback = (StationWorkersContract.View) viewCallback;

        Log.e(debugTag, "onViewAttached");

        //todo uncomment
        //startNetworkSpeedTestService();

        if (savedInstanceState == null) {
            return;
        }

        if (requestState != Constants.REQUEST_RUNNING) {
            Log.e(debugTag, "LOAD AGAIN NOOOOOOOOOOOOOOOOOOOOOOOO");

            viewState = Constants.VIEW_STATE_ATTACHED;

            this.loadStationWorkers(savedInstanceState.getParcelable("workstation_view_model_parcel"));
        }
    }

    @Override
    public void onViewResumed() {
        Log.e(debugTag, "onViewResumed => " + requestState + " " + viewState);

        //possibly some request permissions dialogs have been called
        //activity has been paused and resumed after clearUnnecessaryReferencesAndUnsetRequestState() has been called
        //so mViewModelObserverCreator is null and observerContextstrategy has no reason to be called
        if (mViewModelObserverCreator == null) {
            return;
        }

        if (requestState == Constants.REQUEST_RUNNING && viewState == Constants.VIEW_STATE_ATTACHED) {
            return;
        }

        Log.e(debugTag, "onViewResumed => request pending");

        Log.e(debugTag, mViewModelObserverCreator.getViewModelFlowableObserver() + " ON VIEW RESUMED OBSERVER");

        mViewCallback.setProcessing(true);

        //todo complete for all subject types
        if (subjectType == Constants.FLOWABLE_SUBJECT) {
            observerContextStrategy.setStrategy(new FlowableObserverStrategy(mFlowableProcessor, mViewModelObserverCreator));
        }

        mObserverDisposable = observerContextStrategy.executeStrategy();
    }

    @Override
    public void onViewDetached() {
        mViewCallback = null;

        //todo uncomment
        //stopNetworkSpeedTestService();

        viewState = Constants.VIEW_STATE_DETACHED;

        if (mObserverDisposable != null) {
            Log.e(debugTag, "onViewDetached");

            mObserverDisposable.dispose();

            mObserverDisposable = null;
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadStationWorkers(WorkStationViewModel workStationViewModel) {
        subjectType = Constants.FLOWABLE_SUBJECT;

        requestState = Constants.REQUEST_RUNNING;

        getStationWorkersPostDto.setAction(Constants.ACTION_GET_STATIONWORKERS);
        getStationWorkersPostDto.setWorkstationId(workStationViewModel.getId());

        mFlowableProcessor = AsyncProcessor.create();

        mViewModelObserverCreator = new GetStationWorkersViewModelObserverCreator(this);

        mObserverDisposable = mFlowableProcessor
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeWith(mViewModelObserverCreator.getViewModelFlowableObserver());

        getStationWorkersUseCase
                        .execute(getStationWorkersPostDto)
                        .doOnSubscribe(subscription -> mViewCallback.setProcessing(true))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(mFlowableProcessor);
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadStationWorkersOnSuccess(List<DomainWorkerModel> workers) {
        Log.e(debugTag, "loadStationWorkersOnSuccess");
        clearUnnecessaryReferencesAndUnsetRequestState();

        Flowable
                .fromIterable(workers)
                .flatMapSingle(worker -> domainWorkerModelToWorkerViewModelMapper.map(worker))
                .toList()
                .toFlowable()
                .subscribe(stationWorkersViewModels -> {
                    mViewCallback.loadStationWorkersOnSuccess(stationWorkersViewModels);
                }, error -> {
                    //todo
                });
    }

    @Override
    public void loadStationWorkersOnError(Throwable throwable) {
        clearUnnecessaryReferencesAndUnsetRequestState();

        mViewCallback.loadStationWorkersOnError(throwable);
    }

    //endregion

    //region Private Methods

    private void clearUnnecessaryReferencesAndUnsetRequestState() {
        mObserverDisposable = null;

        mViewModelObserverCreator = null;

        mViewCallback.setProcessing(false);

        requestState = Constants.REQUEST_NONE;
    }

    //endregion

}
