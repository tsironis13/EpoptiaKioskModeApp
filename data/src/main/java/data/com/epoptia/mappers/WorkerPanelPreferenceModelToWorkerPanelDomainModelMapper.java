package data.com.epoptia.mappers;

import javax.inject.Inject;

import data.com.epoptia.localstorage.prefs.entities.Preference_WorkerPanel;
import domain.com.epoptia.model.domain.DomainWorkerPanelModel;

public class WorkerPanelPreferenceModelToWorkerPanelDomainModelMapper {

    //region Injections

    @Inject
    DomainWorkerPanelModel domainWorkerPanelModel;

    //endregion

    //region Constructor

    @Inject
    public WorkerPanelPreferenceModelToWorkerPanelDomainModelMapper() {}

    //endregion

    //region Public Methods

    public DomainWorkerPanelModel map(Preference_WorkerPanel workerPanelPref) {
        domainWorkerPanelModel.setCookie(workerPanelPref.getCookie());
        domainWorkerPanelModel.setUrl(workerPanelPref.getUrl());

        return domainWorkerPanelModel;
    }

    //endregion

}
