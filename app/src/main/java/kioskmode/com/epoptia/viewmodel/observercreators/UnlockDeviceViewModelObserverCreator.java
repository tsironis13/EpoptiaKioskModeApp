package kioskmode.com.epoptia.viewmodel.observercreators;

import kioskmode.com.epoptia.kioskmode.observers.UnlockDeviceViewModelObserver;
import kioskmode.com.epoptia.kioskmode.viewmodel.KioskModeContract;
import kioskmode.com.epoptia.viewmodel.ViewModelCompletableObserver;
import kioskmode.com.epoptia.viewmodel.ViewModelFlowableObserver;
import kioskmode.com.epoptia.viewmodel.ViewModelObserverCreator;
import kioskmode.com.epoptia.viewmodel.ViewModelSingleObserver;

public class UnlockDeviceViewModelObserverCreator extends ViewModelObserverCreator {

    //region Private Methods

    private KioskModeContract.ViewModel mViewModel;

    //endregion

    //region Constructor

    public UnlockDeviceViewModelObserverCreator(KioskModeContract.ViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    //endregion

    //region Public Methods

    @Override
    public ViewModelCompletableObserver createCompletableObserver() {
        return new UnlockDeviceViewModelObserver(mViewModel);
    }

    @Override
    public ViewModelSingleObserver createSingleObserver() {
        return null;
    }

    @Override
    public ViewModelFlowableObserver createFlowableObserver() {
        return null;
    }

    //endregion

}
