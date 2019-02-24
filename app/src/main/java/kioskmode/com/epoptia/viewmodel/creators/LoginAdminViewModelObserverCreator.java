package kioskmode.com.epoptia.viewmodel.creators;

import kioskmode.com.epoptia.login.LoginContract;
import kioskmode.com.epoptia.login.observers.LoginAdminViewModelObserver;
import kioskmode.com.epoptia.viewmodel.ViewModelCompletableObserver;
import kioskmode.com.epoptia.viewmodel.ViewModelObserverCreator;
import kioskmode.com.epoptia.viewmodel.ViewModelSingleObserver;

public class LoginAdminViewModelObserverCreator extends ViewModelObserverCreator {

    //region Private Methods

    private LoginContract.ViewModel mViewModel;

    //endregion

    //region Constructor

    public LoginAdminViewModelObserverCreator(LoginContract.ViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    //endregion

    //region Public Methods

    public ViewModelCompletableObserver createCompletableObserver() {
        return new LoginAdminViewModelObserver(mViewModel);
    }

    @Override
    public ViewModelSingleObserver createSingleObserver() {
        return null;
    }

    //endregion

}
