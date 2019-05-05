package kioskmode.com.epoptia.workstations.viewmodel;

import java.util.List;

import domain.com.epoptia.model.domain.DomainWorkStationModel;
import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.viewmodel.models.NetworkStateViewModel;
import kioskmode.com.epoptia.viewmodel.models.WorkStationViewModel;

public interface WorkStationsContract {

    interface View extends Lifecycle.View {
        void setProcessing(boolean isProcessing);

        void loadWorkStationsOnSuccess(List<DomainWorkStationModel> domainWorkStations);

        void loadWorkStationsOnError(Throwable throwable);

        void onWorkStationItemClick(android.view.View view);

        void enableLockDeviceDialog();

        void setNetworkState(NetworkStateViewModel networkStateViewModel);

        void startKioskModeActivity();

        void startKioskModeActivityWhenAppIsNotTheDefaultLauncher();
    }

    interface ViewModel extends Lifecycle.ViewModel {
        void startNetworkSpeedTestService();

        void stopNetworkSpeedTestService();

        void saveWorkStation(WorkStationViewModel workStationViewModel);

        void selectWorkStation(WorkStationViewModel workStationViewModel);

        void loadWorkStationsOnSuccess(List<DomainWorkStationModel> domainWorkStations);

        void loadWorkStationsOnError(Throwable throwable);

        void loadWorkStations();

        void lockDevice();
    }

    interface ItemClickListener {
        void onItemClick(android.view.View view);
    }

}
