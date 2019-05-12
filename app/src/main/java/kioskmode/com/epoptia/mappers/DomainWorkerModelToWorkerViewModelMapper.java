package kioskmode.com.epoptia.mappers;

import javax.inject.Inject;

import domain.com.epoptia.model.domain.DomainWorkerModel;
import io.reactivex.Single;
import kioskmode.com.epoptia.viewmodel.models.WorkerViewModel;

public class DomainWorkerModelToWorkerViewModelMapper {

    //region Injections

    //endregion

    //region Constructor

    @Inject
    public DomainWorkerModelToWorkerViewModelMapper() {}

    //endregion

    //region Public Methods

    public Single<WorkerViewModel> map(DomainWorkerModel domainWorkerModel) {
        WorkerViewModel workerViewModel = new WorkerViewModel();

        workerViewModel.setId(domainWorkerModel.getId());
        workerViewModel.setName(domainWorkerModel.getName());

        return Single.just(workerViewModel);
    }

    //endregion

}
