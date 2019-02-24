package data.com.epoptia.localstorage.prefs.service;

import com.skydoves.preferenceroom.InjectPreference;
import javax.inject.Inject;
import data.com.epoptia.localstorage.prefs.components.PreferenceComponent_UserComponent;
import data.com.epoptia.localstorage.prefs.entities.Preference_User;
import data.com.epoptia.mappers.UserPreferenceModelToUserDomainModelMapper;
import domain.com.epoptia.model.domain.DomainUserModel;
import io.reactivex.Completable;
import io.reactivex.Single;

public class UserService {

    //region Injections

    @InjectPreference
    public PreferenceComponent_UserComponent userComponent;

    @InjectPreference
    public Preference_User userPref;

    @Inject
    UserPreferenceModelToUserDomainModelMapper userPreferenceModelToUserDomainModelMapper;

    //endregion

    //region Constructor

    @Inject
    public UserService() {
        PreferenceComponent_UserComponent.getInstance().inject(this);
    }

    //endregion

    //region Public Methods

    public Completable saveAccessToken(DomainUserModel user) {
        if (user == null) {
            //todo change error ??
            return Completable.error(new Exception("User is null"));
        }

        return Completable.fromAction(() -> userPref.putAccessToken(user.getAccessToken()));
    }

    public Single<DomainUserModel> getUser() {
        return Single
                    .just(userPref)
                    .map((u) -> {
                        return userPreferenceModelToUserDomainModelMapper.map(u);
                    });
    }

    //endregion

}
