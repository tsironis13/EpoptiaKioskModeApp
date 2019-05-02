package data.com.epoptia.mappers;

import javax.inject.Inject;

import data.com.epoptia.localstorage.prefs.entities.Preference_User;
import domain.com.epoptia.model.domain.DomainUserModel;

public class UserPreferenceModelToUserDomainModelMapper {

    //region Injections

    @Inject
    DomainUserModel domainUserModel;

    //endregion

    //region Constructor

    @Inject
    public UserPreferenceModelToUserDomainModelMapper() {}

    //endregion

    //region Public Methods

    public DomainUserModel map(Preference_User userPref) {
        domainUserModel.setAccessToken(userPref.getAccessToken());

        return domainUserModel;
    }

    //endregion

}
