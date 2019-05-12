package domain.com.epoptia.model.dto.result;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StationWorkersDto extends BaseResponseDto {

    //region Private Properties

    @SerializedName("workers")
    @Expose
    private List<WorkerDto> workers;

    //endregion

    //region Constructor

    public StationWorkersDto() {}

    //endregion

    //region Setters

    public void setWorkers(List<WorkerDto> workers) {
        this.workers = workers;
    }

    //endregion

    //region Getters

    public List<WorkerDto> getWorkers() {
        return workers;
    }

    //endregion

}
