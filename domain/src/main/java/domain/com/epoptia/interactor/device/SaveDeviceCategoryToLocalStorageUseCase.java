package domain.com.epoptia.interactor.device;

import javax.inject.Inject;

import domain.com.epoptia.device.DeviceUtility;
import domain.com.epoptia.interactor.type.CompletableUseCase;
import domain.com.epoptia.model.domain.DomainDeviceModel;
import domain.com.epoptia.repository.localstorage.prefs.DeviceRepository;
import io.reactivex.Completable;
import io.reactivex.Single;

public class SaveDeviceCategoryToLocalStorageUseCase implements CompletableUseCase {

    //region Injections

    @Inject
    DeviceUtility deviceUtility;

    @Inject
    DeviceRepository deviceRepository;

    @Inject
    DomainDeviceModel domainDeviceModel;

    //endregion

    //region Constructor

    @Inject
    public SaveDeviceCategoryToLocalStorageUseCase() {}

    //endregion

    //region Public Methods

    @Override
    public Completable execute() {
        return Single.zip(deviceRepository.getDevice(), deviceUtility.getDeviceCategory(), (device, category) -> {
            domainDeviceModel.setModeState(device.getModeState());
            domainDeviceModel.setCategory(category);

            return domainDeviceModel;
        })
        .flatMapCompletable((deviceModel) -> deviceRepository.setDeviceCategory(deviceModel));
    }

    //endregion

}
