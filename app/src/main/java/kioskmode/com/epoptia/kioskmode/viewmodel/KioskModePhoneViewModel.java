package kioskmode.com.epoptia.kioskmode.viewmodel;

import javax.inject.Inject;

import kioskmode.com.epoptia.R;

public class KioskModePhoneViewModel extends KioskModeViewModel {

    //region Constructor

    @Inject
    public KioskModePhoneViewModel() {}

    //endregion

    //region Public Methods

    @Override
    public int getKioskModeMenuLayout() {
        return R.menu.kiosk_mode_phone_menu;
    }


    //endregion

}
