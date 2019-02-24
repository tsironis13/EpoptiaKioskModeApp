package data.com.epoptia.localstorage.prefs.entities;

import com.skydoves.preferenceroom.KeyName;
import com.skydoves.preferenceroom.PreferenceEntity;
import com.skydoves.preferenceroom.PreferenceFunction;

@PreferenceEntity
public class User {

    @KeyName(name = "accessToken")
    protected final String accessToken = null;

}
