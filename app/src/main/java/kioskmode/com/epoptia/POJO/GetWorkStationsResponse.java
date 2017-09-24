package kioskmode.com.epoptia.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by giannis on 18/9/2017.
 */

public class GetWorkStationsResponse {

    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("workstations")
    @Expose
    private List<WorkStation> workstations;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<WorkStation> getData() {
        return workstations;
    }

    public void setData(List<WorkStation> workstations) {
        this.workstations = workstations;
    }
}
