package domain.com.epoptia.model.domain;

import javax.inject.Inject;

public class DomainUserModel {

    //region Private Properties

    private String accessToken;

    //endregion

    //region Constructor

    @Inject
    public DomainUserModel() {}

    //endregion

    //region Setters

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    //endregion

    //region Getters

    public String getAccessToken() {
        return accessToken;
    }

    //endregion

}
