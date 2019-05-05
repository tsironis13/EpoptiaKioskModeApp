package kioskmode.com.epoptia.kioskmode.viewmodel;

import android.annotation.SuppressLint;
import android.os.Bundle;

import javax.inject.Inject;

import domain.com.epoptia.interactor.device.GetDeviceFromLocalStorageUseCase;
import domain.com.epoptia.interactor.device.PreventStatusBarExpansionUseCase;
import domain.com.epoptia.interactor.device.RemoveStatusBarOverlayCustomViewUseCase;
import domain.com.epoptia.interactor.device.CleanUpOnDeviceUnlockUseCase;
import domain.com.epoptia.interactor.worker.GetWorkerFromLocalStorageUseCase;
import domain.com.epoptia.interactor.workerpanel.GetWorkerPanelFromLocalStorageUseCase;
import domain.com.epoptia.interactor.workstations.GetWorkStationFromLocalStorageUseCase;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.mappers.DomainDeviceModelToDeviceViewModelMapper;
import kioskmode.com.epoptia.mappers.DomainWorkStationModelToWorkStationViewModelMapper;
import kioskmode.com.epoptia.mappers.DomainWorkerModelToWorkerViewModelMapper;
import kioskmode.com.epoptia.mappers.DomainWorkerPanelModelToWorkerPanelViewModelMapper;
import kioskmode.com.epoptia.viewmodel.models.AdminDetailsViewModel;

public class KioskModeViewModel implements KioskModeContract.ViewModel {

    //region Injections

    @Inject
    PreventStatusBarExpansionUseCase preventStatusBarExpansionUseCase;

    @Inject
    RemoveStatusBarOverlayCustomViewUseCase removeStatusBarOverlayCustomViewUseCase;

    @Inject
    GetDeviceFromLocalStorageUseCase getDeviceFromLocalStorageUseCase;

    @Inject
    GetWorkStationFromLocalStorageUseCase getWorkStationFromLocalStorageUseCase;

    @Inject
    GetWorkerPanelFromLocalStorageUseCase getWorkerPanelFromLocalStorageUseCase;

    @Inject
    GetWorkerFromLocalStorageUseCase getWorkerFromLocalStorageUseCase;

    @Inject
    DomainDeviceModelToDeviceViewModelMapper domainDeviceModelToDeviceViewModelMapper;

    @Inject
    DomainWorkStationModelToWorkStationViewModelMapper domainWorkStationModelToWorkStationViewModelMapper;

    @Inject
    DomainWorkerPanelModelToWorkerPanelViewModelMapper domainWorkerPanelModelToWorkerPanelViewModelMapper;

    @Inject
    DomainWorkerModelToWorkerViewModelMapper domainWorkerModelToWorkerViewModelMapper;

    @Inject
    kioskmode.com.epoptia.viewmodel.models.KioskModeViewModel kioskModeViewModel;

    //endregion

    //region Public Properties

    public KioskModeContract.View mViewCallback;

    //endregion

    //region Constructor

    @Inject
    public KioskModeViewModel() {}

    //endregion

    //region Public Methods

    @Override
    public void onViewAttached(Bundle savedInstanceState, Lifecycle.View viewCallback) {
        mViewCallback = (KioskModeContract.View) viewCallback;

        initialize();
    }

    @Override
    public void onViewResumed() {

    }

    @Override
    public void onViewDetached() {

    }

    @Override
    public void removeStatusBarOverlayCustomView() {}

    @SuppressLint("CheckResult")
    public void initialize() {
        Single.zip(
                getDeviceFromLocalStorageUseCase
                                .execute()
                                .flatMap(domainDeviceModel -> domainDeviceModelToDeviceViewModelMapper.map(domainDeviceModel)),
                getWorkStationFromLocalStorageUseCase
                                .execute()
                                .flatMap(domainWorkStationModel -> domainWorkStationModelToWorkStationViewModelMapper.map(domainWorkStationModel)),
                getWorkerFromLocalStorageUseCase
                                .execute()
                                .flatMap(domainWorkerModel -> domainWorkerModelToWorkerViewModelMapper.map(domainWorkerModel)),
                getWorkerPanelFromLocalStorageUseCase
                                .execute()
                                .flatMap(domainWorkerPanelModel -> domainWorkerPanelModelToWorkerPanelViewModelMapper.map(domainWorkerPanelModel)),
                ((deviceViewModel, workStationViewModel, workerViewModel, workerPanelViewModel) -> {

                    kioskModeViewModel.setDeviceViewModel(deviceViewModel);
                    kioskModeViewModel.setWorkStationViewModel(workStationViewModel);
                    kioskModeViewModel.setWorkerViewModel(workerViewModel);
                    kioskModeViewModel.setWorkerPanelViewModel(workerPanelViewModel);

                    return kioskModeViewModel;
                })

        )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(kioskModeViewModel -> {
            int x = 10;
        }, error -> {
            //todo
        });
    }

    @Override
    public int getKioskModeMenuLayout() {
        return 0;
    }

    @Override
    public void unlockDeviceFromAppLogo() {}

    @Override
    public void unlockDevice(AdminDetailsViewModel adminDetailsViewModel) {}

    @Override
    public void unlockDeviceOnSuccess() {}

    @Override
    public void unlockDeviceOnError(Throwable t) {}

    //endregion

}
