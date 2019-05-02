package domain.com.epoptia.repository.localstorage.prefs;

import domain.com.epoptia.model.domain.DomainWorkerModel;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface WorkerRepository {

    Completable setWorkerId(DomainWorkerModel worker);

    Completable setWorkerName(DomainWorkerModel worker);

    Completable clearWorker();

    Single<DomainWorkerModel> getWorker();

}
