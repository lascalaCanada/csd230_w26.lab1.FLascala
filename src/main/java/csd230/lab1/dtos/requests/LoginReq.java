package csd230.lab1.dtos.requests;

/**
 * Data sent by the client to log in. It is only a message, not a database entity.
 */
public class LoginReq {

    private String username;
    private String password;

    // Empty constructor so Jackson can create the object from the JSON body
    public LoginReq() {
    }

    public LoginReq(String username, String password) {
        this.username = username;
        this.password = password;
    }

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
