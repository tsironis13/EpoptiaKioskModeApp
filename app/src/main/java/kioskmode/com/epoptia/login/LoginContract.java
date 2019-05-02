package kioskmode.com.epoptia.login;

import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.viewmodel.models.LoginViewModel;

public interface LoginContract {

    interface View extends Lifecycle.View {
        void setProcessing(boolean isProcessing);

        void bindLoginViewModelToViewModel(LoginViewModel loginViewModel);

        void validateClientSubDomainOnSuccess();

        void validateClientSubDomainOnError(Throwable throwable);

        void loginAdminOnSuccess();

        void loginAdminOnError(Throwable throwable);
    }

    interface ViewModel extends Lifecycle.ViewModel {
        void initializeFormData();

        void submitForm(LoginViewModel loginViewModel);

        void validateClientSubDomain(LoginViewModel loginViewModel);

        void loginAdmin(LoginViewModel loginViewModel);

        void validateClientSubDomainOnSuccess();

        void validateClientSubDomainOnError(Throwable throwable);

        void loginAdminOnSuccess();

        void loginAdminOnError(Throwable throwable);
    }

}
