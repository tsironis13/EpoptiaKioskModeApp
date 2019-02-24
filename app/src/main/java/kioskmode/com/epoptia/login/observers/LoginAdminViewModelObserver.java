package kioskmode.com.epoptia.login.observers;

import android.util.Log;

import kioskmode.com.epoptia.login.LoginContract;
import kioskmode.com.epoptia.viewmodel.ViewModelCompletableObserver;

public class LoginAdminViewModelObserver extends ViewModelCompletableObserver {

    //region Injections

    //endregion

    //region Private Properties

    //todo remove
    private static final String debugTag = LoginAdminViewModelObserver.class.getSimpleName();

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
        Log.e(debugTag, "ERROR => " + e.toString());
        Throwable t1 = e;

        mViewModel.loginAdminOnError(e);
    }

    //endregion

}
