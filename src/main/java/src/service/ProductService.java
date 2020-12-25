package src.service;

import src.WebshopApplication;
import src.models.Product;
import src.models.User;
import src.core.Body;
import src.core.JwtHelper;
import src.db.ProductDAO;
import src.util.MessageUtil;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

import static javax.ws.rs.core.Response.Status.*;

public class ProductService {
    private final ProductDAO productDAO;

    public ProductService(){
        productDAO = WebshopApplication.jdbiCon.onDemand(ProductDAO.class);
        productDAO.createTable();
    }

    public Response getAllProducts(Optional<User> optionalUser){
        Body body = new Body();

        JwtHelper.renewAuthToken(body, optionalUser);

        List<Product> productList = productDAO.getAllProducts();

        return Body.createResponse(body, OK, MessageUtil.PRODUCT_FOUND, productList);
    }
}
