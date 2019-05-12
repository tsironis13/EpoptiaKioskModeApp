package kioskmode.com.epoptia.workstations;

import android.view.View;

import kioskmode.com.epoptia.workstations.viewmodel.WorkStationsContract;

/**
 * Created by giannis on 5/9/2017.
 */

public class WorkStationClickListener implements WorkStationsContract.ItemClickListener {

    //region Private Properties

    private WorkStationsContract.View mView;

    //endregion

    //region Public Methods

    public WorkStationClickListener(WorkStationsContract.View mView) {
        this.mView = mView;
    }

    //endregion

    //region Public Methods

    @Override
    public void onItemClick(View view) {
        mView.onWorkStationItemClick(view);
    }

    //endregion

}
