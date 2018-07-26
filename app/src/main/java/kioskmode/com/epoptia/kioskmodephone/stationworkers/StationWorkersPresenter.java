package kioskmode.com.epoptia.kioskmodephone.stationworkers;

import android.view.View;

/**
 * Created by giannis on 5/9/2017.
 */

public class StationWorkersPresenter implements StationWorkersContract.Presenter{

    private StationWorkersContract.View mView;

    public StationWorkersPresenter(StationWorkersContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void onBaseViewClick(View view) {
        mView.onBaseViewClick(view);
    }

}
