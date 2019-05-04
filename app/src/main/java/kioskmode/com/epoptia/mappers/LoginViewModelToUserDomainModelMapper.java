package kioskmode.com.epoptia.mappers;

import javax.inject.Inject;

import domain.com.epoptia.model.domain.DomainUserModel;
import io.reactivex.Single;
import kioskmode.com.epoptia.viewmodel.models.LoginViewModel;

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

    public Single<DomainUserModel> map(LoginViewModel loginViewModel) {
        user.setAccessToken(loginViewModel.getAccessToken());

        return Single.just(user);
    }

    //endregion

}
