package src.models;

import java.sql.Timestamp;

public class Order {
    public int id;
    public String username;
    public String album;
    public Timestamp orderDate;

    public Order() {}

    public Order(int id, String username, String album, Timestamp orderDate) {
        this.id = id;
        this.username = username;
        this.album = album;
        this.orderDate = orderDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }
}
