package domain.com.epoptia.repository.localstorage.prefs;

import domain.com.epoptia.model.domain.DomainClientModel;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface ClientRepository {

    Completable saveClientSubDomain(DomainClientModel client);

    Single<DomainClientModel> getClient();

}
