package domain.com.epoptia.model.domain;

import javax.inject.Inject;

public class DomainWorkStationModel {

    //region Private Properties

    private int id;

    private String name;

    //endregion

    //region Constructor

    @Inject
    public DomainWorkStationModel() {}

    //endregion

    //region Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    //endregion

    //region Getters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    //endregion

}
