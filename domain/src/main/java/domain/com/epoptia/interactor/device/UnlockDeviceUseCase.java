package domain.com.epoptia.interactor.device;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import domain.com.epoptia.device.network.NetworkUtility;
import domain.com.epoptia.interactor.client.GetClientFromLocalStorageUseCase;
import domain.com.epoptia.interactor.type.CompletableUseCaseWithParameter;
import domain.com.epoptia.interactor.user.SaveAccessTokenToLocalStorageUseCase;
import domain.com.epoptia.mappers.UserDtoToDomainUserModelMapper;
import domain.com.epoptia.model.dto.post.UnlockDevicePostDto;
import domain.com.epoptia.model.dto.result.BaseResponseDto;
import domain.com.epoptia.model.dto.result.UserDto;
import domain.com.epoptia.repository.api.DeviceRepository;
import domain.com.epoptia.repository.localstorage.prefs.UserRepository;
import domain.com.epoptia.utilities.error.RetryWithDelay;
import domain.com.epoptia.utilities.error.ServerSuccessResponseSingleValidatorImpl;
import domain.com.epoptia.validator.UnlockDevicePostDtoValidator;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class UnlockDeviceUseCase implements CompletableUseCaseWithParameter<UnlockDevicePostDto> {

    //region Injections

    @Inject
    NetworkUtility networkUtility;

    @Inject
    UnlockDevicePostDtoValidator unlockDevicePostDtoValidator;

    @Inject
    GetClientFromLocalStorageUseCase getClientFromLocalStorageUseCase;

    @Inject
    UserRepository userLocalStorageRepository;

    @Inject
    DeviceRepository deviceApiRepository;

    @Inject
    ServerSuccessResponseSingleValidatorImpl serverSuccessResponseSingleValidator;

    @Inject
    RetryWithDelay retryWithDelay;

    //endregion

    //region Constructor

    @Inject
    public UnlockDeviceUseCase() {}

    //endregion

    //region Public Methods

    @Override
    public Completable execute(UnlockDevicePostDto unlockDevicePostDto) {
        return Single.zip(
                        unlockDevicePostDtoValidator.validate(unlockDevicePostDto),
                        userLocalStorageRepository.getUser(), (unlockDvcPostDto, user) -> {
                            unlockDevicePostDto.setAccessToken(user.getAccessToken());

                            return unlockDevicePostDto;
                        })
                        .delay(2000, TimeUnit.MILLISECONDS)
                        .flatMap(unlockDvcPostDto -> networkUtility.isNetworkAvailable().toSingleDefault(unlockDvcPostDto))
                        .flatMap(unlockDvcPostDto -> getClientFromLocalStorageUseCase.execute())
                        .flatMap(domainClientModel -> deviceApiRepository
                                                                .unlockDevice(domainClientModel.getSubDomain(), unlockDevicePostDto)
                                                                .flatMap((Function<BaseResponseDto, SingleSource<BaseResponseDto>>) baseResponseDto -> serverSuccessResponseSingleValidator.validateResponse(baseResponseDto))
                                                                .retryWhen(retryWithDelay))
                        .ignoreElement();
    }

    //endregion

}
