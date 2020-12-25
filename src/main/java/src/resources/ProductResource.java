package src.resources;

import src.models.User;
import src.service.ProductService;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/product")
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {
    private final ProductService productService;

    public ProductResource(){
        this.productService = new ProductService();
    }

    @Path("/all")
    @GET
    public Response getAllProducts(@Auth Optional<User> optionalUser){
        return productService.getAllProducts(optionalUser);
    }
}
