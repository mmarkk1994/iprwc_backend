package src.service;

import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import src.WebshopApplication;
import src.models.Product;
import src.models.User;
import src.core.HttpBody;
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
        HttpBody httpBody = new HttpBody();

        JwtHelper.renewToken(httpBody, optionalUser);

        List<Product> productList = productDAO.getAllProducts();

        return HttpBody.createResponse(httpBody, OK, MessageUtil.PRODUCT_FOUND, productList);
    }

    public Response addProduct(User authUser, String album, String description, String image, double price) {
        HttpBody httpBody = new HttpBody();
        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.ADD_PRODUCT)) {
            return HttpBody.createResponse(httpBody, BAD_REQUEST, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        JwtHelper.renewToken(httpBody, authUser);

        try {
            int productId = productDAO.addProduct(album, description, image, price);
            return (productId != -1) ? HttpBody.createResponse(httpBody, OK, MessageUtil.PRODUCT_CREATED, productId):
                    HttpBody.createResponse(httpBody, BAD_REQUEST, MessageUtil.PRODUCT_OPERATION_FAILED, null);
        } catch (UnableToExecuteStatementException e) {
            e.printStackTrace();
            return HttpBody.createResponse(httpBody, BAD_REQUEST, MessageUtil.PRODUCT_ALREADY_EXIST, null);
        }
    }

    public Response editProduct(User authUser, int id, String album, String description, double price){
        HttpBody httpBody = new HttpBody();
        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.UPDATE_PRODUCT)) {
            return HttpBody.createResponse(httpBody, BAD_REQUEST, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        JwtHelper.renewToken(httpBody, authUser);

        try {
            boolean updated = productDAO.editProduct(album, description, price, id);
            return updated ? HttpBody.createResponse(httpBody, OK, MessageUtil.PRODUCT_UPDATED, null)
                    : HttpBody.createResponse(httpBody, BAD_REQUEST, MessageUtil.PRODUCT_OPERATION_FAILED, null);
        } catch (UnableToExecuteStatementException e) {
            return HttpBody.createResponse(httpBody, BAD_REQUEST, MessageUtil.PRODUCT_ALREADY_EXIST, null);
        }
    }

    public Response deleteProduct(User authUser, int id) {
        HttpBody httpBody = new HttpBody();
        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.DELETE_PRODUCT)) {
            HttpBody.createResponse(httpBody, BAD_REQUEST, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        JwtHelper.renewToken(httpBody, authUser);

        Product product = productDAO.getProduct(id);
        if(product == null) {
            HttpBody.createResponse(httpBody, NOT_FOUND, MessageUtil.PRODUCT_NOT_FOUND, null);
        }

        try {
            boolean deleted = productDAO.deleteProduct(id);
            return deleted ? HttpBody.createResponse(httpBody, OK, MessageUtil.PRODUCT_DELETED, null)
                    : HttpBody.createResponse(httpBody, BAD_REQUEST, MessageUtil.PRODUCT_OPERATION_FAILED, null);
        } catch (UnableToExecuteStatementException e) {
            return HttpBody.createResponse(httpBody, BAD_REQUEST, MessageUtil.PRODUCT_NOT_FOUND, null);
        }
    }

    public Response getProduct(int id, Optional<User> optionalUser) {
        HttpBody httpBody = new HttpBody();

        JwtHelper.renewToken(httpBody, optionalUser);

        Product product = productDAO.getProduct(id);

        if(product == null) {
            return HttpBody.createResponse(httpBody, Response.Status.NOT_FOUND, MessageUtil.PRODUCT_NOT_FOUND, null);
        }

        return HttpBody.createResponse(httpBody, OK, MessageUtil.PRODUCT_FOUND, product);
    }
}
