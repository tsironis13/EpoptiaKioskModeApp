package data.com.epoptia.localstorage.prefs.service;

import com.skydoves.preferenceroom.InjectPreference;

import javax.inject.Inject;

import data.com.epoptia.localstorage.prefs.components.PreferenceComponent_WorkerPanelComponent;
import data.com.epoptia.localstorage.prefs.entities.Preference_WorkerPanel;
import data.com.epoptia.mappers.WorkerPanelPreferenceModelToWorkerPanelDomainModelMapper;
import domain.com.epoptia.Constants;
import domain.com.epoptia.model.domain.DomainWorkerPanelModel;
import io.reactivex.Completable;
import io.reactivex.Single;

public class WorkerPanelService {

    //region Injections

    @InjectPreference
    public PreferenceComponent_WorkerPanelComponent workerPanelComponent;

    @InjectPreference
    public Preference_WorkerPanel workerPanelPref;

    @Inject
    WorkerPanelPreferenceModelToWorkerPanelDomainModelMapper workerPanelPreferenceModelToWorkerPanelDomainModelMapper;

    //endregion

    //region Constructor

    @Inject
    public WorkerPanelService() {
        PreferenceComponent_WorkerPanelComponent.getInstance().inject(this);
    }

    //endregion

    //region Public Methods

    public Completable setWorkerPanelCookie(DomainWorkerPanelModel domainWorkerPanelModel) {
        if (domainWorkerPanelModel == null) {
            return Completable.error(new Exception(Constants.INVALID_DOMAIN_MODEL, new NullPointerException()));
        }

        return Completable.fromAction(() -> workerPanelPref.putCookie(domainWorkerPanelModel.getCookie()));
    }

    public Completable setWorkerPanelUrl(DomainWorkerPanelModel domainWorkerPanelModel) {
        if (domainWorkerPanelModel == null) {
            return Completable.error(new Exception(Constants.INVALID_DOMAIN_MODEL, new NullPointerException()));
        }

        return Completable.fromAction(() -> workerPanelPref.putUrl(domainWorkerPanelModel.getUrl()));
    }

    public Completable clearWorkerPanel() {
        return Completable.fromAction(() -> workerPanelPref.clear());
    }

    public Single<DomainWorkerPanelModel> getWorkerPanel() {
        return Single
                .just(workerPanelPref)
                .map((workerPanelPref) -> workerPanelPreferenceModelToWorkerPanelDomainModelMapper.map(workerPanelPref));
    }

    //endregion

}
