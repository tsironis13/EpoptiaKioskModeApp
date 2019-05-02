package kioskmode.com.epoptia.di.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fr.bmartel.speedtest.SpeedTestSocket;
import kioskmode.com.epoptia.app.MyApplication;
import kioskmode.com.epoptia.di.module.repository.localstorage.RepositoryModule;
import kioskmode.com.epoptia.di.module.utility.DeviceModule;
import kioskmode.com.epoptia.di.scope.ApplicationContextScope;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = { RepositoryModule.class, kioskmode.com.epoptia.di.module.repository.api.RepositoryModule.class, DeviceModule.class })
public class AppModule {

    //region Providers

    @Provides
    Application application(MyApplication myApp) {
        return myApp;
    }

    @Provides
    @ApplicationContextScope
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    GsonConverterFactory provideGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    //endregion

}
