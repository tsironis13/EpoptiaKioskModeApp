package domain.com.epoptia.model.dto.result;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WorkStationsDto extends BaseResponseDto {

    //region Private Properties

    @SerializedName("workstations")
    @Expose
    private List<WorkStationDto> workstations;

    //endregion

    //region Constructor

    public WorkStationsDto() {}

    //endregion

    //region Setters


    public void setWorkstations(List<WorkStationDto> workstations) {
        this.workstations = workstations;
    }

    //endregion

    //region Getters

    public List<WorkStationDto> getWorkstations() {
        return workstations;
    }

    //endregion

}
