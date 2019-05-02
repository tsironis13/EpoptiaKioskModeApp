package domain.com.epoptia.utilities.error;

import domain.com.epoptia.model.dto.result.BaseResponseDto;
import io.reactivex.Single;

public interface ServerSuccessResponseSingleValidator {

    <T extends BaseResponseDto> Single<T> validateResponse(T response);

}
