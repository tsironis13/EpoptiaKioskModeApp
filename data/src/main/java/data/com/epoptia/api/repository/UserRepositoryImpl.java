package data.com.epoptia.api.repository;

import javax.inject.Inject;

import data.com.epoptia.api.service.BaseNetworkServiceWrapper;
import domain.com.epoptia.model.dto.post.LoginAdminPostDto;
import domain.com.epoptia.model.dto.result.UserDto;
import domain.com.epoptia.repository.api.UserRepository;
import io.reactivex.Single;

public class UserRepositoryImpl implements UserRepository {

    //region Injections

    @Inject
    BaseNetworkServiceWrapper baseNetworkServiceWrapper;

    //endregion

    //region Constructor

    @Inject
    public UserRepositoryImpl() {}

    //endregion

    //region Public Methods

    @Override
    public Single<UserDto> loginAdmin(String subDomain, LoginAdminPostDto loginAdminPostDto) throws Exception {
        return baseNetworkServiceWrapper.getServiceAPI(subDomain).loginAdmin(loginAdminPostDto);
    }

    //endregion

}
