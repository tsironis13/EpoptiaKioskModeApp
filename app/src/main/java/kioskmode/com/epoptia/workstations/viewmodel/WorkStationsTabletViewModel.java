package kioskmode.com.epoptia.workstations.viewmodel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import javax.inject.Inject;

import kioskmode.com.epoptia.kioskmode.KioskModeActivity;
import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.viewmodel.models.WorkStationViewModel;

public class WorkStationsTabletViewModel extends WorkStationsViewModel {

    //region Constructor

    @Inject
    public WorkStationsTabletViewModel() {}

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
    public void onViewResumed() {
        super.onViewResumed();
    }

    @Override
    public void selectWorkStation(WorkStationViewModel workStationViewModel) {
        super.saveWorkStation(workStationViewModel);

        mViewCallback.enableLockDeviceDialog();
    }

    @SuppressLint("CheckResult")
    public void lockDevice() {
        lockDeviceUseCase
                    .execute(KioskModeActivity.class)
                    .subscribe(isMyAppLauncherDefault -> {
                        if (isMyAppLauncherDefault) {
                            mViewCallback.startKioskModeActivity();
                        } else {
                            mViewCallback.startKioskModeActivityWhenAppIsNotTheDefaultLauncher();
                        }
                    }, error -> {
                        //todo
                    });
    }

    //endregion

}
