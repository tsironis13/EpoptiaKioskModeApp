package domain.com.epoptia.interactor.workerpanel;

import javax.inject.Inject;

import domain.com.epoptia.interactor.type.SingleUseCase;
import domain.com.epoptia.model.domain.DomainWorkerPanelModel;
import domain.com.epoptia.repository.localstorage.prefs.WorkerPanelRepository;
import io.reactivex.Single;

public class GetWorkerPanelFromLocalStorageUseCase implements SingleUseCase<DomainWorkerPanelModel> {

    //region Injections

    @Inject
    WorkerPanelRepository workerPanelRepository;

    //endregion

    //region Constructor

    @Inject
    public GetWorkerPanelFromLocalStorageUseCase() { }

    //endregion

    //region Public Methods

    @Override
    public Single<DomainWorkerPanelModel> execute() {
        return workerPanelRepository.getWorkerPanel();
    }

    //endregion

}
