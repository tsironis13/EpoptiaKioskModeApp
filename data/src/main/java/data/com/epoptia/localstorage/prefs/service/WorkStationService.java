package data.com.epoptia.localstorage.prefs.service;

import com.skydoves.preferenceroom.InjectPreference;

import javax.inject.Inject;

import data.com.epoptia.localstorage.prefs.components.PreferenceComponent_WorkStationComponent;
import data.com.epoptia.localstorage.prefs.entities.Preference_WorkStation;
import data.com.epoptia.mappers.WorkStationPreferenceModelToWorkStationDomainModelMapper;
import domain.com.epoptia.Constants;
import domain.com.epoptia.model.domain.DomainWorkStationModel;
import io.reactivex.Completable;
import io.reactivex.Single;

public class WorkStationService {

    //region Injections

    @InjectPreference
    public PreferenceComponent_WorkStationComponent workStationComponent;

    @InjectPreference
    public Preference_WorkStation workStationPref;

    @Inject
    WorkStationPreferenceModelToWorkStationDomainModelMapper workStationPreferenceModelToWorkStationDomainModelMapper;

    //endregion

    //region Constructor

    @Inject
    public WorkStationService() {
        PreferenceComponent_WorkStationComponent.getInstance().inject(this);
    }

    //endregion

    //region Public Methods

    public Completable setWorkStationId(DomainWorkStationModel domainWorkStationModel) {
        if (domainWorkStationModel == null) {
            return Completable.error(new Exception(Constants.INVALID_DOMAIN_MODEL, new NullPointerException()));
        }

        return Completable.fromAction(() -> workStationPref.putId(domainWorkStationModel.getId()));
    }

    public Completable setWorkStationName(DomainWorkStationModel domainWorkStationModel) {
        if (domainWorkStationModel == null) {
            return Completable.error(new Exception(Constants.INVALID_DOMAIN_MODEL, new NullPointerException()));
        }

        return Completable.fromAction(() -> workStationPref.putName(domainWorkStationModel.getName()));
    }

    public Completable clearWorkStation() {
        return Completable.fromAction(() -> workStationPref.clear());
    }

    public Single<DomainWorkStationModel> getWorkStation() {
        return Single
                .just(workStationPref)
                .map((workStationPref) -> workStationPreferenceModelToWorkStationDomainModelMapper.map(workStationPref));
    }

    //endregion

}
