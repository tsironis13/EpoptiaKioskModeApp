package kioskmode.com.epoptia.viewmodel;

public abstract class ViewModelObserverCreator {

    //region Public Methods

    public abstract ViewModelCompletableObserver createCompletableObserver();

    public abstract ViewModelSingleObserver createSingleObserver();

    public ViewModelCompletableObserver getViewModelCompletableObserver() {
        return createCompletableObserver();
    }

    public ViewModelSingleObserver getViewModelSingleObserver() {
        return createSingleObserver();
    }

    //endregion

}
