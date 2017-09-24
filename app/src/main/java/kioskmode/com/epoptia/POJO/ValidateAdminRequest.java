package kioskmode.com.epoptia.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by giannis on 18/9/2017.
 */

public class ValidateAdminRequest {

    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("customer_domain")
    @Expose
    private String customer_domain;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public String getCustomer_domain() {
        return customer_domain;
    }

    public void setCustomer_domain(String customer_domain) {
        this.customer_domain = customer_domain;
    }
}
