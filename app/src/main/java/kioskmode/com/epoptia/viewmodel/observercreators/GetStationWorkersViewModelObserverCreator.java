package kioskmode.com.epoptia.viewmodel.observercreators;

import kioskmode.com.epoptia.kioskmode.workers.observers.GetStationWorkersViewModelObserver;
import kioskmode.com.epoptia.kioskmode.workers.viewmodel.StationWorkersContract;
import kioskmode.com.epoptia.viewmodel.ViewModelCompletableObserver;
import kioskmode.com.epoptia.viewmodel.ViewModelFlowableObserver;
import kioskmode.com.epoptia.viewmodel.ViewModelObserverCreator;
import kioskmode.com.epoptia.viewmodel.ViewModelSingleObserver;

public class GetStationWorkersViewModelObserverCreator extends ViewModelObserverCreator {

    //region Private Methods

    private StationWorkersContract.ViewModel mViewModel;

    //endregion

    //region Constructor

    public GetStationWorkersViewModelObserverCreator(StationWorkersContract.ViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    //endregion

    //region Public Methods

    @Override
    public ViewModelCompletableObserver createCompletableObserver() {
        return null;
    }

    @Override
    public ViewModelSingleObserver createSingleObserver() {
        return null;
    }

    @Override
    public ViewModelFlowableObserver createFlowableObserver() {
        return new GetStationWorkersViewModelObserver(mViewModel);
    }

    //endregion

}
