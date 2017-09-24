package kioskmode.com.epoptia.admin;

/**
 * Created by giannis on 5/9/2017.
 */

public interface WordStationsContract {

    interface Presenter {
        void onBaseViewClick(android.view.View view);
    }

    interface View {
        void onBaseViewClick(android.view.View view);
    }

}
