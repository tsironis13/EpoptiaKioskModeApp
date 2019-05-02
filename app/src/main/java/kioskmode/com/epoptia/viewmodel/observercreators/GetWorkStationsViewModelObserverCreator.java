package kioskmode.com.epoptia.viewmodel.observercreators;

import kioskmode.com.epoptia.viewmodel.ViewModelCompletableObserver;
import kioskmode.com.epoptia.viewmodel.ViewModelFlowableObserver;
import kioskmode.com.epoptia.viewmodel.ViewModelObserverCreator;
import kioskmode.com.epoptia.viewmodel.ViewModelSingleObserver;
import kioskmode.com.epoptia.workstations.observers.GetWorkStationsViewModelObserver;
import kioskmode.com.epoptia.workstations.viewmodel.WorkStationsContract;

public class GetWorkStationsViewModelObserverCreator extends ViewModelObserverCreator {

    //region Private Methods

    private WorkStationsContract.ViewModel mViewModel;

    //endregion

    //region Constructor

    public GetWorkStationsViewModelObserverCreator(WorkStationsContract.ViewModel viewModel) {
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
        return new GetWorkStationsViewModelObserver(mViewModel);
    }

    //endregion

}
