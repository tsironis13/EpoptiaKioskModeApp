package kioskmode.com.epoptia.workstations.viewmodel;

import android.os.Bundle;
import android.view.View;

import javax.inject.Inject;

import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.viewmodel.models.WorkStationViewModel;

public class WorkStationsPhoneViewModel extends WorkStationsViewModel {

    //region Constructor

    @Inject
    public WorkStationsPhoneViewModel() {}

    //endregion

    //region Public Methods

    @Override
    public void onViewAttached(Bundle savedInstanceState, Lifecycle.View viewCallback) {
        super.onViewAttached(savedInstanceState, viewCallback);
    }

    @Override
    public void loadWorkStations() {
        super.loadWorkStations();
    }

    @Override
    public void selectWorkStation(WorkStationViewModel workStationViewModel) {
        super.saveWorkStation(workStationViewModel);

        mViewCallback.startKioskModeActivity();
    }

    //endregion

}
