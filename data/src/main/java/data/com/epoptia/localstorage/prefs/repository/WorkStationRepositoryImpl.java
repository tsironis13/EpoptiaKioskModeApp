package data.com.epoptia.localstorage.prefs.repository;

import javax.inject.Inject;

import data.com.epoptia.localstorage.prefs.service.WorkStationService;
import domain.com.epoptia.model.domain.DomainWorkStationModel;
import domain.com.epoptia.repository.localstorage.prefs.WorkStationRepository;
import io.reactivex.Completable;
import io.reactivex.Single;

public class WorkStationRepositoryImpl implements WorkStationRepository {

    //region Injections

    @Inject
    WorkStationService workStationService;

    //endregion

    //region Constructor

    @Inject
    public WorkStationRepositoryImpl() { }

    //endregion

    //region Public Methods

    @Override
    public Completable setWorkStationId(DomainWorkStationModel workStation) {
        return workStationService.setWorkStationId(workStation);
    }

    @Override
    public Completable setWorkStationName(DomainWorkStationModel workStation) {
        return workStationService.setWorkStationName(workStation);
    }

    @Override
    public Completable clearWorkStation() {
        return workStationService.clearWorkStation();
    }

    @Override
    public Single<DomainWorkStationModel> getWorkStation() {
        return workStationService.getWorkStation();
    }

    //endregion

}
