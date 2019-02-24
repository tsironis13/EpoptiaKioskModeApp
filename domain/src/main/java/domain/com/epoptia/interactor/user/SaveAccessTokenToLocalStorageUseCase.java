package domain.com.epoptia.interactor.user;

import javax.inject.Inject;

import domain.com.epoptia.interactor.type.CompletableUseCaseWithParameter;
import domain.com.epoptia.model.domain.DomainUserModel;
import domain.com.epoptia.repository.localstorage.prefs.UserRepository;
import io.reactivex.Completable;

public class SaveAccessTokenToLocalStorageUseCase implements CompletableUseCaseWithParameter<DomainUserModel> {

    //region Injections

    @Inject
    UserRepository localStorageUserRepository;

    //endregion

    //region Constructor

    @Inject
    public SaveAccessTokenToLocalStorageUseCase() { }

    //endregion

    //region Public Methods

    @Override
    public Completable execute(DomainUserModel user) {
        return localStorageUserRepository.saveAccessToken(user);
    }

    //endregion

}
