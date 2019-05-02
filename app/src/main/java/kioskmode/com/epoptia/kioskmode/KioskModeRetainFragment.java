package kioskmode.com.epoptia.kioskmode;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import kioskmode.com.epoptia.kioskmode.viewmodel.KioskModeContract;

public class KioskModeRetainFragment extends Fragment {

    //region Private Properties

    private KioskModeContract.ViewModel mViewModel;

    //endregion

    //region Constructor

    @Inject
    public KioskModeRetainFragment() {}

    //endregion

    //region Lifecycle Methods

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);
    }

    //endregion

    //region Public Methods

    public void retainViewModel(KioskModeContract.ViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    public KioskModeContract.ViewModel getViewModel() {
        return mViewModel;
    }

    //endregion

}
