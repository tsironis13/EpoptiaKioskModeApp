package data.com.epoptia.localstorage.prefs.components;

import com.skydoves.preferenceroom.PreferenceComponent;

import data.com.epoptia.localstorage.prefs.entities.WorkStation;
import data.com.epoptia.localstorage.prefs.service.WorkStationService;

@PreferenceComponent(entities = WorkStation.class)
public interface WorkStationComponent {
    void inject(WorkStationService workStationService);
}
