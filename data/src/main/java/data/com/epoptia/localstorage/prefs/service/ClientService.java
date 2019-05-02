package data.com.epoptia.localstorage.prefs.service;

import com.skydoves.preferenceroom.InjectPreference;

import javax.inject.Inject;

import data.com.epoptia.localstorage.prefs.components.PreferenceComponent_ClientComponent;
import data.com.epoptia.localstorage.prefs.entities.Preference_Client;
import data.com.epoptia.mappers.ClientPreferenceModelToClientDomainModelMapper;
import domain.com.epoptia.Constants;
import domain.com.epoptia.model.domain.DomainClientModel;
import io.reactivex.Completable;
import io.reactivex.Single;

public class ClientService {

    //region Injections

    @InjectPreference
    public PreferenceComponent_ClientComponent clientComponent;

    @InjectPreference
    public Preference_Client clientPref;

    @Inject
    ClientPreferenceModelToClientDomainModelMapper clientPreferenceModelToClientDomainModelMapper;

    //endregion

    //region Constructor

    @Inject
    public ClientService() {
        PreferenceComponent_ClientComponent.getInstance().inject(this);
    }

    //endregion

    //region Public Methods

    public Completable setClientSubDomain(DomainClientModel domainClientModel) {
        if (domainClientModel == null) {
            return Completable.error(new Exception(Constants.INVALID_DOMAIN_MODEL, new NullPointerException()));
        }

        return Completable.fromAction(() -> clientPref.putSubDomain(domainClientModel.getSubDomain()));
    }

    public Single<DomainClientModel> getClient() {
        return Single
                    .just(clientPref)
                    .map((clientPref) -> clientPreferenceModelToClientDomainModelMapper.map(clientPref));
    }

    //endregion

}
