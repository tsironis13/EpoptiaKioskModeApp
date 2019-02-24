package kioskmode.com.epoptia.login;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class LoginActivityModule {

    /**
     * @Provides methods are instance methods and they need an instance of our module in order to be invoked.
     * If our Module is abstract and contains @Binds methods,
     * dagger will not instantiate our module and instead directly use the Provider of our injected parameter.
     */

    //region Providers

    @Binds
    public abstract LoginContract.ViewModel provideLoginContractViewModel(LoginViewModel loginViewModel);

    //endregion

}
