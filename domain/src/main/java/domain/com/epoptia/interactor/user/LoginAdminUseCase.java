package domain.com.epoptia.interactor.user;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import domain.com.epoptia.device.Network;
import domain.com.epoptia.interactor.client.GetClientFromLocalStorageUseCase;
import domain.com.epoptia.interactor.type.CompletableUseCaseWithParameter;
import domain.com.epoptia.mappers.UserDtoToDomainUserModelMapper;
import domain.com.epoptia.mappers.UserDtoToResponseWrapperDtoMapper;
import domain.com.epoptia.model.dto.post.LoginAdminPostDto;
import domain.com.epoptia.model.dto.result.UserDto;
import domain.com.epoptia.repository.api.UserRepository;
import domain.com.epoptia.utilities.error.RetryWithDelay;
import domain.com.epoptia.utilities.error.ServerSuccessResponseValidator;
import domain.com.epoptia.validator.LoginAdminPostDtoValidator;
import io.reactivex.Completable;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class LoginAdminUseCase implements CompletableUseCaseWithParameter<LoginAdminPostDto> {

    //region Injections

    @Inject
    Network network;

    @Inject
    LoginAdminPostDtoValidator loginAdminPostDtoValidator;

    @Inject
    GetClientFromLocalStorageUseCase getClientFromLocalStorageUseCase;

    @Inject
    UserRepository userApiRepository;

    @Inject
    ServerSuccessResponseValidator serverSuccessResponseValidator;

    @Inject
    RetryWithDelay retryWithDelay;

    @Inject
    UserDtoToResponseWrapperDtoMapper userDtoToResponseWrapperDtoMapper;

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
                                    .flatMap(dto -> network.isNetworkAvailable().toSingleDefault(dto))
                                    .delay(4000, TimeUnit.MILLISECONDS)
                                    .flatMap(dto -> getClientFromLocalStorageUseCase.execute())
                                    .flatMap(domainClientModel -> userApiRepository
                                                                            .loginAdmin(domainClientModel.getSubDomain(), loginAdminPostDto)
                                                                            .flatMap((Function<UserDto, SingleSource<UserDto>>) userDto -> serverSuccessResponseValidator.validateSingleResponse(userDto))
                                                                            .retryWhen(retryWithDelay))
                                    .map((userDto) -> userDtoToDomainUserModelMapper.map(userDto))
                                    .flatMapCompletable((userModel) -> {

                                        return saveAccessTokenToLocalStorageUseCase.execute(userModel);
                                    });
    }

    //endregion

}
