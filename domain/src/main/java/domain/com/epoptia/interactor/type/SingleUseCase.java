package domain.com.epoptia.interactor.type;

import io.reactivex.Single;

public interface SingleUseCase<T> {

    Single<T> execute();
}
