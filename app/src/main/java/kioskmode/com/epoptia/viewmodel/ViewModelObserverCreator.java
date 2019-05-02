package kioskmode.com.epoptia.viewmodel;

public abstract class ViewModelObserverCreator {

    //region Public Methods

    public abstract ViewModelCompletableObserver createCompletableObserver();

    public abstract ViewModelSingleObserver createSingleObserver();

    public abstract ViewModelFlowableObserver createFlowableObserver();

    public ViewModelCompletableObserver getViewModelCompletableObserver() {
        return createCompletableObserver();
    }

    public ViewModelSingleObserver getViewModelSingleObserver() {
        return createSingleObserver();
    }

    public ViewModelFlowableObserver getViewModelFlowableObserver() {
        return createFlowableObserver();
    }

    //endregion

}
