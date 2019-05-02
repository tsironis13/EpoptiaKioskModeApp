package kioskmode.com.epoptia.viewmodel.models;

import javax.inject.Inject;

public class AdminDetailsViewModel {

    //region Private Properties

    private String username;

    private String password;

    //endregion

    //region Constructor

    @Inject
    AdminDetailsViewModel() {}

    //endregion

    //region Setters

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //endregion

    //region Getters

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    //endregion

}
