package kioskmode.com.epoptia.login.observers;

import kioskmode.com.epoptia.login.LoginContract;
import kioskmode.com.epoptia.viewmodel.ViewModelCompletableObserver;

public class LoginAdminViewModelObserver extends ViewModelCompletableObserver {

    //region Private Properties

    private LoginContract.ViewModel mViewModel;

    //endregion

    //region Constructor

    public LoginAdminViewModelObserver(LoginContract.ViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    //endregion

    //region Public Methods


    @Override
    public void onComplete() {
        mViewModel.loginAdminOnSuccess();
    }

    @Override
    public void onError(Throwable e) {
        mViewModel.loginAdminOnError(e);
    }

    //endregion

}
