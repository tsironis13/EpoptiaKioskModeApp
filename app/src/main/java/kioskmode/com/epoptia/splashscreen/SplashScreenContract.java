package kioskmode.com.epoptia.splashscreen;

import kioskmode.com.epoptia.lifecycle.Lifecycle;

public interface SplashScreenContract {

    interface View extends Lifecycle.View {
        void navigateUserToLoginScreen();

        void navigateUserToWorkStationsScreen();

        void navigateUserToKioskModeScreen();

        void onSaveDeviceCategorySuccess();

        void onError();
    }

    interface ViewModel extends Lifecycle.ViewModel {
        void saveDeviceCategory();

        void checkUserIsAuthenticated();
    }

}
