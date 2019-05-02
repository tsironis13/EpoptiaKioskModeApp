package kioskmode.com.epoptia.lifecycle;

import android.os.Bundle;

public interface Lifecycle {

    interface View {
        void onStartWithSavedInstanceState(Bundle savedInstanceState);
    }

    interface ViewModel {
        void onViewAttached(Bundle savedInstanceState, Lifecycle.View viewCallback);

        void onViewResumed();

        void onViewDetached();
    }

}
