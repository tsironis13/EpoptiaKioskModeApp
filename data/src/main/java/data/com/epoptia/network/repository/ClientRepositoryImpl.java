package data.com.epoptia.network.repository;

import javax.inject.Inject;

import data.com.epoptia.network.service.BaseServiceWrapper;
import domain.com.epoptia.model.dto.post.ValidateClientSubDomainPostDto;
import domain.com.epoptia.model.dto.result.ResponseWrapperDto;
import domain.com.epoptia.model.dto.result.UserDto;
import domain.com.epoptia.repository.api.ClientRepository;
import io.reactivex.Single;

public class ClientRepositoryImpl implements ClientRepository {

    //region Injections

    @Inject
    BaseServiceWrapper baseServiceWrapper;

    //endregion

    //region Constructor

    @Inject
    public ClientRepositoryImpl() {}

    //endregion

    //region Public Methods

    @Override
    public Single<UserDto> validateClientSubDomain(String subDomain, ValidateClientSubDomainPostDto validateClientSubDomainDto) throws Exception {
        return baseServiceWrapper.getServiceAPI(subDomain).validateClientSubDomain(validateClientSubDomainDto);
    }

    //endregion

}
