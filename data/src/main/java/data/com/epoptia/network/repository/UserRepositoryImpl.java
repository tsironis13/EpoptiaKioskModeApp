package data.com.epoptia.network.repository;

import javax.inject.Inject;

import data.com.epoptia.network.service.BaseServiceWrapper;
import domain.com.epoptia.model.dto.post.LoginAdminPostDto;
import domain.com.epoptia.model.dto.result.UserDto;
import domain.com.epoptia.repository.api.UserRepository;
import io.reactivex.Single;

public class UserRepositoryImpl implements UserRepository {

    //region Injections

    @Inject
    BaseServiceWrapper baseServiceWrapper;

    //endregion

    //region Constructor

    @Inject
    public UserRepositoryImpl() {}

    //endregion

    //region Public Methods

    @Override
    public Single<UserDto> loginAdmin(String subDomain, LoginAdminPostDto loginAdminPostDto) throws Exception {
        return baseServiceWrapper.getServiceAPI(subDomain).loginAdmin(loginAdminPostDto);
    }

    //endregion

}
