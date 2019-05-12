package domain.com.epoptia.interactor.workstation;

import javax.inject.Inject;

import domain.com.epoptia.interactor.type.CompletableUseCaseWithParameter;
import domain.com.epoptia.model.domain.DomainWorkStationModel;
import domain.com.epoptia.repository.localstorage.prefs.WorkStationRepository;
import io.reactivex.Completable;

public class SaveWorkStationToLocalStorageUseCase implements CompletableUseCaseWithParameter<DomainWorkStationModel> {

    //region Injections

    @Inject
    WorkStationRepository workStationRepository;

    //endregion

    //region Constructor

    @Inject
    public SaveWorkStationToLocalStorageUseCase() { }

    //endregion

    //region Public Methods

    @Override
    public Completable execute(DomainWorkStationModel workStation) {
        return workStationRepository
                                .setWorkStationId(workStation)
                                .andThen(workStationRepository.setWorkStationName(workStation));
    }

    //endregion

}
