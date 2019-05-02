package kioskmode.com.epoptia.workstations;

import dagger.Binds;
import dagger.Module;
import device.com.epoptia.network.services.NetworkSpeedTestServiceImpl;
import kioskmode.com.epoptia.workstations.viewmodel.WorkStationsContract;
import kioskmode.com.epoptia.workstations.viewmodel.WorkStationsViewModel;

@Module
public abstract class WorkStationsActivityModule {

    /**
     * @Provides methods are instance methods and they need an instance of our module in order to be invoked.
     * If our Module is abstract and contains @Binds methods,
     * dagger will not instantiate our module and instead directly use the Provider of our injected parameter.
     */

    //region Providers

    @Binds
    public abstract WorkStationsContract.ViewModel provideWorkStationsContractViewModel(WorkStationsViewModel workStationsViewModel);

    @Binds
    public abstract NetworkSpeedTestServiceImpl provideSpeedTestSocket(NetworkSpeedTestServiceImpl speedTestSocket);

    //endregion

}
