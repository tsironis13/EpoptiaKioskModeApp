package domain.com.epoptia.model.dto.result;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorkerDto {

    //region Private Properties

    @SerializedName("userId")
    @Expose
    private int userId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    //region Constructor

    public WorkerDto() {}

    //endregion

    //region Setters

    public void setId(int id) {
        this.userId = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    //endregion

    //region Getters

    public int getId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    //endregion

}
