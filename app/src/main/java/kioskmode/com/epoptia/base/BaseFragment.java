package kioskmode.com.epoptia.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import dagger.android.support.AndroidSupportInjection;
import kioskmode.com.epoptia.lifecycle.Lifecycle;

public abstract class BaseFragment extends Fragment implements Lifecycle.View {

    //region Injections

    //endregion

    //region Lifecycle Methods

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        // Perform injection here before M, L (API 22) and below because onAttach(Context)
        // is not yet available at L.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) AndroidSupportInjection.inject(this);

        super.onAttach(activity);
    }

    @Override
    public void onAttach(Context context) {
        // Perform injection here for M (API 23) due to deprecation of onAttach(Activity).
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) AndroidSupportInjection.inject(this);

        super.onAttach(context);
    }

    @Override
    public void onStartWithSavedInstanceState(Bundle savedInstanceState) {
        Lifecycle.ViewModel lifecycleViewModel = getViewModel();

        if (lifecycleViewModel != null) lifecycleViewModel.onViewAttached(savedInstanceState,this);
    }

    @Override
    public void onResume() {
        super.onResume();

        Lifecycle.ViewModel lifecycleViewModel = getViewModel();

        if (lifecycleViewModel != null) lifecycleViewModel.onViewResumed();
    }

    @Override
    public void onStop() {
        super.onStop();

        Lifecycle.ViewModel lifecycleViewModel = getViewModel();

        if (lifecycleViewModel != null) lifecycleViewModel.onViewDetached();
    }

    //endregion

    //region Public Methods

    public abstract Lifecycle.ViewModel getViewModel();

    //endregion

}
