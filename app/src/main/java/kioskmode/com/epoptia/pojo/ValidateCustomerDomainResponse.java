package kioskmode.com.epoptia.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by giannis on 18/9/2017.
 */

public class ValidateCustomerDomainResponse {

    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("access_token")
    @Expose
    private String access_token;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getAccess_token() { return access_token; }

    public void setAccess_token(String access_token) { this.access_token = access_token; }
}
