package kioskmode.com.epoptia.workstations.viewmodel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import domain.com.epoptia.Constants;
import domain.com.epoptia.interactor.device.GetDeviceFromLocalStorageUseCase;
import domain.com.epoptia.interactor.device.LockDeviceUseCase;
import domain.com.epoptia.interactor.network.StartNetworkSpeedTestServiceUseCase;
import domain.com.epoptia.interactor.network.StopNetworkSpeedTestServiceUseCase;
import domain.com.epoptia.interactor.workstations.GetWorkStationsUseCase;
import domain.com.epoptia.interactor.workstations.SaveWorkStationToLocalStorageUseCase;
import domain.com.epoptia.model.domain.DomainBaseModel;
import domain.com.epoptia.model.domain.DomainWorkStationModel;
import domain.com.epoptia.model.dto.post.GetWorkStationsPostDto;
import fr.bmartel.speedtest.model.SpeedTestError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.AsyncProcessor;
import io.reactivex.schedulers.Schedulers;
import kioskmode.com.epoptia.kioskmodetablet.KioskModeActivity;
import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.login.LoginViewModel;
import kioskmode.com.epoptia.mappers.DomainNetworkStateModelToNetworkStateViewModelMapper;
import kioskmode.com.epoptia.mappers.WorkStationViewModelToWorkStationDomainModelMapper;
import kioskmode.com.epoptia.viewmodel.ObserverContextStrategy;
import kioskmode.com.epoptia.viewmodel.ViewModelObserverCreator;
import kioskmode.com.epoptia.viewmodel.models.NetworkStateViewModel;
import kioskmode.com.epoptia.viewmodel.models.WorkStationViewModel;
import kioskmode.com.epoptia.viewmodel.observercreators.GetWorkStationsViewModelObserverCreator;
import kioskmode.com.epoptia.viewmodel.observerstrategy.FlowableObserverStrategy;

public class WorkStationsViewModel implements WorkStationsContract.ViewModel {

    //region Injections

    @Inject
    GetDeviceFromLocalStorageUseCase getDeviceFromLocalStorageUseCase;

    @Inject
    GetWorkStationsUseCase getWorkStationsUseCase;

    @Inject
    StartNetworkSpeedTestServiceUseCase startNetworkSpeedTestServiceUseCase;

    @Inject
    StopNetworkSpeedTestServiceUseCase stopNetworkSpeedTestServiceUseCase;

    @Inject
    LockDeviceUseCase lockDeviceUseCase;

    @Inject
    SaveWorkStationToLocalStorageUseCase saveWorkStationToLocalStorageUseCase;

    @Inject
    NetworkStateViewModel networkStateViewModel;

    @Inject
    GetWorkStationsPostDto getWorkStationsPostDto;

    @Inject
    ObserverContextStrategy observerContextStrategy;

    @Inject
    DomainNetworkStateModelToNetworkStateViewModelMapper domainNetworkStateModelToNetworkStateViewModelMapper;

    @Inject
    WorkStationViewModelToWorkStationDomainModelMapper workStationViewModelToWorkStationDomainModelMapper;

    //endregion

    //region Public Properties

    public WorkStationsContract.View mViewCallback;

    public String test = "JKDSKLSSD";

    //endregion

    //region Private Properties

    private static final String debugTag = WorkStationsViewModel.class.getSimpleName();

    private Disposable mObserverDisposable;

    private ViewModelObserverCreator mViewModelObserverCreator;

    private AsyncProcessor<DomainBaseModel> mFlowableProcessor;

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
    public WorkStationsViewModel() {}

    //endregion

    //region Public Methods

    @Override
    public void onViewAttached(Bundle savedInstanceState, Lifecycle.View viewCallback) {
        mViewCallback = (WorkStationsContract.View) viewCallback;

        Log.e(debugTag, "onViewAttached");

        //todo uncomment
        //startNetworkSpeedTestService();

        if (requestState != Constants.REQUEST_RUNNING) {
            Log.e(debugTag, "LOAD AGAIN NOOOOOOOOOOOOOOOOOOOOOOOO");

            viewState = Constants.VIEW_STATE_ATTACHED;

            this.loadWorkStations();
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
    public void startNetworkSpeedTestService() {
        startNetworkSpeedTestServiceUseCase
                                    .execute()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(networkStateModel -> {
                                        networkStateViewModel = domainNetworkStateModelToNetworkStateViewModelMapper.map(networkStateModel);

                                        mViewCallback.setNetworkState(networkStateViewModel);
                                    }, error -> {
                                        int x = 10;
                                        //todo
                                    });
    }

    @Override
    public void stopNetworkSpeedTestService() {
        stopNetworkSpeedTestServiceUseCase.execute();
    }

    @Override
    public void lockDevice() {}

    @SuppressLint("CheckResult")
    public void loadWorkStations() {
        subjectType = Constants.FLOWABLE_SUBJECT;

        requestState = Constants.REQUEST_RUNNING;

        getWorkStationsPostDto.setAction(Constants.ACTION_GET_WORKSTATIONS);

        mFlowableProcessor = AsyncProcessor.create();

        mViewModelObserverCreator = new GetWorkStationsViewModelObserverCreator(this);

        mObserverDisposable = mFlowableProcessor
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeWith(mViewModelObserverCreator.getViewModelFlowableObserver());

        getWorkStationsUseCase
                        .execute(getWorkStationsPostDto)
                        .doOnSubscribe(subscription -> mViewCallback.setProcessing(true))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(mFlowableProcessor);
    }

    @Override
    public void saveWorkStation(WorkStationViewModel workStationViewModel) {
        if (workStationViewModel == null) {
            return;
        }

        DomainWorkStationModel workStationModel = workStationViewModelToWorkStationDomainModelMapper.map(workStationViewModel);

        saveWorkStationToLocalStorageUseCase
                                        .execute(workStationModel)
                                        .subscribeOn(Schedulers.io())
                                        .subscribe();
    }

    @Override
    public void selectWorkStation(WorkStationViewModel workStationViewModel) {
    }

    @Override
    public void loadWorkStationsOnSuccess(List<DomainWorkStationModel> domainWorkStations) {
        Log.e(debugTag, "loadWorkStationsOnSuccess");
        clearUnnecessaryReferencesAndUnsetRequestState();

        mViewCallback.loadWorkStationsOnSuccess(domainWorkStations);
    }

    @Override
    public void loadWorkStationsOnError(Throwable throwable) {
        clearUnnecessaryReferencesAndUnsetRequestState();

        mViewCallback.loadWorkStationsOnError(throwable);
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
