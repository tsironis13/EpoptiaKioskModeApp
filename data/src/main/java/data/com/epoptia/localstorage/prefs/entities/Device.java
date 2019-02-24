package data.com.epoptia.localstorage.prefs.entities;

import com.skydoves.preferenceroom.KeyName;
import com.skydoves.preferenceroom.PreferenceEntity;

@PreferenceEntity
public class Device {

    @KeyName(name = "modeState")
    protected final int modeState = 15;//DEFAULT_MODE_STATE

    @KeyName(name = "category")
    protected final int category = 1;//DEFAULT PHONE

}
