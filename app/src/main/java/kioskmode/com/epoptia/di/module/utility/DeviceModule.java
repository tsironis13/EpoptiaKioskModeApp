package kioskmode.com.epoptia.di.module.utility;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import device.com.epoptia.DeviceImpl;
import device.com.epoptia.NetworkImpl;
import domain.com.epoptia.device.Device;
import domain.com.epoptia.device.Network;
import domain.com.epoptia.model.domain.DomainDeviceModel;
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
    static Network provideNetwork(@ApplicationContextScope Context context) {
        return new NetworkImpl(context);
    }

    @Provides
    static Device provideDevice(@ApplicationContextScope Context context, DomainDeviceModel device) {
        return new DeviceImpl(context, device);
    }

    //endregion

}
