package kioskmode.com.epoptia.kioskmode.workers;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import kioskmode.com.epoptia.R;
import kioskmode.com.epoptia.adapters.RecyclerViewAdapter;
import kioskmode.com.epoptia.base.BaseFragment;
import kioskmode.com.epoptia.databinding.FragmentStationWorkersBinding;
import kioskmode.com.epoptia.kioskmode.KioskModeActivity;
import kioskmode.com.epoptia.kioskmode.workers.viewmodel.StationWorkersContract;
import kioskmode.com.epoptia.lifecycle.Lifecycle;
import kioskmode.com.epoptia.viewmodel.models.StationWorkersViewModel;
import kioskmode.com.epoptia.viewmodel.models.WorkStationViewModel;
import kioskmode.com.epoptia.viewmodel.models.WorkerViewModel;

public class StationWorkersFragment extends BaseFragment implements StationWorkersContract.View {

    //region Injections

    @Inject
    StationWorkersRetainFragment mStationWorkersRetainFragment;

    @Inject
    StationWorkersContract.ViewModel mViewModel;

    //endregion

    //region Private Properties

    //todo remove
    private static final String debugTag = StationWorkersFragment.class.getSimpleName();

    private FragmentStationWorkersBinding mBinding;

    private View mView;

    private StationWorkersViewModel stationWorkersViewModel;

    private WorkStationViewModel workStationViewModel;

    private List<WorkerViewModel> stationWorkers = new ArrayList<>();

    private RecyclerViewAdapter rcvAdapter;

    private StationWorkerClickListener stationWorkerClickListener;

    //endregion

    //region Lifecycle Methods

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null ) {

            mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_station_workers, container, false);

            mView = mBinding.getRoot();
        }

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            workStationViewModel = savedInstanceState.getParcelable(getResources().getString(R.string.workstation_view_model_parcel));
        } else {
            if (getArguments() != null) {
                stationWorkersViewModel = getArguments().getParcelable(getResources().getString(R.string.stationworkers_view_model_parcel));

                //station workers view model already checked in navigateUserToStationWorkersScreen() of KioskModeActivity class
                workStationViewModel = stationWorkersViewModel.getWorkStationViewModel();
            }
        }

        retainViewModel();

        setToolbarTitle(workStationViewModel);

        initializeStationWorkersRcv();
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = new Bundle();

        bundle.putParcelable(getResources().getString(R.string.workstation_view_model_parcel), workStationViewModel);

        //todo add usage comment
        //pass data to onViewAttached
        super.onStartWithSavedInstanceState(bundle);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //retain only workstation view model
        //only workstation id and name must be saved across configuration changes
        outState.putParcelable(getResources().getString(R.string.workstation_view_model_parcel), workStationViewModel);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e(debugTag, "VIEW MODEL INSTANCE onDestroy!!!!!! => " + mViewModel);

        mStationWorkersRetainFragment.retainViewModel(mViewModel);
    }

    //endregion

    //region Public Methods

    public static StationWorkersFragment newInstance(StationWorkersViewModel stationWorkersViewModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("stationworkers_view_model_parcel", stationWorkersViewModel);

        StationWorkersFragment workersFragment = new StationWorkersFragment();
        workersFragment.setArguments(bundle);

        return workersFragment;
    }

    @Override
    public Lifecycle.ViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public void setProcessing(boolean isProcessing) {

    }

    @Override
    public void loadStationWorkersOnSuccess(List<WorkerViewModel> workers) {
        this.stationWorkers = workers;

        rcvAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadStationWorkersOnError(Throwable throwable) {
        //todo add error handing
    }

    @Override
    public void onStationWorkerItemClick(View view) {

    }

    //endregion

    //region Private Methods

    private void retainViewModel() {
        if (getActivity() == null) {
            return;
        }

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        if (fragmentManager.findFragmentByTag(getResources().getString(R.string.station_workers_retain_fragment)) == null) {

            fragmentManager.beginTransaction().add(mStationWorkersRetainFragment, getResources().getString(R.string.station_workers_retain_fragment)).commit();
        } else {
            mStationWorkersRetainFragment = (StationWorkersRetainFragment) fragmentManager.findFragmentByTag(getResources().getString(R.string.station_workers_retain_fragment));

            if (mStationWorkersRetainFragment == null) {
                return;
            }

            if (mStationWorkersRetainFragment.getViewModel() != null) mViewModel = mStationWorkersRetainFragment.getViewModel();

            Log.e(debugTag, "VIEW MODEL INSTANCE !!!!!! => " + mViewModel);
        }
    }

    private void setToolbarTitle(WorkStationViewModel workStationViewModel) {
        String toolbarTitle = null;

        String workStationName = workStationViewModel.getName();

        int currentOrientation = getResources().getConfiguration().orientation;

        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            toolbarTitle = getResources().getString(R.string.workers_frgmt_title) + " " + workStationName;
        } else {
            int stationNameLength = workStationName.length();

            if (stationNameLength > 19) {
                String substr = workStationName.substring(0, 19);

                toolbarTitle = getResources().getString(R.string.workers_frgmt_portrait_title) + " " + substr + "...";
            } else {
                toolbarTitle = getResources().getString(R.string.workers_frgmt_portrait_title) + " " + workStationName;
            }
        }

        if (getActivity() != null) {
            ((KioskModeActivity)getActivity()).setToolbarTitle(toolbarTitle);
            ((KioskModeActivity)getActivity()).setToolbarUsernameTitle("");
        }
    }

    private void initializeStationWorkersRcv() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        if (rcvAdapter == null) {
            mBinding.rclView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        }

        rcvAdapter = new RecyclerViewAdapter(R.layout.station_workers_rcv_row) {
            @Override
            protected Object getObjForPosition(int position, ViewDataBinding mBinding) {
                return stationWorkers.get(position);
            }

            @Override
            protected int getLayoutIdForPosition(int position) {
                return R.layout.station_workers_rcv_row;
            }

            @Override
            protected int getTotalItems() {
                return stationWorkers.size();
            }

            @Override
            protected Object getClickListenerObject() {
                return stationWorkerClickListener;
            }
        };

        mBinding.rclView.setLayoutManager(linearLayoutManager);
        mBinding.rclView.setNestedScrollingEnabled(false);
        mBinding.rclView.setAdapter(rcvAdapter);
    }

    //endregion

}
