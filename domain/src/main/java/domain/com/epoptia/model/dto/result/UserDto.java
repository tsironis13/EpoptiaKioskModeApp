package domain.com.epoptia.model.dto.result;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDto extends BaseResponseDto {

    //region Private Properties

    @SerializedName("access_token")
    @Expose
    private String access_token;

    //endregion

    //region Constructor

    public UserDto() {}

    //endregion

    //region Setters

    public void setAccessToken(String accessToken) {
        this.access_token = accessToken;
    }

    //endregion

    //region Getters

    public String getAccessToken() {
        return access_token;
    }

    //endregion

}
