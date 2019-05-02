package domain.com.epoptia.utilities.error;

import domain.com.epoptia.model.dto.result.BaseResponseDto;
import io.reactivex.Flowable;

public interface ServerSuccessResponseFlowableValidator {

    <T extends BaseResponseDto> Flowable<T> validateResponse(T response);

}
