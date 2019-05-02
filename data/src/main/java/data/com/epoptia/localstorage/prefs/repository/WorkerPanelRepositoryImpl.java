package data.com.epoptia.localstorage.prefs.repository;

import javax.inject.Inject;

import data.com.epoptia.localstorage.prefs.service.WorkerPanelService;
import domain.com.epoptia.model.domain.DomainWorkerPanelModel;
import domain.com.epoptia.repository.localstorage.prefs.WorkerPanelRepository;
import io.reactivex.Completable;
import io.reactivex.Single;

public class WorkerPanelRepositoryImpl implements WorkerPanelRepository {

    //region Injections

    @Inject
    WorkerPanelService workerPanelService;

    //endregion

    //region Constructor

    @Inject
    public WorkerPanelRepositoryImpl() { }

    //endregion

    //region Public Methods

    @Override
    public Completable clearWorkerPanel() {
        return workerPanelService.clearWorkerPanel();
    }

    @Override
    public Single<DomainWorkerPanelModel> getWorkerPanel() {
        return workerPanelService.getWorkerPanel();
    }

    //endregion

}
