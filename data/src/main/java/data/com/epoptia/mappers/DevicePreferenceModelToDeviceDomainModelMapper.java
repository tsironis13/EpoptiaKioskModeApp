package data.com.epoptia.mappers;

import javax.inject.Inject;

import data.com.epoptia.localstorage.prefs.entities.Preference_Device;
import domain.com.epoptia.model.domain.DomainDeviceModel;

public class DevicePreferenceModelToDeviceDomainModelMapper {

    //region Injections

    @Inject
    DomainDeviceModel domainDeviceModel;

    //endregion

    //region Constructor

    @Inject
    public DevicePreferenceModelToDeviceDomainModelMapper() {}

    //endregion

    //region Public Methods

    public DomainDeviceModel map(Preference_Device devicePref) {
        domainDeviceModel.setModeState(devicePref.getModeState());
        domainDeviceModel.setCategory(devicePref.getCategory());

        return domainDeviceModel;
    }

    //endregion

}
