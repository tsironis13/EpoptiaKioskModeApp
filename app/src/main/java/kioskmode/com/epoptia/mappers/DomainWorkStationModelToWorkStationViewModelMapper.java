package kioskmode.com.epoptia.mappers;

import javax.inject.Inject;

import domain.com.epoptia.model.domain.DomainWorkStationModel;
import io.reactivex.Single;
import kioskmode.com.epoptia.viewmodel.models.WorkStationViewModel;

public class DomainWorkStationModelToWorkStationViewModelMapper {

    //region Injections

    //endregion

    //region Constructor

    @Inject
    public DomainWorkStationModelToWorkStationViewModelMapper() {}

    //endregion

    //region Public Methods

    public Single<WorkStationViewModel> map(DomainWorkStationModel domainWorkStationModel) {
        WorkStationViewModel workStationViewModel = new WorkStationViewModel();

        workStationViewModel.setId(domainWorkStationModel.getId());
        workStationViewModel.setName(domainWorkStationModel.getName());

        return Single.just(workStationViewModel);
    }

    //endregion

}
