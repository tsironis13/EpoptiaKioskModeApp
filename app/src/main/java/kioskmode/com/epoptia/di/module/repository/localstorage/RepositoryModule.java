package kioskmode.com.epoptia.di.module.repository.localstorage;

import dagger.Binds;
import dagger.Module;
import data.com.epoptia.localstorage.prefs.repository.ClientRepositoryImpl;
import data.com.epoptia.localstorage.prefs.repository.DeviceRepositoryImpl;
import data.com.epoptia.localstorage.prefs.repository.UserRepositoryImpl;
import data.com.epoptia.localstorage.prefs.repository.WorkStationRepositoryImpl;
import data.com.epoptia.localstorage.prefs.repository.WorkerPanelRepositoryImpl;
import data.com.epoptia.localstorage.prefs.repository.WorkerRepositoryImpl;
import domain.com.epoptia.repository.localstorage.prefs.ClientRepository;
import domain.com.epoptia.repository.localstorage.prefs.DeviceRepository;
import domain.com.epoptia.repository.localstorage.prefs.UserRepository;
import domain.com.epoptia.repository.localstorage.prefs.WorkStationRepository;
import domain.com.epoptia.repository.localstorage.prefs.WorkerPanelRepository;
import domain.com.epoptia.repository.localstorage.prefs.WorkerRepository;

@Module
public abstract class RepositoryModule {

    /**
     * @Provides methods are instance methods and they need an instance of our module in order to be invoked.
     * If our Module is abstract and contains @Binds methods,
     * dagger will not instantiate our module and instead directly use the Provider of our injected parameter.
     */

    //region Providers

    @Binds
    public abstract UserRepository provideUserRepository(UserRepositoryImpl userRepository);

    @Binds
    public abstract ClientRepository provideClientRepository(ClientRepositoryImpl customerRepository);

    @Binds
    public abstract DeviceRepository provideDeviceRepository(DeviceRepositoryImpl deviceRepository);

    @Binds
    public abstract WorkStationRepository provideWorkStationRepository(WorkStationRepositoryImpl workStationRepository);

    @Binds
    public abstract WorkerPanelRepository provideWorkerPanelRepository(WorkerPanelRepositoryImpl workerPanelRepository);

    @Binds
    public abstract WorkerRepository provideWorkerRepository(WorkerRepositoryImpl workerRepository);

    //endregion

}
