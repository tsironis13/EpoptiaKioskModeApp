package kioskmode.com.epoptia.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by giannis on 28/9/2017.
 */

public class UnlockDeviceRequest {

    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("access_token")
    @Expose
    private String access_token;
    @SerializedName("customer_domain")
    @Expose
    private String customer_domain;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;

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

    public String getCustomer_domain() {
        return customer_domain;
    }

    public void setCustomer_domain(String customer_domain) {
        this.customer_domain = customer_domain;
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
}
