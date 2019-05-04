package domain.com.epoptia.interactor.client;

import javax.inject.Inject;

import domain.com.epoptia.device.network.NetworkUtility;
import domain.com.epoptia.interactor.type.CompletableUseCaseWithParameter;
import domain.com.epoptia.mappers.ValidateClientSubDomainPostDtoToDomainClientModelMapper;
import domain.com.epoptia.model.dto.post.ValidateClientSubDomainPostDto;
import domain.com.epoptia.model.dto.result.ResponseWrapperDto;
import domain.com.epoptia.model.dto.result.UserDto;
import domain.com.epoptia.repository.api.ClientRepository;
import domain.com.epoptia.utilities.error.RetryWithDelay;
import domain.com.epoptia.utilities.error.ServerSuccessResponseSingleValidatorImpl;
import domain.com.epoptia.validator.ClientSubDomainPostDtoValidator;
import io.reactivex.Completable;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class ValidateClientSubDomainUseCase implements CompletableUseCaseWithParameter<ValidateClientSubDomainPostDto> {

    //region Injections

    @Inject
    NetworkUtility networkUtility;

    @Inject
    domain.com.epoptia.repository.localstorage.prefs.ClientRepository clientLocalStorageRepository;

    @Inject
    ClientRepository clientApiRepository;

    @Inject
    ClientSubDomainPostDtoValidator clientSubDomainDtoValidator;

    @Inject
    RetryWithDelay retryWithDelay;

    @Inject
    ResponseWrapperDto responseWrapperDto;

    @Inject
    SaveClientSubDomainToLocalStorageUseCase saveClientSubDomainToLocalStorageUseCase;

    @Inject
    ValidateClientSubDomainPostDtoToDomainClientModelMapper validateClientSubDomainPostDtoToDomainClientModelMapper;

    @Inject
    ServerSuccessResponseSingleValidatorImpl serverSuccessResponseSingleValidator;

    //endregion

    //region Constructor

    @Inject
    public ValidateClientSubDomainUseCase() {}

    //endregion

    //region Public Methods

    @Override
    public Completable execute(ValidateClientSubDomainPostDto validateClientSubDomainDto) {
        return clientSubDomainDtoValidator
                                .validate(validateClientSubDomainDto)
                                .flatMap(dto -> networkUtility.isNetworkAvailable().toSingleDefault(dto))
                                .flatMap(dto -> clientApiRepository
                                                            .validateClientSubDomain(dto.getClientSubDomain(), validateClientSubDomainDto)
                                                            .flatMap((Function<UserDto, SingleSource<UserDto>>) userDto -> serverSuccessResponseSingleValidator.validateResponse(userDto))
                                                            .retryWhen(retryWithDelay))
                                .flatMap((data) -> validateClientSubDomainPostDtoToDomainClientModelMapper.map(validateClientSubDomainDto))
                                .flatMapCompletable((data) -> saveClientSubDomainToLocalStorageUseCase.execute(data));
    }

    //endregion

}
