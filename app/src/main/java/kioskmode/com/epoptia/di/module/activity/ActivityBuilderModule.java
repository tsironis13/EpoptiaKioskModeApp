package kioskmode.com.epoptia.di.module.activity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import kioskmode.com.epoptia.splashscreen.SplashScreenActivity;
import kioskmode.com.epoptia.admin.WorkStationsActivity;
import kioskmode.com.epoptia.di.scope.ActivityScope;
import kioskmode.com.epoptia.kioskmodetablet.KioskModeActivity;
import kioskmode.com.epoptia.login.LoginActivity;
import kioskmode.com.epoptia.login.LoginActivityModule;
import kioskmode.com.epoptia.splashscreen.SplashScreenActivityModule;

/**
 * AndroidSupportInjectionModule is a dagger @Module,
 * that will provide instance of DispatchingAndroidInjector<Activity>.
 * Include AndroidSupportInjectionModule to generate code
 * that is necessary for AndroidInjection.
 */
@Module(includes = AndroidSupportInjectionModule.class)
public abstract class ActivityBuilderModule {

    //region Activity Injectors

    /**
     * Put SplashScreenActivity into injector map.
     *
     * @return
     */
    @ActivityScope
    @ContributesAndroidInjector(modules = SplashScreenActivityModule.class)
    abstract SplashScreenActivity contributesSplashScreenActivityInjector();


    /**
     * Put LoginActivity into injector map.
     *
     * @return
     */
    @ActivityScope
    @ContributesAndroidInjector(modules = LoginActivityModule.class)
    abstract LoginActivity contributesLoginActivityInjector();

    /**
     * Put WorkStationsActivity into injector map.
     *
     * @return
     */
    @ActivityScope
    @ContributesAndroidInjector()
    abstract WorkStationsActivity contributesWorkStationsActivityInjector();

    /**
     * Put KioskModeActivity tablet into injector map.
     *
     * @return
     */
    @ActivityScope
    @ContributesAndroidInjector()
    abstract KioskModeActivity contributesKioskModeActivityTabletInjector();

    /**
     * Put KioskModeActivity phone into injector map.
     *
     * @return
     */
    @ActivityScope
    @ContributesAndroidInjector()
    abstract kioskmode.com.epoptia.kioskmodephone.KioskModeActivity contributesKioskModeActivityPhoneInjector();

    //endregion

}
