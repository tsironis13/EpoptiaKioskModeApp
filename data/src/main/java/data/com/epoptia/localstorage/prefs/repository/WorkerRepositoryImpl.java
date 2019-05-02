package data.com.epoptia.localstorage.prefs.repository;

import javax.inject.Inject;

import data.com.epoptia.localstorage.prefs.service.WorkerService;
import domain.com.epoptia.model.domain.DomainWorkerModel;
import domain.com.epoptia.repository.localstorage.prefs.WorkerRepository;
import io.reactivex.Completable;
import io.reactivex.Single;

public class WorkerRepositoryImpl implements WorkerRepository {

    //region Injections

    @Inject
    WorkerService workerService;

    //endregion

    //region Constructor

    @Inject
    public WorkerRepositoryImpl() { }

    //endregion

    //region Public Methods

    @Override
    public Completable setWorkerId(DomainWorkerModel worker) {
        return workerService.setWorkerId(worker);
    }

    @Override
    public Completable setWorkerName(DomainWorkerModel worker) {
        return workerService.setWorkerName(worker);
    }

    @Override
    public Completable clearWorker() {
        return workerService.clearWorker();
    }

    @Override
    public Single<DomainWorkerModel> getWorker() {
        return workerService.getWorker();
    }

    //endregion

}
