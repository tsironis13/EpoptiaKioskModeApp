package domain.com.epoptia.repository.localstorage.prefs;

import domain.com.epoptia.model.domain.DomainDeviceModel;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface DeviceRepository {

    Completable setDeviceCategory(DomainDeviceModel device);

    Completable setDeviceModeState(DomainDeviceModel device);

    Single<DomainDeviceModel> getDevice();

}
