package domain.com.epoptia.model.domain;

import javax.inject.Inject;

public class DomainNetworkStateModel {

    //region Private Properties

    private String msg;

    //endregion

    //region Constructor

    @Inject
    public DomainNetworkStateModel() {}

    //endregion

    //region Setters

    public void setMsg(String msg) {
        this.msg = msg;
    }

    //endregion

    //region Getters

    public String getMsg() {
        return msg;
    }

    //endregion

}
