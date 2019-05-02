package domain.com.epoptia.model.domain;

import java.util.List;

import javax.inject.Inject;

//todo add usage
public class DomainBaseModel {

    //region Private Properties

    private List<DomainWorkStationModel> workStationModels;

    //endregion

    //region Constructor

    @Inject
    public DomainBaseModel() {}

    //endregion

    //region Setters

    public void setWorkStationModels(List<DomainWorkStationModel> workStationModels) {
        this.workStationModels = workStationModels;
    }

    //endregion

    //region Getters

    public List<DomainWorkStationModel> getWorkStationModels() {
        return workStationModels;
    }

    //endregion

}
