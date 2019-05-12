package kioskmode.com.epoptia.kioskmode.workers.viewmodel;

import java.util.List;

import domain.com.epoptia.model.domain.DomainWorkerModel;
import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.viewmodel.models.WorkStationViewModel;
import kioskmode.com.epoptia.viewmodel.models.WorkerViewModel;

public interface StationWorkersContract {

    interface View extends Lifecycle.View {
        void setProcessing(boolean isProcessing);

        void loadStationWorkersOnSuccess(List<WorkerViewModel> workers);

        void loadStationWorkersOnError(Throwable throwable);

        void onStationWorkerItemClick(android.view.View view);
    }

    interface ViewModel extends Lifecycle.ViewModel {
        void loadStationWorkersOnSuccess(List<DomainWorkerModel> domainWorkStations);

        void loadStationWorkersOnError(Throwable throwable);

        void loadStationWorkers(WorkStationViewModel workStationViewModel);
    }

    interface ItemClickListener {
        void onItemClick(android.view.View view);
    }

}
