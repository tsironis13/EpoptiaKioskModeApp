package data.com.epoptia.localstorage.prefs.repository;

import javax.inject.Inject;

import data.com.epoptia.localstorage.prefs.service.DeviceService;
import domain.com.epoptia.model.domain.DomainDeviceModel;
import domain.com.epoptia.repository.localstorage.prefs.DeviceRepository;
import io.reactivex.Completable;
import io.reactivex.Single;

public class DeviceRepositoryImpl implements DeviceRepository {

    //region Injections

    @Inject
    DeviceService deviceService;

    //endregion

    //region Constructor

    @Inject
    public DeviceRepositoryImpl() { }

    //endregion

    //region Public Methods

    @Override
    public Completable setDeviceModeState(DomainDeviceModel device) {
        return deviceService.setDeviceModeState(device);
    }

    @Override
    public Completable setDeviceCategory(DomainDeviceModel device) {
        return deviceService.setDeviceCategory(device);
    }

    @Override
    public Single<DomainDeviceModel> getDevice() {
        return deviceService.getDevice();
    }

    //endregion

}
