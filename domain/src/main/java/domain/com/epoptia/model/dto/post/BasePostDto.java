package domain.com.epoptia.model.dto.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.inject.Inject;

public class BasePostDto {

    //region Private Properties

    @SerializedName("action")
    @Expose
    private String action;

    //endregion

    //region Constructor

    @Inject
    public BasePostDto() {}

    //endregion

    //region Setters

    public void setAction(String action) {
        this.action = action;
    }

    //endregion

    //region Getters

    public String getAction() {
        return action;
    }

    //endregion

}
