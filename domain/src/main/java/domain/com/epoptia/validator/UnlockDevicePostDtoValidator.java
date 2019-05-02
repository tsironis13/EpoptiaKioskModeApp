package domain.com.epoptia.validator;

import javax.inject.Inject;

import domain.com.epoptia.Constants;
import domain.com.epoptia.model.dto.post.UnlockDevicePostDto;
import domain.com.epoptia.validator.type.SingleValidatorWithParameter;
import io.reactivex.Single;

public class UnlockDevicePostDtoValidator implements SingleValidatorWithParameter<UnlockDevicePostDto, UnlockDevicePostDto> {

    //region Constructor

    @Inject
    public UnlockDevicePostDtoValidator() {}

    //endregion

    //region Public Methods

    public Single<UnlockDevicePostDto> validate(UnlockDevicePostDto unlockDevicePostDto) {
        if (unlockDevicePostDto == null) {
            return Single.error(new Exception(Constants.INVALID_DTO, new NullPointerException()));
        }

        if (unlockDevicePostDto.getUsername() == null || unlockDevicePostDto.getUsername().isEmpty()) {
            return Single.error(new Exception(Constants.USERNAME_PASSWORD_REQUIRED, new NullPointerException()));
        }

        if (unlockDevicePostDto.getPassword() == null || unlockDevicePostDto.getPassword().isEmpty()) {
            return Single.error(new Exception(Constants.USERNAME_PASSWORD_REQUIRED, new NullPointerException()));
        }

        return Single.just(unlockDevicePostDto);
    }

    //endregion

}
