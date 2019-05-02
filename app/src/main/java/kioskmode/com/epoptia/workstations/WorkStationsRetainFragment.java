package kioskmode.com.epoptia.workstations;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import kioskmode.com.epoptia.workstations.viewmodel.WorkStationsContract;

public class WorkStationsRetainFragment extends Fragment {

    //region Private Properties

    private WorkStationsContract.ViewModel mViewModel;

    //endregion

    //region Constructor

    @Inject
    public WorkStationsRetainFragment() {}

    //endregion

    //region Lifecycle Methods

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);
    }

    //endregion

    //region Public Methods

    public void retainViewModel(WorkStationsContract.ViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    public WorkStationsContract.ViewModel getViewModel() {
        return mViewModel;
    }

    //endregion

}
