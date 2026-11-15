package csd230.lab1.dtos.responses;

/**
 * Data returned after a successful login: the username and the JWT the client must use later.
 */
public class LoginRes {

    private String username;
    private String token;

    public LoginRes(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
