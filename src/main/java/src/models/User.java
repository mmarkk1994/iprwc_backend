package src.models;

import java.security.Principal;

public class User implements Principal {

    private int id;
    private String username;
    private String authToken;
    private String email;
    private String streetAddress;
    private String postalCode;
    private String province;
    private int privilege;

    public User(int id, String name, String email, String streetAddress, String postalCode, String province, int privilege) {
        this.id = id;
        this.username = name;
        this.email = email;
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.province = province;
        this.privilege = privilege;
    }

    public User(){}

    @Override
    public String getName() {
        return this.username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public int getPrivilege() { return privilege; }
}
