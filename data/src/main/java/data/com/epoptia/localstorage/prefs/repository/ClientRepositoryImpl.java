package data.com.epoptia.localstorage.prefs.repository;

import javax.inject.Inject;

import data.com.epoptia.localstorage.prefs.service.ClientService;
import domain.com.epoptia.Constants;
import domain.com.epoptia.model.domain.DomainClientModel;
import domain.com.epoptia.repository.localstorage.prefs.ClientRepository;
import io.reactivex.Completable;
import io.reactivex.Single;

public class ClientRepositoryImpl implements ClientRepository {

    //region Injections

    @Inject
    ClientService clientService;

    //endregion

    //region Constructor

    @Inject
    public ClientRepositoryImpl() { }

    //endregion

    //region Public Methods

    @Override
    public Completable setClientSubDomain(DomainClientModel client) {
        return clientService.setClientSubDomain(client);
    }

    @Override
    public Single<DomainClientModel> getClient() {
        return clientService.getClient();
    }

    //endregion

}
