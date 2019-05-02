package data.com.epoptia.localstorage.prefs.service;

import com.skydoves.preferenceroom.InjectPreference;

import javax.inject.Inject;

import data.com.epoptia.localstorage.prefs.components.PreferenceComponent_WorkerComponent;
import data.com.epoptia.localstorage.prefs.entities.Preference_Worker;
import data.com.epoptia.mappers.WorkerPreferenceModelToWorkerDomainModelMapper;
import domain.com.epoptia.Constants;
import domain.com.epoptia.model.domain.DomainWorkerModel;
import io.reactivex.Completable;
import io.reactivex.Single;

public class WorkerService {

    //region Injections

    @InjectPreference
    public PreferenceComponent_WorkerComponent workerComponent;

    @InjectPreference
    public Preference_Worker workerPref;

    @Inject
    WorkerPreferenceModelToWorkerDomainModelMapper workerPreferenceModelToWorkerDomainModelMapper;

    //endregion

    //region Constructor

    @Inject
    public WorkerService() {
        PreferenceComponent_WorkerComponent.getInstance().inject(this);
    }

    //endregion

    //region Public Methods

    public Completable setWorkerId(DomainWorkerModel domainWorkerModel) {
        if (domainWorkerModel == null) {
            return Completable.error(new Exception(Constants.INVALID_DOMAIN_MODEL, new NullPointerException()));
        }

        return Completable.fromAction(() -> workerPref.putId(domainWorkerModel.getId()));
    }

    public Completable setWorkerName(DomainWorkerModel domainWorkerModel) {
        if (domainWorkerModel == null) {
            return Completable.error(new Exception(Constants.INVALID_DOMAIN_MODEL, new NullPointerException()));
        }

        return Completable.fromAction(() -> workerPref.putName(domainWorkerModel.getName()));
    }

    public Completable clearWorker() {
        return Completable.fromAction(() -> workerPref.clear());
    }

    public Single<DomainWorkerModel> getWorker() {
        return Single
                .just(workerPref)
                .map((workerPref) -> workerPreferenceModelToWorkerDomainModelMapper.map(workerPref));
    }

    //endregion

}
