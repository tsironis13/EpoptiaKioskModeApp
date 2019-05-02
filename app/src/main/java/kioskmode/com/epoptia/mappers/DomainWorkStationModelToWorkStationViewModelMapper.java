package kioskmode.com.epoptia.mappers;

import javax.inject.Inject;

import domain.com.epoptia.model.domain.DomainWorkStationModel;
import kioskmode.com.epoptia.viewmodel.models.WorkStationViewModel;

public class DomainWorkStationModelToWorkStationViewModelMapper {

    //region Injections

    @Inject
    WorkStationViewModel workStationViewModel;

    //endregion

    //region Constructor

    @Inject
    public DomainWorkStationModelToWorkStationViewModelMapper() {}

    //endregion

    //region Public Methods

    public WorkStationViewModel map(DomainWorkStationModel domainWorkStationModel) {
        workStationViewModel.setWorkStationId(domainWorkStationModel.getId());
        workStationViewModel.setWorkStationName(domainWorkStationModel.getName());

        return workStationViewModel;
    }

    //endregion

}
