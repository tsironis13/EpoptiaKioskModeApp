package domain.com.epoptia.interactor.device;

import javax.inject.Inject;

import domain.com.epoptia.interactor.type.CompletableUseCase;
import domain.com.epoptia.repository.localstorage.prefs.DeviceRepository;
import io.reactivex.Completable;

public class SaveDeviceCategoryToLocalStorageUseCase implements CompletableUseCase {

    //region Injections

    @Inject
    domain.com.epoptia.device.Device device;

    @Inject
    DeviceRepository deviceRepository;

    //endregion

    //region Constructor

    @Inject
    public SaveDeviceCategoryToLocalStorageUseCase() {}

    //endregion

    //region Public Methods

    @Override
    public Completable execute() {
        return device
                    .getDeviceCategory()
                    .flatMapCompletable((d) -> deviceRepository.setDeviceCategory(d));
    }

    //endregion

}
