package domain.com.epoptia.model.domain;

import javax.inject.Inject;

public class DomainWorkerPanelModel {

    //region Private Properties

    private String cookie;

    private String url;

    //endregion

    //region Constructor

    @Inject
    public DomainWorkerPanelModel() {}

    //endregion

    //region Setters

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //endregion

    //region Getters

    public String getCookie() {
        return cookie;
    }

    public String getUrl() {
        return url;
    }

    //endregion

}
