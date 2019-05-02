package kioskmode.com.epoptia.mappers;

import javax.inject.Inject;

import domain.com.epoptia.model.domain.DomainWorkerModel;
import kioskmode.com.epoptia.viewmodel.models.WorkerViewModel;

public class DomainWorkerModelToWorkerViewModelMapper {

    //region Injections

    @Inject
    WorkerViewModel workerViewModel;

    //endregion

    //region Constructor

    @Inject
    public DomainWorkerModelToWorkerViewModelMapper() {}

    //endregion

    //region Public Methods

    public WorkerViewModel map(DomainWorkerModel domainWorkerModel) {
        workerViewModel.setWorkerId(domainWorkerModel.getId());
        workerViewModel.setWorkerName(domainWorkerModel.getName());

        return workerViewModel;
    }

    //endregion

}
