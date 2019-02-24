package domain.com.epoptia.interactor.client;

import javax.inject.Inject;

import domain.com.epoptia.interactor.type.CompletableUseCaseWithParameter;
import domain.com.epoptia.model.domain.DomainClientModel;
import domain.com.epoptia.repository.localstorage.prefs.ClientRepository;
import io.reactivex.Completable;

public class SaveClientSubDomainToLocalStorageUseCase implements CompletableUseCaseWithParameter<DomainClientModel> {

    //region Injections

    @Inject
    ClientRepository localStorageClientRepository;

    //endregion

    //region Constructor

    @Inject
    public SaveClientSubDomainToLocalStorageUseCase() {}

    //endregion

    //region Public Methods

    @Override
    public Completable execute(DomainClientModel client) {
        return localStorageClientRepository.saveClientSubDomain(client);
    }

    //endregion

}
