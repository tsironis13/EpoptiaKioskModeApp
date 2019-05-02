package domain.com.epoptia.utilities.error;

import javax.inject.Inject;

import domain.com.epoptia.Constants;
import domain.com.epoptia.model.dto.result.BaseResponseDto;
import io.reactivex.Flowable;

public class ServerSuccessResponseFlowableValidatorImpl implements ServerSuccessResponseFlowableValidator {

    //region Constructor

    @Inject
    public ServerSuccessResponseFlowableValidatorImpl() {}

    //endregion

    //region Public Methods

    @Override
    public <T extends BaseResponseDto> Flowable<T> validateResponse(T response) {
        if (response.getCode() != Constants.RESPONSE_OK) {
            return Flowable.error(new Exception(response.getDescription()));
        }

        return Flowable.just(response);
    }

    //endregion

}
