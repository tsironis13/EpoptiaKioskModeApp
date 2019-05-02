package data.com.epoptia.mappers;

import javax.inject.Inject;

import data.com.epoptia.localstorage.prefs.entities.Preference_Client;
import domain.com.epoptia.model.domain.DomainClientModel;

public class ClientPreferenceModelToClientDomainModelMapper {

    //region Injections

    @Inject
    DomainClientModel domainClientModel;

    //endregion

    //region Constructor

    @Inject
    public ClientPreferenceModelToClientDomainModelMapper() {}

    //endregion

    //region Public Methods

    public DomainClientModel map(Preference_Client clientPref) {
        domainClientModel.setSubDomain(clientPref.getSubDomain());

        return domainClientModel;
    }

    //endregion

}
