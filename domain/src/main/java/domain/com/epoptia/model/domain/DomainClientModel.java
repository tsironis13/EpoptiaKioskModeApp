package domain.com.epoptia.model.domain;

import javax.inject.Inject;

public class DomainClientModel {

    //region Private Properties

    private String subDomain;

    //endregion

    //region Constructor

    @Inject
    public DomainClientModel() {}

    //endregion

    //region Setters


    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
    }

    //endregion

    //region Getters

    public String getSubDomain() {
        return subDomain;
    }

    //endregion

}
