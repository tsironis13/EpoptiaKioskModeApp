package kioskmode.com.epoptia.splashscreen;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class SplashScreenActivityModule {

    /**
     * @Provides methods are instance methods and they need an instance of our module in order to be invoked.
     * If our Module is abstract and contains @Binds methods,
     * dagger will not instantiate our module and instead directly use the Provider of our injected parameter.
     */

    //region Providers

    @Binds
    public abstract SplashScreenContract.ViewModel provideSplashScreenContractViewModel(SplashScreenViewModel splashScreenViewModel);

    //endregion

}
