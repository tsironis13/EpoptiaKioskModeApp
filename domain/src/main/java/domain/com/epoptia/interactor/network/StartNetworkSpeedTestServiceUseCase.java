package domain.com.epoptia.interactor.network;

import javax.inject.Inject;

import domain.com.epoptia.device.network.NetworkUtility;
import domain.com.epoptia.device.network.services.NetworkSpeedTestService;
import domain.com.epoptia.interactor.type.FlowableUseCase;
import domain.com.epoptia.model.domain.DomainNetworkStateModel;
import domain.com.epoptia.model.domain.DomainSpeedTestModel;
import fr.bmartel.speedtest.model.SpeedTestError;
import io.reactivex.Flowable;

import static fr.bmartel.speedtest.model.SpeedTestError.CONNECTION_ERROR;

public class StartNetworkSpeedTestServiceUseCase implements FlowableUseCase<DomainNetworkStateModel> {

    //region Injections

    @Inject
    DomainNetworkStateModel domainNetworkStateModel;

    @Inject
    NetworkSpeedTestService networkSpeedTestService;

    @Inject
    NetworkUtility networkUtility;

    //endregion

    //region Constructor

    @Inject
    public StartNetworkSpeedTestServiceUseCase() {}

    //endregion

    //region Public Methods

    @Override
    public Flowable<DomainNetworkStateModel> execute() {
        networkSpeedTestService.startService();

        //combineLatest simply combines multiple sources and emits any time there’s a new value from any of them.

        //zip operator The difference is that it waits until there is a new value from each stream
        return Flowable.combineLatest(
                networkUtility.getNetworkLinkSpeed().toFlowable(),
                networkSpeedTestService.addSpeedTestListener(),
                (linkSpeed, speedTestModel) -> {
                    SpeedTestError speedTestError = speedTestModel.getSpeedTestErrorModel();

                    if (speedTestError != null) {
                        if (speedTestError == CONNECTION_ERROR) {
                            domainNetworkStateModel.setMsg("NO INTERNET CONNECTION");
                        }
                    } else {
                        if (linkSpeed <= 15 && speedTestModel.getTransferRateBit() <= 0.5) {
                            domainNetworkStateModel.setMsg("Low NetworkUtility Connection <100Mbps (Please check out Wifi and Lan) \n Low Internet Connection <15 Mbps");
                        } else if (speedTestModel.getTransferRateBit() <= 0.5) {
                            domainNetworkStateModel.setMsg("Low Internet Connection <15 Mbps");
                        } else if (speedTestModel.getTransferRateBit() <= 15) {
                            domainNetworkStateModel.setMsg("Low NetworkUtility Connection <100Mbps (Please check out Wifi and Lan)");
                        } else {
                            domainNetworkStateModel.setMsg(null);
                        }
                    }

                    return domainNetworkStateModel;
                }
        );
    }

    //endregion

}
