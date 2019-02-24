package domain.com.epoptia.interactor.device;

import javax.inject.Inject;

import domain.com.epoptia.Constants;
import domain.com.epoptia.interactor.type.VoidUseCaseWithParameter;
import domain.com.epoptia.interfaces.DeviceCategory;
import domain.com.epoptia.repository.localstorage.prefs.DeviceRepository;

public class CheckDeviceCategoryUseCase implements VoidUseCaseWithParameter<DeviceCategory> {

    //region Injections

    @Inject
    DeviceRepository deviceRepository;

    //endregion

    //region Constructor

    @Inject
    public CheckDeviceCategoryUseCase() {}

    //endregion

    //region Public Methods

    @Override
    public void execute(DeviceCategory deviceCategory) {
        deviceRepository
                    .getDevice()
                    .subscribe(device -> {
                        if (device.getCategory() == Constants.TABLET) {
                            deviceCategory.deviceIsTablet();
                        } else {
                            deviceCategory.deviceIsPhone();
                        }
                    }, (error) -> {
                        //todo
                    });
    }

    //endregion

}
