package domain.com.epoptia.interactor.worker;

import javax.inject.Inject;

import domain.com.epoptia.interactor.type.CompletableUseCase;
import domain.com.epoptia.repository.localstorage.prefs.WorkerRepository;
import io.reactivex.Completable;

public class ClearWorkerFromLocalStorageUseCase implements CompletableUseCase {

    //region Injections

    @Inject
    WorkerRepository workerRepository;

    //endregion

    //region Constructor

    @Inject
    public ClearWorkerFromLocalStorageUseCase() { }

    //endregion

    //region Public Methods

    @Override
    public Completable execute() {
        return workerRepository.clearWorker();
    }

    //endregion

}
