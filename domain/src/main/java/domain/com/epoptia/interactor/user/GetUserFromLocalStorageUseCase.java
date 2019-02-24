package domain.com.epoptia.interactor.user;

import javax.inject.Inject;

import domain.com.epoptia.interactor.type.SingleUseCase;
import domain.com.epoptia.model.domain.DomainUserModel;
import domain.com.epoptia.repository.localstorage.prefs.UserRepository;
import io.reactivex.Single;

public class GetUserFromLocalStorageUseCase implements SingleUseCase<DomainUserModel> {

    //region Injections

    @Inject
    UserRepository localStorageUserRepository;

    //endregion

    //region Constructor

    @Inject
    public GetUserFromLocalStorageUseCase() { }

    //endregion

    //region Public Methods

    @Override
    public Single<DomainUserModel> execute() {
        return localStorageUserRepository.getUser();
    }

    //endregion

}
