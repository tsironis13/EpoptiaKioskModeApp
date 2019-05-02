package domain.com.epoptia.interactor.worker;

import javax.inject.Inject;

import domain.com.epoptia.interactor.type.SingleUseCase;
import domain.com.epoptia.model.domain.DomainWorkerModel;
import domain.com.epoptia.repository.localstorage.prefs.WorkerRepository;
import io.reactivex.Single;

public class GetWorkerFromLocalStorageUseCase implements SingleUseCase<DomainWorkerModel> {

    //region Injections

    @Inject
    WorkerRepository workerRepository;

    //endregion

    //region Constructor

    @Inject
    public GetWorkerFromLocalStorageUseCase() { }

    //endregion

    //region Public Methods

    @Override
    public Single<DomainWorkerModel> execute() {
        return workerRepository.getWorker();
    }

    //endregion

}
