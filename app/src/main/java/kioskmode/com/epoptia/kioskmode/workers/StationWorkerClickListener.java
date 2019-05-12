package kioskmode.com.epoptia.kioskmode.workers;

import android.view.View;

import kioskmode.com.epoptia.kioskmode.workers.viewmodel.StationWorkersContract;

public class StationWorkerClickListener implements StationWorkersContract.ItemClickListener {

    //region Private Properties

    private StationWorkersContract.View mView;

    //endregion

    //region Public Methods

    public StationWorkerClickListener(StationWorkersContract.View mView) {
        this.mView = mView;
    }

    //endregion

    //region Public Methods

    @Override
    public void onItemClick(View view) {
        mView.onStationWorkerItemClick(view);
    }

    //endregion

}
