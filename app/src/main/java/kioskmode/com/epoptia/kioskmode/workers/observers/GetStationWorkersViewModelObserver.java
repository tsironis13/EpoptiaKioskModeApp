package kioskmode.com.epoptia.kioskmode.workers.observers;

import domain.com.epoptia.model.domain.DomainBaseModel;
import kioskmode.com.epoptia.kioskmode.workers.viewmodel.StationWorkersContract;
import kioskmode.com.epoptia.viewmodel.ViewModelFlowableObserver;

public class GetStationWorkersViewModelObserver extends ViewModelFlowableObserver {

    //region Private Properties

    private StationWorkersContract.ViewModel mViewModel;

    //endregion

    //region Constructor

    public GetStationWorkersViewModelObserver(StationWorkersContract.ViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    //endregion

    //region Public Methods

    @Override
    public void onNext(DomainBaseModel domainStationWorkersModels) {
        mViewModel.loadStationWorkersOnSuccess(domainStationWorkersModels.getWorkerModels());
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onError(Throwable e) {
        mViewModel.loadStationWorkersOnError(e);
    }

    //endregion

}
