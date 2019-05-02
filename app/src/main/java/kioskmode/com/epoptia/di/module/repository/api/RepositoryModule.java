package kioskmode.com.epoptia.di.module.repository.api;

import dagger.Binds;
import dagger.Module;
import data.com.epoptia.api.repository.ClientRepositoryImpl;
import data.com.epoptia.api.repository.DeviceRepositoryImpl;
import data.com.epoptia.api.repository.UserRepositoryImpl;
import data.com.epoptia.api.repository.WorkStationsRepositoryImpl;
import domain.com.epoptia.repository.api.ClientRepository;
import domain.com.epoptia.repository.api.DeviceRepository;
import domain.com.epoptia.repository.api.UserRepository;
import domain.com.epoptia.repository.api.WorkStationsRepository;

@Module
public abstract class RepositoryModule {

    /**
     * @Provides methods are instance methods and they need an instance of our module in order to be invoked.
     * If our Module is abstract and contains @Binds methods,
     * dagger will not instantiate our module and instead directly use the Provider of our injected parameter.
     */

    //region Providers

    @Binds
    public abstract ClientRepository provideClientApiRepository(ClientRepositoryImpl clientApiRepositoryImpl);

    @Binds
    public abstract UserRepository provideUserApiRepository(UserRepositoryImpl userApiRepositoryImpl);

    @Binds
    public abstract WorkStationsRepository provideWorkStationsRepository(WorkStationsRepositoryImpl workStationsRepository);

    @Binds
    public abstract DeviceRepository provideDeviceRepository(DeviceRepositoryImpl deviceRepository);

    //endregion

}
