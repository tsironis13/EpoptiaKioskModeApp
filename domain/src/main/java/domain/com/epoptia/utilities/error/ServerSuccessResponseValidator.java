package domain.com.epoptia.utilities.error;

import javax.inject.Inject;

import domain.com.epoptia.model.dto.result.BaseResponseDto;
import io.reactivex.Single;

/**
 * Use this class to validate server successful
 * responses.
 */
public class ServerSuccessResponseValidator {

    //region Constructor

    @Inject
    public ServerSuccessResponseValidator() {}

    //endregion

    //region Public Methods

    public <T extends BaseResponseDto> Single<T> validateSingleResponse(T response) {
        if (response.getCode() != 200) {
            return Single.error(new Exception());
        }

        return Single.just(response);
    }

    //endregion

}
