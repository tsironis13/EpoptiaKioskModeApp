package data.com.epoptia.localstorage.prefs.components;

import com.skydoves.preferenceroom.PreferenceComponent;

import data.com.epoptia.localstorage.prefs.entities.Worker;
import data.com.epoptia.localstorage.prefs.service.WorkerService;

@PreferenceComponent(entities = Worker.class)
public interface WorkerComponent {
    void inject(WorkerService workerService);
}
