package domain.com.epoptia.interactor.device;

import javax.inject.Inject;

import domain.com.epoptia.interactor.type.CompletableUseCaseWithParameter;
import domain.com.epoptia.model.domain.DomainDeviceModel;
import domain.com.epoptia.repository.localstorage.prefs.DeviceRepository;
import io.reactivex.Completable;

public class SaveDeviceModeStateToLocalStorageUseCase implements CompletableUseCaseWithParameter<DomainDeviceModel> {

    //region Injections

    @Inject
    DeviceRepository deviceRepository;

    //endregion

    //region Constructor

    @Inject
    public SaveDeviceModeStateToLocalStorageUseCase() {}

    //endregion

    //region Public Methods

    @Override
    public Completable execute(DomainDeviceModel device) {
        return deviceRepository.setDeviceModeState(device);
    }

    //endregion

}
