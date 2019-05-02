package kioskmode.com.epoptia.di.module.utility;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import device.com.epoptia.DeviceUtilityImpl;
import device.com.epoptia.network.services.NetworkSpeedTestServiceImpl;
import device.com.epoptia.network.NetworkUtilityImpl;
import domain.com.epoptia.device.DeviceUtility;
import domain.com.epoptia.device.network.services.NetworkSpeedTestService;
import domain.com.epoptia.device.network.NetworkUtility;
import domain.com.epoptia.model.domain.DomainDeviceModel;
import domain.com.epoptia.model.domain.DomainSpeedTestModel;
import fr.bmartel.speedtest.SpeedTestSocket;
import kioskmode.com.epoptia.di.scope.ApplicationContextScope;

@Module
public abstract class DeviceModule {

    /**
     * @Provides methods are instance methods and they need an instance of our module in order to be invoked.
     * If our Module is abstract and contains @Binds methods,
     * dagger will not instantiate our module and instead directly use the Provider of our injected parameter.
     */

    //region Providers

    @Provides
    static SpeedTestSocket provideSpeedTestSocket() {
        return new SpeedTestSocket();
    }

    @Provides
    static NetworkUtility provideNetwork(@ApplicationContextScope Context context) {
        return new NetworkUtilityImpl(context);
    }

    @Provides
    static DeviceUtility provideDevice(@ApplicationContextScope Context context) {
        return new DeviceUtilityImpl(context);
    }

    @Provides
    @Singleton
    static NetworkSpeedTestService provideNetworkSpeedTestService(SpeedTestSocket speedTestSocket, DomainSpeedTestModel domainSpeedTestModel) {
        return new NetworkSpeedTestServiceImpl(speedTestSocket, domainSpeedTestModel);
    }

    //endregion

}
