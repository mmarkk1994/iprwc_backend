package src.resources;

import src.models.User;
import src.service.ProductService;
import io.dropwizard.auth.Auth;

import javax.validation.constraints.NotNull;
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

    @Path("/{id}")
    @GET
    public Response getProduct(@PathParam("id") int id, @Auth Optional<User> optionalUser){
        return productService.getProduct(id, optionalUser);
    }

    @Path("/add")
    @POST
    public Response addProduct(@Auth User authUser, @FormParam("album") @NotNull String album,
                               @FormParam("description") @NotNull String description,
                               @FormParam("image") @NotNull String image,
                               @FormParam("price") @NotNull double price){
        return productService.addProduct(authUser, album, description, image, price);
    }

    @Path("/delete/{id}")
    @DELETE
    public Response deleteProduct(@Auth User authUser, @PathParam("id") int id){
        return productService.deleteProduct(authUser, id);
    }

    @Path("/edit/{id}")
    @PUT
    public Response editProduct(@Auth User authUser, @PathParam("id") int id,
                                  @FormParam("album") @NotNull String album,
                                  @FormParam("description") @NotNull String description,
                                  @FormParam("price") @NotNull double price){
        return productService.editProduct(authUser, id, album, description, price);
    }
}
