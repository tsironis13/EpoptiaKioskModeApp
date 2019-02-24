package data.com.epoptia.localstorage.prefs.components;

import com.skydoves.preferenceroom.PreferenceComponent;

import data.com.epoptia.localstorage.prefs.entities.Device;
import data.com.epoptia.localstorage.prefs.service.DeviceService;

@PreferenceComponent(entities = Device.class)
public interface DeviceComponent {
    void inject(DeviceService deviceService);
}
