package domain.com.epoptia.interactor.client;

import javax.inject.Inject;

import domain.com.epoptia.interactor.type.SingleUseCase;
import domain.com.epoptia.model.domain.DomainClientModel;
import domain.com.epoptia.repository.localstorage.prefs.ClientRepository;
import io.reactivex.Single;

public class GetClientFromLocalStorageUseCase implements SingleUseCase<DomainClientModel> {

    //region Injections

    @Inject
    ClientRepository localStorageClientRepository;

    //endregion

    //region Constructor

    @Inject
    public GetClientFromLocalStorageUseCase() { }

    //endregion

    //region Public Methods

    @Override
    public Single<DomainClientModel> execute() {
        return localStorageClientRepository.getClient();
    }

    //endregion

}
