package kioskmode.com.epoptia.splashscreen.helper;

import javax.inject.Inject;

public class UserDeviceModel {

    //region Private Properties

    private String userAccessToken;

    private int deviceModeState;

    //endregion

    //region Constructor

    @Inject
    public UserDeviceModel() {}

    //endregion

    //region Setters

    public void setUserAccessToken(String userAccessToken) {
        this.userAccessToken = userAccessToken;
    }

    public void setDeviceModeState(int deviceModeState) {
        this.deviceModeState = deviceModeState;
    }

    //endregion

    //region Getters

    public String getUserAccessToken() {
        return userAccessToken;
    }

    public int getDeviceModeState() {
        return deviceModeState;
    }

    //endregion

}
