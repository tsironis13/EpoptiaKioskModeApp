package kioskmode.com.epoptia.splashscreen;

import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.viewmodel.models.DeviceViewModel;

public interface SplashScreenContract {

    interface View extends Lifecycle.View {
        void navigateUserToLoginScreen(DeviceViewModel deviceViewModel);

        void navigateUserToWorkStationsScreen(DeviceViewModel deviceViewModel);

        void navigateUserToKioskModeScreen(DeviceViewModel deviceViewModel);

        void onSaveDeviceCategorySuccess();

        void onError();
    }

    interface ViewModel extends Lifecycle.ViewModel {
        void saveDeviceCategory();

        void checkUserAndDeviceState();
    }

}
