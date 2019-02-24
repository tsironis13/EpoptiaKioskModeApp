package domain.com.epoptia.model.dto.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.inject.Inject;

public class ValidateClientSubDomainPostDto extends BasePostDto {

    //region Private Properties

    @SerializedName("customer_domain")
    @Expose
    private String customer_domain;

    //endregion

    //region Constructor

    @Inject
    public ValidateClientSubDomainPostDto() {}

    //endregion

    //region Setters

    public void setClientSubDomain(String clientSubDomain) {
        this.customer_domain = clientSubDomain;
    }

    //endregion

    //region Getters

    public String getClientSubDomain() {
        return customer_domain;
    }

    //endregion

}
