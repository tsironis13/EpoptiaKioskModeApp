package kioskmode.com.epoptia.di.component;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import kioskmode.com.epoptia.app.MyApplication;
import kioskmode.com.epoptia.di.module.AppModule;
import kioskmode.com.epoptia.di.module.activity.ActivityBuilderModule;

@Singleton
@Component(modules = { AppModule.class, ActivityBuilderModule.class })
public interface AppComponent extends AndroidInjector<MyApplication> {

    //region Builder

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<MyApplication> {}

    //endregion

}
