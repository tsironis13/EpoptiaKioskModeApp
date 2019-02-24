package domain.com.epoptia.model.dto.result;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponseDto {

    //region Private Properties

    @SerializedName("code")
    @Expose
    private int code;

    //endregion

    //region Constructor

    public BaseResponseDto() {}

    //endregion

    //region Setters

    public void setCode(int code) {
        this.code = code;
    }

    //endregion

    //region Getters

    public int getCode() {
        return code;
    }

    //endregion

}
