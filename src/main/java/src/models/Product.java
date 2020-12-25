package src.models;

public class Product {
    private int id;
    private String album;
    private String description;
    private String image;
    private String price;

    public Product(int id, String album, String description, String image, String price){
        this.id = id;
        this.album = album;
        this.description = description;
        this.image = image;
        this.price = price;
    }

    public Product(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String name) {
        this.album = album;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) { this.price = price; }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
