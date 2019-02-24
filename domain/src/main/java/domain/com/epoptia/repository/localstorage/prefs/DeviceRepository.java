package domain.com.epoptia.repository.localstorage.prefs;

import domain.com.epoptia.model.domain.DomainDeviceModel;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface DeviceRepository {

    Completable setDeviceModeState(DomainDeviceModel device);

    Completable setDeviceCategory(DomainDeviceModel device);

    Single<DomainDeviceModel> getDevice();

}
