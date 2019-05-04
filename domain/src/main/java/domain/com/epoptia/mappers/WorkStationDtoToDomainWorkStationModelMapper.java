package domain.com.epoptia.mappers;

import javax.inject.Inject;

import domain.com.epoptia.model.domain.DomainWorkStationModel;
import domain.com.epoptia.model.dto.result.WorkStationDto;
import io.reactivex.Single;

public class WorkStationDtoToDomainWorkStationModelMapper {

    //region Constructor

    @Inject
    public WorkStationDtoToDomainWorkStationModelMapper() {}

    //endregion

    //region Public Methods

    public Single<DomainWorkStationModel> map(WorkStationDto workStationDto) {
        //avoid same DomainWorkStationModel instance
        DomainWorkStationModel domainWorkStationModel = new DomainWorkStationModel();

        domainWorkStationModel.setId(workStationDto.getId());
        domainWorkStationModel.setName(workStationDto.getName());

        return Single.just(domainWorkStationModel);
    }

    //endregion

}
