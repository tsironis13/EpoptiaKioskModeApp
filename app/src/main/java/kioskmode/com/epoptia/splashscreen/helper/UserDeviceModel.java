package kioskmode.com.epoptia.splashscreen.helper;

import javax.inject.Inject;

import domain.com.epoptia.model.domain.DomainDeviceModel;
import domain.com.epoptia.model.domain.DomainUserModel;

public class UserDeviceModel {

    //region Private Properties

    private DomainUserModel domainUserModel;

    private DomainDeviceModel domainDeviceModel;

    //endregion

    //region Constructor

    @Inject
    public UserDeviceModel() {}

    //endregion

    //region Setters


    public void setDomainUserModel(DomainUserModel domainUserModel) {
        this.domainUserModel = domainUserModel;
    }

    public void setDomainDeviceModel(DomainDeviceModel domainDeviceModel) {
        this.domainDeviceModel = domainDeviceModel;
    }

    //endregion

    //region Getters


    public DomainUserModel getDomainUserModel() {
        return domainUserModel;
    }

    public DomainDeviceModel getDomainDeviceModel() {
        return domainDeviceModel;
    }

    //endregion

}
