package data.com.epoptia.localstorage.prefs.components;

import com.skydoves.preferenceroom.PreferenceComponent;

import data.com.epoptia.localstorage.prefs.entities.User;
import data.com.epoptia.localstorage.prefs.service.UserService;

@PreferenceComponent(entities = User.class)
public interface UserComponent {
    void inject(UserService userService);
}
