package kioskmode.com.epoptia.kioskmode.workers;

import dagger.Binds;
import dagger.Module;
import kioskmode.com.epoptia.kioskmode.workers.viewmodel.StationWorkersContract;
import kioskmode.com.epoptia.kioskmode.workers.viewmodel.StationWorkersViewModel;

@Module
public abstract class StationWorkersFragmentModule {

    /**
     * @Provides methods are instance methods and they need an instance of our module in order to be invoked.
     * If our Module is abstract and contains @Binds methods,
     * dagger will not instantiate our module and instead directly use the Provider of our injected parameter.
     */

    //region Providers

    @Binds
    public abstract StationWorkersContract.ViewModel provideStationWorkersContractViewModel(StationWorkersViewModel stationWorkersViewModel);

}
