package domain.com.epoptia.model.dto.result;

import javax.inject.Inject;

public class ResponseWrapperDto {

    //region Private Properties

    private UserDto userDto;

    //endregion

    //region Constructor

    @Inject
    public ResponseWrapperDto() {}

    //endregion

    //region Setters

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    //endregion

    //region Getters

    public UserDto getUserDto() {
        return userDto;
    }

    //endregion

}
