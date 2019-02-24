package kioskmode.com.epoptia.login;

import domain.com.epoptia.model.dto.result.UserDto;
import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.login.model.LoginViewModel;

public interface LoginContract {

    interface View extends Lifecycle.View {
        void deviceIsTablet();

        void deviceIsPhone();

        void setProcessing(boolean isProcessing);

        void initializeSubDomain(LoginViewModel loginViewModel);

        void validateClientSubDomainOnSuccess();

        void validateClientSubDomainOnError(Throwable throwable);

        void loginAdminOnSuccess();

        void loginAdminOnError(Throwable throwable);
    }

    interface ViewModel extends Lifecycle.ViewModel {
        void initializeFormData();

        void checkDeviceCategory();

        void submitForm(LoginViewModel loginViewModel);

        void validateClientSubDomain(LoginViewModel loginViewModel);

        void loginAdmin(LoginViewModel loginViewModel);

        void validateClientSubDomainOnSuccess();

        void validateClientSubDomainOnError(Throwable throwable);

        void loginAdminOnSuccess();

        void loginAdminOnError(Throwable throwable);
    }

}
