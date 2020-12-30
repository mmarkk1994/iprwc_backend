package src.service;

import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import src.WebshopApplication;
import src.models.Product;
import src.models.User;
import src.core.Body;
import src.core.JwtHelper;
import src.db.ProductDAO;
import src.util.MessageUtil;
import src.util.PrivilegeUtil;

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

    public Response addProduct(User authUser, String album, String description, String image, double price) {
        Body body = new Body();
        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.ADD_PRODUCT)){
            return Body.createResponse(body, BAD_REQUEST, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        JwtHelper.renewAuthToken(body, authUser);

        try {
            int productId = productDAO.addProduct(album, description, image, price);
            return (productId != -1) ? Body.createResponse(body, OK, MessageUtil.PRODUCT_CREATED, productId):
                    Body.createResponse(body, BAD_REQUEST, MessageUtil.PRODUCT_OPERATION_FAILED, null);
        } catch (UnableToExecuteStatementException e){
            e.printStackTrace();
            return Body.createResponse(body, BAD_REQUEST, MessageUtil.PRODUCT_ALREADY_EXIST, null);
        }

    }
}
