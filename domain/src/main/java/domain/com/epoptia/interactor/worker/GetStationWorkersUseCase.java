package domain.com.epoptia.interactor.worker;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import domain.com.epoptia.device.network.NetworkUtility;
import domain.com.epoptia.interactor.client.GetClientFromLocalStorageUseCase;
import domain.com.epoptia.interactor.type.FlowableUseCaseWithParameter;
import domain.com.epoptia.mappers.ListDomainWorkerToDomainBaseModelMapper;
import domain.com.epoptia.mappers.WorkerDtoToDomainWorkerModelMapper;
import domain.com.epoptia.model.domain.DomainBaseModel;
import domain.com.epoptia.model.dto.post.GetStationWorkersPostDto;
import domain.com.epoptia.repository.api.WorkerRepository;
import domain.com.epoptia.repository.localstorage.prefs.UserRepository;
import domain.com.epoptia.utilities.error.RetryWithDelay;
import domain.com.epoptia.utilities.error.ServerSuccessResponseFlowableValidatorImpl;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class GetStationWorkersUseCase implements FlowableUseCaseWithParameter<GetStationWorkersPostDto, DomainBaseModel> {

    //region Injections

    @Inject
    NetworkUtility networkUtility;

    @Inject
    UserRepository userLocalStorageRepository;

    @Inject
    GetClientFromLocalStorageUseCase getClientFromLocalStorageUseCase;

    @Inject
    WorkerRepository workerApiRepository;

    @Inject
    ServerSuccessResponseFlowableValidatorImpl serverSuccessResponseFlowableValidator;

    @Inject
    RetryWithDelay retryWithDelay;

    @Inject
    WorkerDtoToDomainWorkerModelMapper workerDtoToDomainWorkerModelMapper;

    @Inject
    ListDomainWorkerToDomainBaseModelMapper listDomainWorkerToDomainBaseModelMapper;

    //endregion

    //region Constructor

    @Inject
    public GetStationWorkersUseCase() {}

    //endregion

    //region Public Methods

    /**
     * This (flatMap -> workStationsDto...) ensures a couple of things:
     *
     * The number of Lists emitted by the Observable is maintained. i.e. if the source emits 3 lists, there will be 3 transformed lists on the other end
     * Using Observable.fromIterable() will ensure the inner Observable terminates so that toList() can be used
     *
     * @param stationWorkersPostDto
     * @return
     */
    @Override
    public Flowable<DomainBaseModel> execute(GetStationWorkersPostDto stationWorkersPostDto) {
        return userLocalStorageRepository
                .getUser()
                .delay(1000, TimeUnit.MILLISECONDS)
                .flatMap(user -> {
                    stationWorkersPostDto.setAccessToken(user.getAccessToken());

                    return Single.just(stationWorkersPostDto);
                })
                .flatMap(stationWkrsPostDto -> networkUtility.isNetworkAvailable().toSingleDefault(stationWkrsPostDto))
                .flatMap(stationWkrsPostDto -> getClientFromLocalStorageUseCase.execute())
                .toFlowable()
                .flatMap(domainClientModel -> workerApiRepository
                                                        .getStationWorkers(domainClientModel.getSubDomain(), stationWorkersPostDto)
                                                        .flatMap(stationsWorkersDto -> serverSuccessResponseFlowableValidator.validateResponse(stationsWorkersDto))
                                                        .retryWhen(retryWithDelay))
                .flatMap(stationWorkersDto ->
                        Flowable
                                .fromIterable(stationWorkersDto.getWorkers())
                                .flatMapSingle(stationWorkerDto -> workerDtoToDomainWorkerModelMapper.map(stationWorkerDto))
                                .toList()
                                .toFlowable())
                .flatMapSingle(domainWorkers -> listDomainWorkerToDomainBaseModelMapper.map(domainWorkers));
    }


    //endregion

}
