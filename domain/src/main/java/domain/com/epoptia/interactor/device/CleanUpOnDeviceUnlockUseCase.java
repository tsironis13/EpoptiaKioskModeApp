package domain.com.epoptia.interactor.device;

import javax.inject.Inject;

import domain.com.epoptia.Constants;
import domain.com.epoptia.device.DeviceUtility;
import domain.com.epoptia.interactor.type.CompletableUseCaseWithParameter;
import domain.com.epoptia.interactor.worker.ClearWorkerFromLocalStorageUseCase;
import domain.com.epoptia.interactor.workerpanel.ClearWorkerPanelFromLocalStorageUseCase;
import domain.com.epoptia.interactor.workstations.ClearWorkStationFromLocalStorageUseCase;
import io.reactivex.Completable;

public class CleanUpOnDeviceUnlockUseCase implements CompletableUseCaseWithParameter<Class> {

    //region Injections

    @Inject
    DeviceUtility deviceUtility;

    @Inject
    SaveDeviceModeStateToLocalStorageUseCase saveDeviceModeStateToLocalStorageUseCase;

    @Inject
    ClearWorkerPanelFromLocalStorageUseCase clearWorkerPanelFromLocalStorageUseCase;

    @Inject
    ClearWorkerFromLocalStorageUseCase clearWorkerFromLocalStorageUseCase;

    @Inject
    ClearWorkStationFromLocalStorageUseCase clearWorkStationFromLocalStorageUseCase;

    //endregion

    //region Constructor

    @Inject
    public CleanUpOnDeviceUnlockUseCase() {}

    //endregion

    //region Public Methods

    @Override
    public Completable execute(Class component) {
        return deviceUtility
                    .disableComponent(component)
                    .andThen(saveDeviceModeStateToLocalStorageUseCase.execute(Constants.DEFAULT_MODE_STATE))
                    .andThen(clearWorkerFromLocalStorageUseCase.execute())
                    .andThen(clearWorkerPanelFromLocalStorageUseCase.execute())
                    .andThen(clearWorkStationFromLocalStorageUseCase.execute());
    }

    //endregion

}
