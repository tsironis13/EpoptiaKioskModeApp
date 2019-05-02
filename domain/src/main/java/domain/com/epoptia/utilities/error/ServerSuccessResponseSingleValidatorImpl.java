package domain.com.epoptia.utilities.error;

import javax.inject.Inject;

import domain.com.epoptia.Constants;
import domain.com.epoptia.model.dto.result.BaseResponseDto;
import io.reactivex.Single;

/**
 * Use this class to validate server successful
 * responses.
 */
public class ServerSuccessResponseSingleValidatorImpl implements ServerSuccessResponseSingleValidator {

    //region Constructor

    @Inject
    public ServerSuccessResponseSingleValidatorImpl() {}

    //endregion

    //region Public Methods

    @Override
    public <T extends BaseResponseDto> Single<T> validateResponse(T response) {
        if (response.getCode() != Constants.RESPONSE_OK) {
            return Single.error(new Exception(response.getDescription()));
        }

        return Single.just(response);
    }

    //endregion

}
