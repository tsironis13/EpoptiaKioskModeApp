package domain.com.epoptia.interactor.workstation;

import javax.inject.Inject;

import domain.com.epoptia.interactor.type.SingleUseCase;
import domain.com.epoptia.model.domain.DomainWorkStationModel;
import domain.com.epoptia.repository.localstorage.prefs.WorkStationRepository;
import io.reactivex.Single;

public class GetWorkStationFromLocalStorageUseCase implements SingleUseCase<DomainWorkStationModel> {

    //region Injections

    @Inject
    WorkStationRepository workerStationRepository;

    //endregion

    //region Constructor

    @Inject
    public GetWorkStationFromLocalStorageUseCase() { }

    //endregion

    //region Public Methods

    @Override
    public Single<DomainWorkStationModel> execute() {
        return workerStationRepository.getWorkStation();
    }

    //endregion

}
