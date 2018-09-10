package kioskmode.com.epoptia.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by giannis on 25/9/2017.
 */

public class LogoutWorkerRequest {

    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("access_token")
    @Expose
    private String access_token;
    @SerializedName("user_id")
    @Expose
    private int userId;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAccess_token() {
        return access_token;
    }

    public int getUserId() {  return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
