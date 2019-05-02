package kioskmode.com.epoptia.workstations.observers;

import domain.com.epoptia.model.domain.DomainBaseModel;
import kioskmode.com.epoptia.viewmodel.ViewModelFlowableObserver;
import kioskmode.com.epoptia.workstations.viewmodel.WorkStationsContract;

public class GetWorkStationsViewModelObserver extends ViewModelFlowableObserver {

    //region Private Properties

    private WorkStationsContract.ViewModel mViewModel;

    //endregion

    //region Constructor

    public GetWorkStationsViewModelObserver(WorkStationsContract.ViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    //endregion

    //region Public Methods

    @Override
    public void onNext(DomainBaseModel domainWorkStationModels) {
        mViewModel.loadWorkStationsOnSuccess(domainWorkStationModels.getWorkStationModels());
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onError(Throwable e) {
        mViewModel.loadWorkStationsOnError(e);
    }

    //endregion

}
