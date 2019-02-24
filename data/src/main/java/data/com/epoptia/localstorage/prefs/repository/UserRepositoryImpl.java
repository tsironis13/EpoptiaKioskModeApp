package data.com.epoptia.localstorage.prefs.repository;

import javax.inject.Inject;

import data.com.epoptia.localstorage.prefs.service.UserService;
import domain.com.epoptia.model.domain.DomainUserModel;
import domain.com.epoptia.repository.localstorage.prefs.UserRepository;
import io.reactivex.Completable;
import io.reactivex.Single;

public class UserRepositoryImpl implements UserRepository {

    //region Injections

    @Inject
    UserService userService;

    //endregion

    //region Constructor

    @Inject
    public UserRepositoryImpl() { }

    //endregion

    //region Public Methods

    @Override
    public Completable saveAccessToken(DomainUserModel user) {
        return userService.saveAccessToken(user);
    }

    @Override
    public Single<DomainUserModel> getUser() {
        return userService.getUser();
    }

    //endregion

}
