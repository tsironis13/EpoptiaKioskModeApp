package domain.com.epoptia.mappers;

import javax.inject.Inject;

import domain.com.epoptia.model.dto.result.ResponseWrapperDto;
import domain.com.epoptia.model.dto.result.UserDto;

public class UserDtoToResponseWrapperDtoMapper {

    //region Injections

    @Inject
    ResponseWrapperDto responseWrapperDto;

    //endregion

    //region Constructor

    @Inject
    public UserDtoToResponseWrapperDtoMapper() {}

    //endregion

    //region Public Methods

    public ResponseWrapperDto map(UserDto userDto) {
        responseWrapperDto.setUserDto(userDto);

        return responseWrapperDto;
    }

    //endregion

}
