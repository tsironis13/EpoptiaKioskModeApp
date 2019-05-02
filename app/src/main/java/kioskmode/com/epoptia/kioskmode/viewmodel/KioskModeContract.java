package kioskmode.com.epoptia.kioskmode.viewmodel;

import android.view.Menu;

import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.viewmodel.models.AdminDetailsViewModel;

public interface KioskModeContract {

    interface View extends Lifecycle.View {
        void deviceUnlocked();
    }

    interface ViewModel extends Lifecycle.ViewModel {
        int getKioskModeMenuLayout();

        void unlockDeviceFromAppLogo();

        void removeStatusBarOverlayCustomView();

        void unlockDevice(AdminDetailsViewModel adminDetailsViewModel);

        void unlockDeviceOnSuccess();

        void unlockDeviceOnError(Throwable t);
    }

}
