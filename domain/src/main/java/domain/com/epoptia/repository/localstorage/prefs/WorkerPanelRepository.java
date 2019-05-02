package domain.com.epoptia.repository.localstorage.prefs;

import domain.com.epoptia.model.domain.DomainWorkerPanelModel;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface WorkerPanelRepository {

    Single<DomainWorkerPanelModel> getWorkerPanel();

    Completable clearWorkerPanel();

}
