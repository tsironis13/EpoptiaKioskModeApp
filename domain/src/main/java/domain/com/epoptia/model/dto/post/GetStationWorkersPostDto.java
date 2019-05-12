package domain.com.epoptia.model.dto.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.inject.Inject;

public class GetStationWorkersPostDto extends BasePostDto {

    //region Private Properties

    @SerializedName("access_token")
    @Expose
    private String access_token;

    //todo add workStationPostDto in future ??
    @SerializedName("workstation_id")
    @Expose
    private int workstation_id;

    //endregion

    //region Constructor

    @Inject
    public GetStationWorkersPostDto() {}

    //endregion

    //region Setters

    public void setAccessToken(String access_token) {
        this.access_token = access_token;
    }

    public void setWorkstationId(int workstation_id) {
        this.workstation_id = workstation_id;
    }

    //endregion

    //region Getters

    public String getAccessToken() {
        return access_token;
    }

    public int getWorkstationId() {
        return workstation_id;
    }

    //endregion

}
