package kioskmode.com.epoptia.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by thomatou on 9/25/17.
 */

public class ValidateWorkerResponse {

    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("workstation_url")
    @Expose
    private String workstation_url;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getWorkstation_url() {
        return workstation_url;
    }

    public void setWorkstation_url(String workstation_url) {
        this.workstation_url = workstation_url;
    }
}
