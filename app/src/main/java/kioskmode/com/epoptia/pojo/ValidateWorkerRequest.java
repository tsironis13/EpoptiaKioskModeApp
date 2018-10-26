package kioskmode.com.epoptia.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by thomatou on 9/25/17.
 */

public class ValidateWorkerRequest {

    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("access_token")
    @Expose
    private String access_token;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("workstation_id")
    @Expose
    private int station_id;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStation_id() { return station_id; }

    public void setStation_id(int station_id) { this.station_id = station_id; }
}
