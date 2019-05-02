package domain.com.epoptia.model.dto.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.inject.Inject;

public class UnlockDevicePostDto extends BasePostDto {

    //region Private Properties

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

    //endregion

    //region Constructor

    @Inject
    public UnlockDevicePostDto() {}

    //endregion

    //region Setters

    public void setAccessToken(String access_token) {
        this.access_token = access_token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCustomerDomain(String customer_domain) {
        this.customer_domain = customer_domain;
    }

    //endregion

    //region Getters

    public String getAccessToken() {
        return access_token;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCustomerDomain() {
        return customer_domain;
    }

    //endregion

}
