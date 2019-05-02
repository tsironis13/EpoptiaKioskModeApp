package domain.com.epoptia.validator;

import javax.inject.Inject;

import domain.com.epoptia.Constants;
import domain.com.epoptia.model.dto.post.ValidateClientSubDomainPostDto;
import domain.com.epoptia.validator.type.SingleValidatorWithParameter;
import io.reactivex.Single;

public class ClientSubDomainPostDtoValidator implements SingleValidatorWithParameter<ValidateClientSubDomainPostDto, ValidateClientSubDomainPostDto> {

    //region Constructor

    @Inject
    public ClientSubDomainPostDtoValidator() {}

    //endregion

    //region Public Methods

    public Single<ValidateClientSubDomainPostDto> validate(ValidateClientSubDomainPostDto validateClientSubDomainDto) {
        if (validateClientSubDomainDto == null) {
            return Single.error(new Exception(Constants.INVALID_DTO, new NullPointerException()));
        }

        if (validateClientSubDomainDto.getClientSubDomain() == null || validateClientSubDomainDto.getClientSubDomain().isEmpty()) {
            return Single.error(new Exception(Constants.SUBDOMAIN_REQUIRED, new NullPointerException()));
        }

        return Single.just(validateClientSubDomainDto);
    }

    //endregion

}
