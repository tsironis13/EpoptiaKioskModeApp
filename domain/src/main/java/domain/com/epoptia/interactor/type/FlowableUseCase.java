package domain.com.epoptia.interactor.type;

import io.reactivex.Flowable;

public interface FlowableUseCase<T> {

    Flowable<T> execute();

}
