package kioskmode.com.epoptia.mappers;

import javax.inject.Inject;

import domain.com.epoptia.model.domain.DomainWorkStationModel;
import kioskmode.com.epoptia.viewmodel.models.WorkStationViewModel;

public class WorkStationViewModelToWorkStationDomainModelMapper {

    //region Injections

    @Inject
    DomainWorkStationModel workStation;

    //endregion

    //region Constructor

    @Inject
    public WorkStationViewModelToWorkStationDomainModelMapper() {}

    //endregion

    //region Public Methods

    public DomainWorkStationModel map(WorkStationViewModel workStationViewModel) {
        workStation.setId(workStationViewModel.getWorkStationId());
        workStation.setName(workStationViewModel.getWorkStationName());

        return workStation;
    }

    //endregion

}
