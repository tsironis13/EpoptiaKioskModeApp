package kioskmode.com.epoptia.kioskmode.workers;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import kioskmode.com.epoptia.kioskmode.workers.viewmodel.StationWorkersContract;

public class StationWorkersRetainFragment extends Fragment {

    //region Private Properties

    private StationWorkersContract.ViewModel mViewModel;

    //endregion

    //region Constructor

    @Inject
    public StationWorkersRetainFragment() {}

    //endregion

    //region Lifecycle Methods

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);
    }

    //endregion

    //region Public Methods

    public void retainViewModel(StationWorkersContract.ViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    public StationWorkersContract.ViewModel getViewModel() {
        return mViewModel;
    }

    //endregion

}
