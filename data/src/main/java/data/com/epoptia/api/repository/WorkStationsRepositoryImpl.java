package data.com.epoptia.api.repository;

import javax.inject.Inject;

import data.com.epoptia.api.service.BaseNetworkServiceWrapper;
import domain.com.epoptia.model.dto.post.GetWorkStationsPostDto;
import domain.com.epoptia.model.dto.result.WorkStationsDto;
import domain.com.epoptia.repository.api.WorkStationsRepository;
import io.reactivex.Flowable;

public class WorkStationsRepositoryImpl implements WorkStationsRepository {

    //region Injections

    @Inject
    BaseNetworkServiceWrapper baseNetworkServiceWrapper;

    //endregion

    //region Constructor

    @Inject
    public WorkStationsRepositoryImpl() {}

    //endregion

    //region Public Methods

    @Override
    public Flowable<WorkStationsDto> getWorkStations(String subDomain, GetWorkStationsPostDto workStationsPostDto) throws Exception {
        return baseNetworkServiceWrapper.getServiceAPI(subDomain).getWorkStations(workStationsPostDto);
    }

    //endregion

}
