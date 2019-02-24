package domain.com.epoptia.interactor.device;

import javax.inject.Inject;

import domain.com.epoptia.interactor.type.SingleUseCase;
import domain.com.epoptia.model.domain.DomainDeviceModel;
import domain.com.epoptia.repository.localstorage.prefs.DeviceRepository;
import io.reactivex.Single;

public class GetDeviceFromLocalStorageUseCase implements SingleUseCase<DomainDeviceModel> {

    //region Injections

    @Inject
    DeviceRepository localStorageDeviceRepository;

    //endregion

    //region Constructor

    @Inject
    public GetDeviceFromLocalStorageUseCase() { }

    //endregion

    //region Public Methods

    @Override
    public Single<DomainDeviceModel> execute() {
        return localStorageDeviceRepository.getDevice();
    }

    //endregion

}
