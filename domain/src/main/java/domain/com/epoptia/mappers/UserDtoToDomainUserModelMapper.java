package domain.com.epoptia.mappers;

import javax.inject.Inject;

import domain.com.epoptia.model.domain.DomainUserModel;
import domain.com.epoptia.model.dto.result.UserDto;
import io.reactivex.Single;

public class UserDtoToDomainUserModelMapper {

    //region Injections

    @Inject
    DomainUserModel domainUserModel;

    //endregion

    //region Constructor

    @Inject
    public UserDtoToDomainUserModelMapper() {}

    //endregion

    //region Public Methods

    public Single<DomainUserModel> map(UserDto userDto) {
        domainUserModel.setAccessToken(userDto.getAccessToken());

        return Single.just(domainUserModel);
    }

    //endregion

}
