package kioskmode.com.epoptia.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by giannis on 18/9/2017.
 */

public class GetWorkersResponse {

    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("workers")
    @Expose
    private List<StationWorker> workers;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<StationWorker> getData() {
        return workers;
    }

    public void setData(List<StationWorker> workers) {
        this.workers = workers;
    }

}
