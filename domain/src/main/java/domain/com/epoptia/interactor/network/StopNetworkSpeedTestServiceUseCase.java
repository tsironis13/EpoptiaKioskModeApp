package domain.com.epoptia.interactor.network;

import javax.inject.Inject;

import domain.com.epoptia.device.network.services.NetworkSpeedTestService;
import domain.com.epoptia.interactor.type.VoidUseCase;

public class StopNetworkSpeedTestServiceUseCase implements VoidUseCase {

    //region Injections

    @Inject
    NetworkSpeedTestService networkSpeedTestService;

    //endregion

    //region Constructor

    @Inject
    public StopNetworkSpeedTestServiceUseCase() {}

    //endregion

    //region Public Methods

    @Override
    public void execute() {
        networkSpeedTestService.stopService();
    }

    //endregion

}
