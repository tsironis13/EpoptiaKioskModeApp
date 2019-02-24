package kioskmode.com.epoptia.lifecycle;

public interface Lifecycle {

    interface View {

    }

    interface ViewModel {
        void onViewAttached(Lifecycle.View viewCallback);

        void onViewResumed();

        void onViewDetached();
    }

}
