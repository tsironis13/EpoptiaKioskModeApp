package domain.com.epoptia.interactor.workerpanel;

import javax.inject.Inject;

import domain.com.epoptia.interactor.type.CompletableUseCase;
import domain.com.epoptia.repository.localstorage.prefs.WorkerPanelRepository;
import io.reactivex.Completable;

public class ClearWorkerPanelFromLocalStorageUseCase implements CompletableUseCase {

    //region Injections

    @Inject
    WorkerPanelRepository workerPanelRepository;

    //endregion

    //region Constructor

    @Inject
    public ClearWorkerPanelFromLocalStorageUseCase() { }

    //endregion

    //region Public Methods

    @Override
    public Completable execute() {
        return workerPanelRepository.clearWorkerPanel();
    }

    //endregion

}
