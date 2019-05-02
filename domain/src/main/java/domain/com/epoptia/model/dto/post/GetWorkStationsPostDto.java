package domain.com.epoptia.model.dto.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.inject.Inject;

public class GetWorkStationsPostDto extends BasePostDto {

    //region Private Properties

    @SerializedName("access_token")
    @Expose
    private String access_token;

    //endregion

    //region Constructor

    @Inject
    public GetWorkStationsPostDto() {}

    //endregion

    //region Setters

    public void setAccessToken(String access_token) {
        this.access_token = access_token;
    }

    //endregion

    //region Getters

    public String getAccessToken() {
        return access_token;
    }

    //endregion

}
