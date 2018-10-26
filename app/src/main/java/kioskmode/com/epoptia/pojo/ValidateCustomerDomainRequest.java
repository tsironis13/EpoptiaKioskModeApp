package kioskmode.com.epoptia.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by giannis on 18/9/2017.
 */

public class ValidateCustomerDomainRequest {

    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("customer_domain")
    @Expose
    private String customer_domain;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCustomer_domain() {
        return customer_domain;
    }

    public void setCustomer_domain(String customer_domain) {
        this.customer_domain = customer_domain;
    }
}
