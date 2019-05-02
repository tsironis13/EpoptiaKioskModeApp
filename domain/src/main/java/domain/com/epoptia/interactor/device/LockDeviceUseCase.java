package domain.com.epoptia.interactor.device;

import javax.inject.Inject;

import domain.com.epoptia.Constants;
import domain.com.epoptia.device.DeviceUtility;
import domain.com.epoptia.interactor.type.SingleUseCaseWithParameter;
import io.reactivex.Single;

public class LockDeviceUseCase implements SingleUseCaseWithParameter<Class, Boolean> {

    //region Injections

    @Inject
    DeviceUtility deviceUtility;

    @Inject
    SaveDeviceModeStateToLocalStorageUseCase saveDeviceModeStateToLocalStorageUseCase;

    //endregion

    //region Constructor

    @Inject
    public LockDeviceUseCase() {}

    //endregion

    //region Public Methods

    @Override
    public Single<Boolean> execute(Class component) {
        return deviceUtility
                        .enableComponent(component)
                        .andThen(saveDeviceModeStateToLocalStorageUseCase.execute(Constants.KIOSK_MODE_STATE))
                        .toSingleDefault(true)
                        .flatMap(componentEnabled -> deviceUtility.isMyAppLauncherDefault());
    }

    //endregion

}
