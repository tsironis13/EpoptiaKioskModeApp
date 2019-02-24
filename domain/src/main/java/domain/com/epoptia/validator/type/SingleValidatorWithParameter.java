package domain.com.epoptia.validator.type;

import io.reactivex.Single;

public interface SingleValidatorWithParameter<P, R> {

    Single<R> validate(P parameter);

}
