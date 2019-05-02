package data.com.epoptia.localstorage.prefs.entities;

import com.skydoves.preferenceroom.KeyName;
import com.skydoves.preferenceroom.PreferenceEntity;

@PreferenceEntity
public class Worker {

    @KeyName(name = "id")
    protected final int id = 0;

    @KeyName(name = "name")
    protected final String name = null;

}
