package domain.com.epoptia.mappers;

import java.util.List;

import javax.inject.Inject;

import domain.com.epoptia.model.domain.DomainBaseModel;
import domain.com.epoptia.model.domain.DomainWorkStationModel;

public class ListDomainWorkStationToDomainBaseModelMapper {

    //region Injections

    @Inject
    DomainBaseModel domainBaseModel;

    //endregion

    //region Constructor

    @Inject
    public ListDomainWorkStationToDomainBaseModelMapper() {}

    //endregion

    //region Public Methods

    public DomainBaseModel map(List<DomainWorkStationModel> workStations) {
        domainBaseModel.setWorkStationModels(workStations);

        return domainBaseModel;
    }

    //endregion

}
