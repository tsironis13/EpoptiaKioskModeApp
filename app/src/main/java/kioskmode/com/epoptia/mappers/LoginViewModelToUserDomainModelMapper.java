package kioskmode.com.epoptia.mappers;

import javax.inject.Inject;

import domain.com.epoptia.model.domain.DomainUserModel;
import kioskmode.com.epoptia.login.model.LoginViewModel;

public class LoginViewModelToUserDomainModelMapper {

    //region Injections

    @Inject
    DomainUserModel user;

    //endregion

    //region Constructor

    @Inject
    public LoginViewModelToUserDomainModelMapper() {}

    //endregion

    //region Public Methods

    public DomainUserModel map(LoginViewModel loginViewModel) {
        user.setAccessToken(loginViewModel.getAccessToken());

        return user;
    }

    //endregion

}
