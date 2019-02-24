package domain.com.epoptia.validator;

import javax.inject.Inject;

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
            return Single.error(new Exception("An error occurred.", new NullPointerException()));
        }

        if (validateClientSubDomainDto.getClientSubDomain() == null || validateClientSubDomainDto.getClientSubDomain().isEmpty()) {
            return Single.error(new Exception("Το domain είναι υποχρεωτικό."));
        }

        return Single.just(validateClientSubDomainDto);
    }

    //endregion

}
