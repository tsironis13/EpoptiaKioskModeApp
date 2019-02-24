package data.com.epoptia.localstorage.prefs.components;

import com.skydoves.preferenceroom.PreferenceComponent;

import data.com.epoptia.localstorage.prefs.entities.Client;
import data.com.epoptia.localstorage.prefs.service.ClientService;

@PreferenceComponent(entities = Client.class)
public interface ClientComponent {
    void inject(ClientService clientService);
}
