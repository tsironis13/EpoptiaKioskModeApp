package kioskmode.com.epoptia.login.observers;

import android.util.Log;
import kioskmode.com.epoptia.login.LoginContract;
import kioskmode.com.epoptia.viewmodel.ViewModelCompletableObserver;

public class ValidateClientSubDomainViewModelObserver extends ViewModelCompletableObserver {

    //region Injections

    //endregion

    //region Private Properties

    //todo remove
    private static final String debugTag = ValidateClientSubDomainViewModelObserver.class.getSimpleName();

    private LoginContract.ViewModel mViewModel;

    //endregion

    //region Constructor

    public ValidateClientSubDomainViewModelObserver(LoginContract.ViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    //endregion

    //region Public Methods

    @Override
    public void onComplete() {
        mViewModel.validateClientSubDomainOnSuccess();
    }

    @Override
    public void onError(Throwable e) {
        Log.e(debugTag, "ERROR => " + e.toString());

        mViewModel.validateClientSubDomainOnError(e);
    }

    //endregion

}
