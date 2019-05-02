package kioskmode.com.epoptia.kioskmode;

import dagger.Binds;
import dagger.Module;
import kioskmode.com.epoptia.kioskmode.viewmodel.KioskModeContract;
import kioskmode.com.epoptia.kioskmode.viewmodel.KioskModeViewModel;

@Module
public abstract class KioskModeActivityModule {

    /**
     * @Provides methods are instance methods and they need an instance of our module in order to be invoked.
     * If our Module is abstract and contains @Binds methods,
     * dagger will not instantiate our module and instead directly use the Provider of our injected parameter.
     */

    //region Providers

    @Binds
    public abstract KioskModeContract.ViewModel provideKioskModeContractViewModel(KioskModeViewModel kioskModeViewModel);

    //endregion

}
