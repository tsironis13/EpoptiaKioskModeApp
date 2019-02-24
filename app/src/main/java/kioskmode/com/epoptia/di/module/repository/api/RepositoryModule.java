package kioskmode.com.epoptia.di.module.repository.api;

import dagger.Binds;
import dagger.Module;
import data.com.epoptia.network.repository.ClientRepositoryImpl;
import data.com.epoptia.network.repository.UserRepositoryImpl;
import domain.com.epoptia.repository.api.ClientRepository;
import domain.com.epoptia.repository.api.UserRepository;

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

    //endregion

}
