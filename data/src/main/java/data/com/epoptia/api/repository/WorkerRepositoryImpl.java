package data.com.epoptia.api.repository;

import javax.inject.Inject;

import data.com.epoptia.api.service.BaseNetworkServiceWrapper;
import domain.com.epoptia.model.dto.post.GetStationWorkersPostDto;
import domain.com.epoptia.model.dto.result.StationWorkersDto;
import domain.com.epoptia.repository.api.WorkerRepository;
import io.reactivex.Flowable;

public class WorkerRepositoryImpl implements WorkerRepository {

    //region Injections

    @Inject
    BaseNetworkServiceWrapper baseNetworkServiceWrapper;

    //endregion

    //region Constructor

    @Inject
    public WorkerRepositoryImpl() {}

    //endregion

    //region Public Methods

    @Override
    public Flowable<StationWorkersDto> getStationWorkers(String subDomain, GetStationWorkersPostDto stationWorkersPostDto) throws Exception {
        return baseNetworkServiceWrapper.getServiceAPI(subDomain).getStationWorkers(stationWorkersPostDto);
    }

    //endregion

}
