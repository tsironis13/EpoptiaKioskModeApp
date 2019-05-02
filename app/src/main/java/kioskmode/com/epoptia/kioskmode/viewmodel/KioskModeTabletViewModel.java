package kioskmode.com.epoptia.kioskmode.viewmodel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import domain.com.epoptia.Constants;
import domain.com.epoptia.interactor.device.UnlockDeviceUseCase;
import domain.com.epoptia.model.dto.post.UnlockDevicePostDto;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.CompletableSubject;
import kioskmode.com.epoptia.R;
import kioskmode.com.epoptia.kioskmode.KioskModeActivity;
import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.viewmodel.ObserverContextStrategy;
import kioskmode.com.epoptia.viewmodel.ViewModelObserverCreator;
import kioskmode.com.epoptia.viewmodel.models.AdminDetailsViewModel;
import kioskmode.com.epoptia.viewmodel.observercreators.UnlockDeviceViewModelObserverCreator;
import kioskmode.com.epoptia.viewmodel.observerstrategy.CompletableObserverStrategy;

public class KioskModeTabletViewModel extends KioskModeViewModel {

    //region Injections

    @Inject
    UnlockDevicePostDto unlockDevicePostDto;

    @Inject
    UnlockDeviceUseCase unlockDeviceUseCase;

    @Inject
    ObserverContextStrategy observerContextStrategy;

    //endregion

    //region Private Properties

    private static final String debugTag = KioskModeTabletViewModel.class.getSimpleName();

    private int count = 0;

    private long startMillis = 0;

    //10 -> running
    //0 -> none
    private int requestState;

    private int subjectType;

    private CompletableSubject mCompletableProcessor;

    private ViewModelObserverCreator mViewModelObserverCreator;

    private Disposable mObserverDisposable;

    //endregion

    //region Constructor

    @Inject
    public KioskModeTabletViewModel() {}

    //endregion

    //region Public Methods

    @Override
    public void onViewAttached(Bundle savedInstanceState, Lifecycle.View viewCallback) {
        super.onViewAttached(savedInstanceState, viewCallback);
    }

    @Override
    public void onViewResumed() {
        preventStatusBarExpansion();

        //Log.e(debugTag, "VIEW RESULMED => " + requestState);
        if (requestState == Constants.REQUEST_RUNNING) {
            Log.e(debugTag, "onViewResumed => request pending");

            Log.e(debugTag, mViewModelObserverCreator.getViewModelCompletableObserver() + " ON VIEW RESUMED OBSERVER");

            //todo
            //mViewCallback.setProcessing(true);
            //todo complete for all subject types
            if (subjectType == Constants.COMPLETABLE_SUBJECT) {
                observerContextStrategy.setStrategy(new CompletableObserverStrategy(mCompletableProcessor, mViewModelObserverCreator));
            }

            mObserverDisposable = observerContextStrategy.executeStrategy();
        }
    }

    @Override
    public void onViewDetached() {
        count = 0;

        startMillis = 0;

        mViewCallback = null;

        if (mObserverDisposable != null) {
            Log.e(debugTag, "onViewDetached");

            mObserverDisposable.dispose();

            mObserverDisposable = null;
        }
    }

    public void preventStatusBarExpansion() {
        //todo error handling ??
        preventStatusBarExpansionUseCase
                                    .execute()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe();
    }

    @Override
    public void removeStatusBarOverlayCustomView() {
        removeStatusBarOverlayCustomViewUseCase
                                            .execute()
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe();
    }

    @Override
    public int getKioskModeMenuLayout() {
        return R.menu.kiosk_mode_tablet_menu;
    }

    @SuppressLint("CheckResult")
    @Override
    public void unlockDeviceFromAppLogo() {
        //get system current milliseconds
        long time = System.currentTimeMillis();

        //if it is the first time, or if it has been more than 3 seconds since the first tap ( so it is like a new try), we reset everything
        if (startMillis == 0 || (time-startMillis > 3000) ) {
            startMillis = time;

            count = 1;
        } else {
            //it is not the first, and it has been less than 3 seconds since the first
            //  time-startMillis< 3000
            count++;
        }

        if (count == 5) {

            cleanUpOnDeviceUnlockUseCase
                                .execute(KioskModeActivity.class)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> mViewCallback.deviceUnlocked(), error -> {
                                    //todo ??
                                });
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void unlockDevice(AdminDetailsViewModel adminDetailsViewModel) {
        if (adminDetailsViewModel == null) {
            return;
        }

        subjectType = Constants.COMPLETABLE_SUBJECT;

        requestState = Constants.REQUEST_RUNNING;

        unlockDevicePostDto.setUsername(adminDetailsViewModel.getUsername());
        unlockDevicePostDto.setPassword(adminDetailsViewModel.getPassword());
        unlockDevicePostDto.setAction(Constants.ACTION_UNLOCK_DEVICE);

        mCompletableProcessor = CompletableSubject.create();

        mViewModelObserverCreator = new UnlockDeviceViewModelObserverCreator(this);

        Log.e(debugTag, mViewModelObserverCreator.getViewModelCompletableObserver() + " ON SUBMIT");

        mObserverDisposable = mCompletableProcessor
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(mViewModelObserverCreator.getViewModelCompletableObserver());

        unlockDeviceUseCase
                    .execute(unlockDevicePostDto)
                    //.doOnSubscribe(subscription -> mViewCallback.setProcessing(true))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(mCompletableProcessor);
    }

    @Override
    public void unlockDeviceOnSuccess() {
        clearUnnecessaryReferencesAndUnsetRequestState();

        Log.e(debugTag, "unlockDeviceOnSuccess");
    }

    @Override
    public void unlockDeviceOnError(Throwable t) {
        clearUnnecessaryReferencesAndUnsetRequestState();

        Log.e(debugTag, "unlockDeviceOnError" + t);
    }

    //endregion

    //region Private Methods

    private void clearUnnecessaryReferencesAndUnsetRequestState() {
        mObserverDisposable = null;

        mViewModelObserverCreator = null;

        //mViewCallback.setProcessing(false);

        requestState = Constants.REQUEST_NONE;
    }

    //endregion

}
