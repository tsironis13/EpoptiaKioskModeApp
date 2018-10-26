package kioskmode.com.epoptia.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by giannis on 18/9/2017.
 */

public class GetWorkStationsRequest {

    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("access_token")
    @Expose
    private String access_token;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
