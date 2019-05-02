package data.com.epoptia.localstorage.prefs.components;

import com.skydoves.preferenceroom.PreferenceComponent;

import data.com.epoptia.localstorage.prefs.entities.WorkerPanel;
import data.com.epoptia.localstorage.prefs.service.WorkerPanelService;

@PreferenceComponent(entities = WorkerPanel.class)
public interface WorkerPanelComponent {
    void inject(WorkerPanelService systemDashboardService);
}
