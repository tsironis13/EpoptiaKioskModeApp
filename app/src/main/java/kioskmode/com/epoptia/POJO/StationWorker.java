package kioskmode.com.epoptia.POJO;

/**
 * Created by giannis on 5/9/2017.
 */

public class StationWorker {

    private int userId;
    private String name;
    private String username;
    private String password;

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }


    public int getUserId() {  return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
