package domain.com.epoptia.interactor.device;

import javax.inject.Inject;

import domain.com.epoptia.interactor.type.CompletableUseCaseWithParameter;
import domain.com.epoptia.model.domain.DomainDeviceModel;
import domain.com.epoptia.repository.localstorage.prefs.DeviceRepository;
import io.reactivex.Completable;
import io.reactivex.Single;

public class SaveDeviceModeStateToLocalStorageUseCase implements CompletableUseCaseWithParameter<Integer> {

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
    public Completable execute(Integer modeState) {
        return deviceRepository
                            .getDevice()
                            .flatMap(device -> {
                                device.setModeState(modeState);

                                return Single.just(device);
                            })
                            .flatMapCompletable((deviceModel) -> deviceRepository.setDeviceModeState(deviceModel));

    }

    //endregion

}
