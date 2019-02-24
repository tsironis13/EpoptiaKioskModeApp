package domain.com.epoptia.mappers;

import javax.inject.Inject;

import domain.com.epoptia.model.domain.DomainUserModel;
import domain.com.epoptia.model.dto.result.UserDto;

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

    public DomainUserModel map(UserDto userDto) {
        domainUserModel.setAccessToken(userDto.getAccessToken());

        return domainUserModel;
    }

    //endregion

}
