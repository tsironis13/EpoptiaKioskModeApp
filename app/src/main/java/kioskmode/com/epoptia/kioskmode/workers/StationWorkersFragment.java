package kioskmode.com.epoptia.kioskmode.workers;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import domain.com.epoptia.model.domain.DomainWorkerModel;
import kioskmode.com.epoptia.R;
import kioskmode.com.epoptia.adapters.RecyclerViewAdapter;
import kioskmode.com.epoptia.base.BaseFragment;
import kioskmode.com.epoptia.databinding.FragmentStationWorkersBinding;
import kioskmode.com.epoptia.kioskmode.KioskModeActivity;
import kioskmode.com.epoptia.viewmodel.models.StationWorkersViewModel;
import kioskmode.com.epoptia.viewmodel.models.WorkStationViewModel;

public class StationWorkersFragment extends BaseFragment {

    //region Injections


    //endregion

    //region Private Properties

    private FragmentStationWorkersBinding mBinding;

    private View mView;

    private StationWorkersViewModel stationWorkersViewModel;

    private WorkStationViewModel workStationViewModel;

    private List<DomainWorkerModel> stationWorkers = new ArrayList<>();

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

        setToolbarTitle(workStationViewModel);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        mBinding.rcv.setLayoutManager(linearLayoutManager);
        mBinding.rcv.setHasFixedSize(true);
        mBinding.rcv.setAdapter(new RecyclerViewAdapter(R.layout.station_workers_tablet_rcv_row) {
            @Override
            protected Object getObjForPosition(int position, ViewDataBinding mBinding) {
                return stationWorkers.get(position);
            }

            @Override
            protected Object getClickListenerObject() {
                return null;
            }

            @Override
            protected int getLayoutIdForPosition(int position) {
                return R.layout.station_workers_tablet_rcv_row;
            }

            @Override
            protected int getTotalItems() {
                return stationWorkers.size();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //retain only workstation view model
        //only workstation id and name must be saved across configuration changes
        outState.putParcelable(getResources().getString(R.string.workstation_view_model_parcel), workStationViewModel);
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

    //endregion

    //region Private Methods

    private void setToolbarTitle(WorkStationViewModel workStationViewModel) {
        String toolbarTitle = null;
        String workStationName = workStationViewModel.getWorkStationName();

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

    //endregion

}
