package domain.com.epoptia.interactor.workstations;

import javax.inject.Inject;

import domain.com.epoptia.interactor.type.CompletableUseCase;
import domain.com.epoptia.repository.localstorage.prefs.WorkStationRepository;
import io.reactivex.Completable;

public class ClearWorkStationFromLocalStorageUseCase implements CompletableUseCase {

    //region Injections

    @Inject
    WorkStationRepository workStationRepository;

    //endregion

    //region Constructor

    @Inject
    public ClearWorkStationFromLocalStorageUseCase() { }

    //endregion

    //region Public Methods

    @Override
    public Completable execute() {
        return workStationRepository.clearWorkStation();
    }

    //endregion

}
