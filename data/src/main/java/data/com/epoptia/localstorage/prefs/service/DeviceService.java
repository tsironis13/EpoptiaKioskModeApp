package data.com.epoptia.localstorage.prefs.service;

import com.skydoves.preferenceroom.InjectPreference;

import javax.inject.Inject;

import data.com.epoptia.localstorage.prefs.components.PreferenceComponent_DeviceComponent;
import data.com.epoptia.localstorage.prefs.entities.Preference_Device;
import data.com.epoptia.mappers.DevicePreferenceModelToDeviceDomainModelMapper;
import domain.com.epoptia.model.domain.DomainDeviceModel;
import io.reactivex.Completable;
import io.reactivex.Single;

public class DeviceService {

    //region Injections

    @InjectPreference
    public PreferenceComponent_DeviceComponent deviceComponent;

    @InjectPreference
    public Preference_Device devicePref;

    @Inject
    DevicePreferenceModelToDeviceDomainModelMapper devicePreferenceModelToDeviceDomainModelMapper;

    //endregion

    //region Constructor

    @Inject
    public DeviceService() {
        PreferenceComponent_DeviceComponent.getInstance().inject(this);
    }

    //endregion

    //region Public Methods

    public Completable setDeviceModeState(DomainDeviceModel device) {
        if (device == null) {
            //todo error ??
            return Completable.error(new Exception("Device is null"));
        }

        return Completable.fromAction(() -> devicePref.putModeState(device.getModeState()));
    }

    public Completable setDeviceCategory(DomainDeviceModel device) {
        if (device == null) {
            //todo error ??
            return Completable.error(new Exception("Device is null"));
        }

        return Completable.fromAction(() -> devicePref.putCategory(device.getCategory()));
    }

    public Single<DomainDeviceModel> getDevice() {
        return Single
                    .just(devicePref)
                    .map((d) -> devicePreferenceModelToDeviceDomainModelMapper.map(d));
    }

    //endregion

}
