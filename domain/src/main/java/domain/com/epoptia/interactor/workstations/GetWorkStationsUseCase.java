package domain.com.epoptia.interactor.workstations;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import domain.com.epoptia.device.network.NetworkUtility;
import domain.com.epoptia.interactor.client.GetClientFromLocalStorageUseCase;
import domain.com.epoptia.interactor.type.FlowableUseCaseWithParameter;
import domain.com.epoptia.mappers.ListDomainWorkStationToDomainBaseModelMapper;
import domain.com.epoptia.mappers.WorkStationDtoToDomainWorkStationModelMapper;
import domain.com.epoptia.model.domain.DomainBaseModel;
import domain.com.epoptia.model.dto.post.GetWorkStationsPostDto;
import domain.com.epoptia.model.dto.result.WorkStationDto;
import domain.com.epoptia.model.dto.result.WorkStationsDto;
import domain.com.epoptia.repository.api.WorkStationsRepository;
import domain.com.epoptia.repository.localstorage.prefs.UserRepository;
import domain.com.epoptia.utilities.error.RetryWithDelay;
import domain.com.epoptia.utilities.error.ServerSuccessResponseFlowableValidatorImpl;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class GetWorkStationsUseCase implements FlowableUseCaseWithParameter<GetWorkStationsPostDto, DomainBaseModel> {

    //region Injections

    @Inject
    NetworkUtility networkUtility;

    @Inject
    UserRepository userLocalStorageRepository;

    @Inject
    GetClientFromLocalStorageUseCase getClientFromLocalStorageUseCase;

    @Inject
    WorkStationsRepository workStationsApiRepository;

    @Inject
    ServerSuccessResponseFlowableValidatorImpl serverSuccessResponseFlowableValidator;

    @Inject
    RetryWithDelay retryWithDelay;

    @Inject
    WorkStationDtoToDomainWorkStationModelMapper workStationDtoToDomainWorkStationModelMapper;

    @Inject
    ListDomainWorkStationToDomainBaseModelMapper listDomainWorkStationToDomainBaseModelMapper;

    //endregion

    //region Constructor

    @Inject
    public GetWorkStationsUseCase() {}

    //endregion

    //region Public Methods

    /**
     * This (flatMap -> workStationsDto...) ensures a couple of things:
     *
     * The number of Lists emitted by the Observable is maintained. i.e. if the source emits 3 lists, there will be 3 transformed lists on the other end
     * Using Observable.fromIterable() will ensure the inner Observable terminates so that toList() can be used
     *
     * @param workStationsPostDto
     * @return
     */
    @Override
    public Flowable<DomainBaseModel> execute(GetWorkStationsPostDto workStationsPostDto) {
        return userLocalStorageRepository
                                .getUser()
                                .delay(1000, TimeUnit.MILLISECONDS)
                                .flatMap(user -> {
                                    workStationsPostDto.setAccessToken(user.getAccessToken());

                                    return Single.just(workStationsPostDto);
                                })
                                .flatMap(workStatnsPostDto -> networkUtility.isNetworkAvailable().toSingleDefault(workStatnsPostDto))
                                .flatMap(workStatnsPostDto -> getClientFromLocalStorageUseCase.execute())
                                .toFlowable()
                                .flatMap(domainClientModel -> workStationsApiRepository
                                                                                .getWorkStations(domainClientModel.getSubDomain(), workStationsPostDto)
                                                                                .flatMap(workStationsDto -> serverSuccessResponseFlowableValidator.validateResponse(workStationsDto))
                                                                                .retryWhen(retryWithDelay))
                                .flatMap(workStationsDto ->
                                        Flowable
                                            .fromIterable(workStationsDto.getWorkstations())
                                            .flatMapSingle(workStationDto -> workStationDtoToDomainWorkStationModelMapper.map(workStationDto))
                                            .toList()
                                            .toFlowable())
                                .flatMapSingle(domainWorkStations -> listDomainWorkStationToDomainBaseModelMapper.map(domainWorkStations));
    }


    //endregion

}
