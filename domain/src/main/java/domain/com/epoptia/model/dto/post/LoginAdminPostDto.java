package domain.com.epoptia.model.dto.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.inject.Inject;

public class LoginAdminPostDto extends BasePostDto {

    //region Private Properties

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("customer_domain")
    @Expose
    private String customer_domain;

    //endregion

    //region Constructor

    @Inject
    public LoginAdminPostDto() {}

    //endregion

    //region Setters

    public void setClientSubDomain(String clientSubDomain) {
        this.customer_domain = clientSubDomain;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //endregion

    //region Getters

    public String getClientSubDomain() {
        return customer_domain;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    //endregion

}
