package domain.com.epoptia.interactor.user;

import javax.inject.Inject;

import domain.com.epoptia.device.network.NetworkUtility;
import domain.com.epoptia.interactor.client.GetClientFromLocalStorageUseCase;
import domain.com.epoptia.interactor.type.CompletableUseCaseWithParameter;
import domain.com.epoptia.mappers.UserDtoToDomainUserModelMapper;
import domain.com.epoptia.model.dto.post.LoginAdminPostDto;
import domain.com.epoptia.model.dto.result.UserDto;
import domain.com.epoptia.repository.api.UserRepository;
import domain.com.epoptia.utilities.error.RetryWithDelay;
import domain.com.epoptia.utilities.error.ServerSuccessResponseSingleValidatorImpl;
import domain.com.epoptia.validator.LoginAdminPostDtoValidator;
import io.reactivex.Completable;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class LoginAdminUseCase implements CompletableUseCaseWithParameter<LoginAdminPostDto> {

    //region Injections

    @Inject
    NetworkUtility networkUtility;

    @Inject
    LoginAdminPostDtoValidator loginAdminPostDtoValidator;

    @Inject
    GetClientFromLocalStorageUseCase getClientFromLocalStorageUseCase;

    @Inject
    UserRepository userApiRepository;

    @Inject
    ServerSuccessResponseSingleValidatorImpl serverSuccessResponseSingleValidator;

    @Inject
    RetryWithDelay retryWithDelay;

    @Inject
    UserDtoToDomainUserModelMapper userDtoToDomainUserModelMapper;

    @Inject
    SaveAccessTokenToLocalStorageUseCase saveAccessTokenToLocalStorageUseCase;

    //endregion

    //region Constructor

    @Inject
    public LoginAdminUseCase() {}

    //endregion

    //region Public Methods

    @Override
    public Completable execute(LoginAdminPostDto loginAdminPostDto) {
        return loginAdminPostDtoValidator
                                    .validate(loginAdminPostDto)
                                    .flatMap(loginAdmnPostDto -> networkUtility.isNetworkAvailable().toSingleDefault(loginAdmnPostDto))
                                    .flatMap(loginAdmnPostDto -> getClientFromLocalStorageUseCase.execute())
                                    .flatMap(domainClientModel -> userApiRepository
                                                                            .loginAdmin(domainClientModel.getSubDomain(), loginAdminPostDto)
                                                                            .flatMap((Function<UserDto, SingleSource<UserDto>>) userDto -> serverSuccessResponseSingleValidator.validateResponse(userDto))
                                                                            .retryWhen(retryWithDelay))
                                    .flatMap((userDto) -> userDtoToDomainUserModelMapper.map(userDto))
                                    .flatMapCompletable((userModel) -> saveAccessTokenToLocalStorageUseCase.execute(userModel));
    }

    //endregion

}
