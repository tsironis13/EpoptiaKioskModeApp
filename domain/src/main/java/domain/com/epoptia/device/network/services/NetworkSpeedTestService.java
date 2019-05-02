package domain.com.epoptia.device.network.services;

import domain.com.epoptia.model.domain.DomainSpeedTestModel;
import io.reactivex.Flowable;

public interface NetworkSpeedTestService {

    //todo
    void startService();

    //todo
    Flowable<DomainSpeedTestModel> addSpeedTestListener();

    void stopService();

}
