package kioskmode.com.epoptia.kioskmodetablet.stationworkers;

/**
 * Created by giannis on 5/9/2017.
 */

public interface StationWorkersContract {

    interface Presenter {
        void onBaseViewClick(android.view.View view);
    }

    interface View {
        void onBaseViewClick(android.view.View view);
    }

}
