package kioskmode.com.epoptia.kioskmode.observers;

import kioskmode.com.epoptia.kioskmode.viewmodel.KioskModeContract;
import kioskmode.com.epoptia.viewmodel.ViewModelCompletableObserver;

public class UnlockDeviceViewModelObserver extends ViewModelCompletableObserver {

    //region Private Properties

    private KioskModeContract.ViewModel mViewModel;

    //endregion

    //region Constructor

    public UnlockDeviceViewModelObserver(KioskModeContract.ViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    //endregion

    //region Public Methods


    @Override
    public void onComplete() {
        mViewModel.unlockDeviceOnSuccess();
    }

    @Override
    public void onError(Throwable e) {
        mViewModel.unlockDeviceOnError(e);
    }

    //endregion

}
