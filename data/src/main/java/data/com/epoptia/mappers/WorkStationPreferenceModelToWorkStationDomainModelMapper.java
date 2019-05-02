package data.com.epoptia.mappers;

import javax.inject.Inject;

import data.com.epoptia.localstorage.prefs.entities.Preference_WorkStation;
import domain.com.epoptia.model.domain.DomainWorkStationModel;

public class WorkStationPreferenceModelToWorkStationDomainModelMapper {

    //region Injections

    @Inject
    DomainWorkStationModel domainWorkStationModel;

    //endregion

    //region Constructor

    @Inject
    public WorkStationPreferenceModelToWorkStationDomainModelMapper() {}

    //endregion

    //region Public Methods

    public DomainWorkStationModel map(Preference_WorkStation workStationPref) {
        domainWorkStationModel.setId(workStationPref.getId());
        domainWorkStationModel.setName(workStationPref.getName());

        return domainWorkStationModel;
    }

    //endregion

}
