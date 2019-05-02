package domain.com.epoptia.mappers;

import javax.inject.Inject;

import domain.com.epoptia.model.domain.DomainWorkStationModel;
import domain.com.epoptia.model.dto.result.WorkStationDto;

public class WorkStationDtoToDomainWorkStationModelMapper {

    //region Constructor

    @Inject
    public WorkStationDtoToDomainWorkStationModelMapper() {}

    //endregion

    //region Public Methods

    public DomainWorkStationModel map(WorkStationDto workStationDto) {
        //avoid same DomainWorkStationModel instance
        DomainWorkStationModel domainWorkStationModel = new DomainWorkStationModel();

        domainWorkStationModel.setId(workStationDto.getId());
        domainWorkStationModel.setName(workStationDto.getName());

        return domainWorkStationModel;
    }

    //endregion

}
