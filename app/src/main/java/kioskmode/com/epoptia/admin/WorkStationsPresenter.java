package kioskmode.com.epoptia.admin;

import android.view.View;

/**
 * Created by giannis on 5/9/2017.
 */

public class WorkStationsPresenter implements WordStationsContract.Presenter {

    private WordStationsContract.View mView;

    public WorkStationsPresenter(WordStationsContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void onBaseViewClick(View view) {
        mView.onBaseViewClick(view);
    }
}
