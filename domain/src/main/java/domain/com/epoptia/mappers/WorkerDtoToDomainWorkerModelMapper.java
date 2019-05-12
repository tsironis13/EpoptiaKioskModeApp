package domain.com.epoptia.mappers;

import javax.inject.Inject;

import domain.com.epoptia.model.domain.DomainWorkerModel;
import domain.com.epoptia.model.dto.result.WorkerDto;
import io.reactivex.Single;

public class WorkerDtoToDomainWorkerModelMapper {

    //region Constructor

    @Inject
    public WorkerDtoToDomainWorkerModelMapper() {}

    //endregion

    //region Public Methods

    public Single<DomainWorkerModel> map(WorkerDto workerDto) {
        //avoid same DomainWorkStationModel instance
        DomainWorkerModel domainWorkerModel = new DomainWorkerModel();

        domainWorkerModel.setId(workerDto.getId());
        domainWorkerModel.setName(workerDto.getName());

        return Single.just(domainWorkerModel);
    }

    //endregion

}
