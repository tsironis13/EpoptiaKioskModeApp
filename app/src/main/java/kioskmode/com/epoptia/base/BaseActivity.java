package kioskmode.com.epoptia.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import dagger.android.AndroidInjection;
import kioskmode.com.epoptia.lifecycle.Lifecycle;

/**
 * Created by giannis on 27/8/2017.
 */

public abstract class BaseActivity extends AppCompatActivity implements Lifecycle.View {

    //region Private Methods



    //endregion

    //region Lifecycle Methods

    /**
     * Get instance of MyApplication and check if it implements HasActivityInjector.
     * Invoke AndroidInjector<Activity> activityInjector() to get instance of DispatchingAndroidInjector<Activity>,
     * look for injector for activity in the map of injectors stored in DispatchingAndroidInjector<Activity>.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (getViewModel() != null) getViewModel().onViewAttached(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getViewModel() != null) getViewModel().onViewResumed();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (getViewModel() != null) getViewModel().onViewDetached();
    }

    //endregion

    //region Public Methods

    public abstract Lifecycle.ViewModel getViewModel();

    public void showSnackBrMsg(String msg, View container, int length) {
        Snackbar snackbar = Snackbar.make(container, msg, length);
        snackbar.show();
    }

    //endregion

}
