package kioskmode.com.epoptia.mappers;

import javax.inject.Inject;

import domain.com.epoptia.model.domain.DomainWorkStationModel;
import io.reactivex.Single;
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

    public Single<DomainWorkStationModel> map(WorkStationViewModel workStationViewModel) {
        workStation.setId(workStationViewModel.getId());
        workStation.setName(workStationViewModel.getName());

        return Single.just(workStation);
    }

    //endregion

}
