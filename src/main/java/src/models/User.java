package src.models;

import java.security.Principal;

public class User implements Principal {

    private int id;
    private String username;
    private String authToken;
    private String email;
    private int privilege;

    public User(int id, String name, String email, int privilege) {
        this.id = id;
        this.username = name;
        this.email = email;
        this.privilege = privilege;
    }

    public User(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String username) {
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    @Override
    public String getName() {
        return this.username;
    }
}
