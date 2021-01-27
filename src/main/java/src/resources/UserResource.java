package src.resources;

import io.dropwizard.auth.Auth;
import src.models.User;
import src.service.UserService;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserService userService;

    public UserResource(){
        userService = new UserService();
    }

    @Path("/register")
    @POST
    public Response register(@FormParam("username") @NotNull String username, @FormParam("email") @NotNull String email,
                             @FormParam("password") @NotNull String password, @FormParam("streetAddress") @NotNull String streetAddress,
                             @FormParam("postalCode") @NotNull String postalCode, @FormParam("province") @NotNull String province){
        return userService.register(username, email, password, streetAddress, postalCode, province);
    }

    @Path("/login")
    @POST
    public Response login(@FormParam("username") @NotNull String username,
                          @FormParam("password") @NotNull String password){

        return userService.login(username, password);
    }

    @Path("/profile/{id}")
    @GET
    public Response getUserProfile(@Auth User authUser, @PathParam("id") int id){
        return userService.getUser(authUser, id);
    }

    @Path("/profile/{id}/edit")
    @PUT
    public Response editProfile(@Auth User authUser, @PathParam("id") int id, @FormParam("email") String email,
                                @FormParam("streetAddress") String streetAddress, @FormParam("postalCode") String postalCode,
                                @FormParam("province") String province){
        return userService.editProfile(authUser, id, email, streetAddress, postalCode, province);
    }
}
