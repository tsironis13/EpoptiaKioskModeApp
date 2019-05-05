package domain.com.epoptia.interactor.type;

import io.reactivex.Completable;

public interface CompletableUseCaseWithTwoParameters<P1, P2> {

    Completable execute(P1 parameter1, P2 parameter2);

}
