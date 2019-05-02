package domain.com.epoptia.validator;

import javax.inject.Inject;

import domain.com.epoptia.Constants;
import domain.com.epoptia.model.dto.post.LoginAdminPostDto;
import domain.com.epoptia.validator.type.SingleValidatorWithParameter;
import io.reactivex.Single;

public class LoginAdminPostDtoValidator implements SingleValidatorWithParameter<LoginAdminPostDto, LoginAdminPostDto> {

    //region Constructor

    @Inject
    public LoginAdminPostDtoValidator() {}

    //endregion

    //region Public Methods

    public Single<LoginAdminPostDto> validate(LoginAdminPostDto loginAdminPostDto) {
        if (loginAdminPostDto == null) {
            return Single.error(new Exception(Constants.INVALID_DTO, new NullPointerException()));
        }

        if (loginAdminPostDto.getClientSubDomain() == null || loginAdminPostDto.getClientSubDomain().isEmpty()) {
            return Single.error(new Exception(Constants.SUBDOMAIN_REQUIRED, new NullPointerException()));
        }

        if (loginAdminPostDto.getUsername() == null || loginAdminPostDto.getUsername().isEmpty()) {
            return Single.error(new Exception(Constants.USERNAME_PASSWORD_REQUIRED, new NullPointerException()));
        }

        if (loginAdminPostDto.getPassword() == null || loginAdminPostDto.getPassword().isEmpty()) {
            return Single.error(new Exception(Constants.USERNAME_PASSWORD_REQUIRED, new NullPointerException()));
        }

        return Single.just(loginAdminPostDto);
    }

    //endregion

}
