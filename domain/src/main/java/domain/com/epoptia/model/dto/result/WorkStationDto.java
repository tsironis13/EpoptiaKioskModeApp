package domain.com.epoptia.model.dto.result;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorkStationDto {

    //region Private Properties

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    //region Constructor

    public WorkStationDto() {}

    //endregion

    //region Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    //endregion

    //region Getters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    //endregion

}
