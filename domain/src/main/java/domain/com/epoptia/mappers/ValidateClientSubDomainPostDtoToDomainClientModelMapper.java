package domain.com.epoptia.mappers;

import javax.inject.Inject;

import domain.com.epoptia.model.domain.DomainClientModel;
import domain.com.epoptia.model.dto.post.ValidateClientSubDomainPostDto;
import io.reactivex.Single;

public class ValidateClientSubDomainPostDtoToDomainClientModelMapper {

    //region Injections

    @Inject
    DomainClientModel domainClientModel;

    //endregion

    //region Constructor

    @Inject
    public ValidateClientSubDomainPostDtoToDomainClientModelMapper() {}

    //endregion

    //region Public Methods

    public Single<DomainClientModel> map(ValidateClientSubDomainPostDto validateClientSubDomainPostDto) {
        domainClientModel.setSubDomain(validateClientSubDomainPostDto.getClientSubDomain());

        return Single.just(domainClientModel);
    }

    //endregion

}
