package domain.com.epoptia.model.dto.result;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponseDto {

    //region Private Properties

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("description")
    @Expose
    private String description;

    //endregion

    //region Constructor

    public BaseResponseDto() {}

    //endregion

    //region Setters

    public void setCode(int code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //endregion

    //region Getters

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    //endregion

}
