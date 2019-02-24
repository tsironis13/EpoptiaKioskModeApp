package domain.com.epoptia.repository.localstorage.prefs;

import domain.com.epoptia.model.domain.DomainUserModel;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface UserRepository {

    Completable saveAccessToken(DomainUserModel user);

    Single<DomainUserModel> getUser();

}
