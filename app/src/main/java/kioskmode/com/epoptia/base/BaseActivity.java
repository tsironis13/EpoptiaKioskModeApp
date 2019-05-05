package kioskmode.com.epoptia.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.view.inputmethod.InputMethodManager;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import kioskmode.com.epoptia.lifecycle.Lifecycle;

/**
 * Created by giannis on 27/8/2017.
 */

public abstract class BaseActivity extends AppCompatActivity implements HasSupportFragmentInjector, Lifecycle.View {

    //region Injections

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

    //endregion

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
    }

    @Override
    public void onStartWithSavedInstanceState(Bundle savedInstanceState) {
        Lifecycle.ViewModel lifecycleViewModel = getViewModel();

        if (lifecycleViewModel != null) lifecycleViewModel.onViewAttached(savedInstanceState,this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Lifecycle.ViewModel lifecycleViewModel = getViewModel();

        if (lifecycleViewModel != null) lifecycleViewModel.onViewResumed();
    }

    @Override
    protected void onStop() {
        super.onStop();

        Lifecycle.ViewModel lifecycleViewModel = getViewModel();

        if (lifecycleViewModel != null) lifecycleViewModel.onViewDetached();
    }

    //endregion

    //region Public Methods

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

    public abstract Lifecycle.ViewModel getViewModel();

    public void enableImmersiveMode() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return;
        }

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    public void showSnackBrMsg(String msg, View container, int length) {
        Snackbar snackbar = Snackbar.make(container, msg, length);
        snackbar.show();
    }

    public void hideSoftKeyboard() {
        View view = getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm == null) {
                return;
            }

            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //endregion

}
