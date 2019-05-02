package data.com.epoptia.mappers;

import javax.inject.Inject;

import data.com.epoptia.localstorage.prefs.entities.Preference_Worker;
import domain.com.epoptia.model.domain.DomainWorkerModel;

public class WorkerPreferenceModelToWorkerDomainModelMapper {

    //region Injections

    @Inject
    DomainWorkerModel domainWorkerModel;

    //endregion

    //region Constructor

    @Inject
    public WorkerPreferenceModelToWorkerDomainModelMapper() {}

    //endregion

    //region Public Methods

    public DomainWorkerModel map(Preference_Worker workerPref) {
        domainWorkerModel.setId(workerPref.getId());
        domainWorkerModel.setName(workerPref.getName());

        return domainWorkerModel;
    }

    //endregion

}
