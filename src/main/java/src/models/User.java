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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getEmail() {
        return email;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getProvince() {
        return province;
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
