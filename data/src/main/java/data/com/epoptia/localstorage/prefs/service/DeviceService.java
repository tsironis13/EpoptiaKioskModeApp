package data.com.epoptia.localstorage.prefs.service;

import com.skydoves.preferenceroom.InjectPreference;

import javax.inject.Inject;

import data.com.epoptia.localstorage.prefs.components.PreferenceComponent_DeviceComponent;
import data.com.epoptia.localstorage.prefs.entities.Preference_Device;
import data.com.epoptia.mappers.DevicePreferenceModelToDeviceDomainModelMapper;
import domain.com.epoptia.Constants;
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

    public Completable setDeviceCategory(DomainDeviceModel domainDeviceModel) {
        if (domainDeviceModel == null) {
            return Completable.error(new Exception(Constants.INVALID_DOMAIN_MODEL, new NullPointerException()));
        }

        return Completable.fromAction(() -> devicePref.putCategory(domainDeviceModel.getCategory()));
    }

    public Completable setDeviceModeState(DomainDeviceModel domainDeviceModel) {
        if (domainDeviceModel == null) {
            return Completable.error(new Exception(Constants.INVALID_DOMAIN_MODEL, new NullPointerException()));
        }

        return Completable.fromAction(() -> devicePref.putModeState(domainDeviceModel.getModeState()));
    }

    public Single<DomainDeviceModel> getDevice() {
        return Single
                    .just(devicePref)
                    .map((devicePref) -> devicePreferenceModelToDeviceDomainModelMapper.map(devicePref));
    }

    //endregion

}
