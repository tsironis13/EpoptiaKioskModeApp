package domain.com.epoptia.repository.localstorage.prefs;

import domain.com.epoptia.model.domain.DomainWorkStationModel;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface WorkStationRepository {

    Completable setWorkStationId(DomainWorkStationModel workStation);

    Completable setWorkStationName(DomainWorkStationModel workStation);

    Completable clearWorkStation();

    Single<DomainWorkStationModel> getWorkStation();

}
