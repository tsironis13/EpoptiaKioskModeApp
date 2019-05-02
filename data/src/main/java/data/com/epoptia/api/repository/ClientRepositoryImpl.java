package data.com.epoptia.api.repository;

import javax.inject.Inject;

import data.com.epoptia.api.service.BaseNetworkServiceWrapper;
import domain.com.epoptia.model.dto.post.ValidateClientSubDomainPostDto;
import domain.com.epoptia.model.dto.result.UserDto;
import domain.com.epoptia.repository.api.ClientRepository;
import io.reactivex.Single;

public class ClientRepositoryImpl implements ClientRepository {

    //region Injections

    @Inject
    BaseNetworkServiceWrapper baseNetworkServiceWrapper;

    //endregion

    //region Constructor

    @Inject
    public ClientRepositoryImpl() {}

    //endregion

    //region Public Methods

    @Override
    public Single<UserDto> validateClientSubDomain(String subDomain, ValidateClientSubDomainPostDto validateClientSubDomainDto) throws Exception {
        return baseNetworkServiceWrapper.getServiceAPI(subDomain).validateClientSubDomain(validateClientSubDomainDto);
    }

    //endregion

}
