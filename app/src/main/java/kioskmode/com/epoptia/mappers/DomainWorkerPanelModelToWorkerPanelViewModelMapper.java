package kioskmode.com.epoptia.mappers;

import javax.inject.Inject;

import domain.com.epoptia.model.domain.DomainWorkerPanelModel;
import io.reactivex.Single;
import kioskmode.com.epoptia.viewmodel.models.WorkerPanelViewModel;

public class DomainWorkerPanelModelToWorkerPanelViewModelMapper {

    //region Injections

    @Inject
    WorkerPanelViewModel workerPanelViewModel;

    //endregion

    //region Constructor

    @Inject
    public DomainWorkerPanelModelToWorkerPanelViewModelMapper() {}

    //endregion

    //region Public Methods

    public Single<WorkerPanelViewModel> map(DomainWorkerPanelModel domainWorkerPanelModel) {
        workerPanelViewModel.setCookie(domainWorkerPanelModel.getCookie());
        workerPanelViewModel.setUrl(domainWorkerPanelModel.getUrl());

        return Single.just(workerPanelViewModel);
    }

    //endregion

}
