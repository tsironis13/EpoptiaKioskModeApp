package domain.com.epoptia.validator;

import javax.inject.Inject;

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
            return Single.error(new Exception("An error occurred.", new NullPointerException()));
        }

        if (loginAdminPostDto.getClientSubDomain() == null || loginAdminPostDto.getClientSubDomain().isEmpty()) {
            return Single.error(new Exception("Please provide a client sub domain."));
        }

        if (loginAdminPostDto.getUsername() == null || loginAdminPostDto.getUsername().isEmpty()) {
            return Single.error(new Exception("Please provide a username."));
        }

        if (loginAdminPostDto.getPassword() == null || loginAdminPostDto.getPassword().isEmpty()) {
            return Single.error(new Exception("Please provide a password."));
        }

        return Single.just(loginAdminPostDto);
    }

    //endregion

}
