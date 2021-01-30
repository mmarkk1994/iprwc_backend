package src.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import src.WebshopApplication;
import src.models.User;
import src.core.HttpBody;
import src.core.JwtHelper;
import src.db.UserDAO;
import src.util.MessageUtil;
import src.util.PrivilegeUtil;
import src.util.ValidationUtil;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;

public class UserService {
    private final UserDAO userDAO;
    private final int COST = 12;

    public UserService(){
        userDAO = WebshopApplication.jdbiCon.onDemand(UserDAO.class);
        userDAO.createTable();
    }

    public Response login(String username, String password) {
        HttpBody httpBody = new HttpBody();
        String pass = userDAO.getPasswordFromUsername(username);

        if(pass == null){
            return HttpBody.createResponse(httpBody, Response.Status.BAD_REQUEST, MessageUtil.EMAIL_PASS_INVALID_COMBI, null);
        }

        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), pass);

        if(!result.validFormat || !result.verified){
            return HttpBody.createResponse(httpBody, Response.Status.BAD_REQUEST, MessageUtil.EMAIL_PASS_INVALID_COMBI, null);
        }

        User user = userDAO.getUserFromUsername(username);
        httpBody.setContent(user);
        httpBody.setMessage(MessageUtil.LOGIN_OK);
        user.setToken(JwtHelper.createToken(user.getId()));
        return httpBody.build();
    }

    public Response register(String username, String email, String password, String streetAddress, String postalCode, String province){
        HttpBody httpBody = new HttpBody();

        if(!ValidationUtil.checkIfEmailIsValid(email)){
            return HttpBody.createResponse(httpBody, Response.Status.BAD_REQUEST, MessageUtil.EMAIL_NOT_VALID, null);
        }

        if(!ValidationUtil.checkLengthPassword(password)){
            return HttpBody.createResponse(httpBody, Response.Status.BAD_REQUEST, MessageUtil.PASSWORD_LENGHT_TO_SHORT, null);
        }

        String hashedPass = BCrypt.withDefaults().hashToString(COST, password.toCharArray());

        return createUser(username, email, hashedPass, streetAddress, postalCode, province, httpBody);
    }

    private Response createUser(String username, String email, String hashedPass,
                                String streetAddress, String postalCode, String province, HttpBody httpBody) {
        try {
            int id = userDAO.createUser(username, email, hashedPass, streetAddress, postalCode, province);
            User user = userDAO.getUserFromId(id);
            return HttpBody.createResponse(httpBody, Response.Status.OK, MessageUtil.ACCOUNT_CREATED, user);
        } catch (UnableToExecuteStatementException e) {
            return HttpBody.createResponse(httpBody, Response.Status.BAD_REQUEST, MessageUtil.EMAIL_ALREADY_EXISTS, null);
        }
    }

    public Response editProfile(User authUser, int id, String email, String streetAddress, String postalCode, String province){
        HttpBody httpBody = new HttpBody();
        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.UPDATE_PROFILE)){
            return HttpBody.createResponse(httpBody, BAD_REQUEST, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        JwtHelper.renewToken(httpBody, authUser);

        boolean updated = userDAO.editProfile(id, email, streetAddress, postalCode, province);
        return updated ? HttpBody.createResponse(httpBody, OK, MessageUtil.PROFILE_UPDATED, null)
                : HttpBody.createResponse(httpBody, BAD_REQUEST, MessageUtil.PROFILE_UPDATE_FAILED, null);

    }

    public Response getUser(User authUser, int id){
        HttpBody httpBody = new HttpBody();

        if(checkUserId(authUser, id)){
            return HttpBody.createResponse(httpBody, Response.Status.NOT_FOUND, MessageUtil.USER_NOT_FOUND, null);
        }

        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.CHECK_COSTUMER_PROFILE)){
            return HttpBody.createResponse(httpBody, Response.Status.BAD_REQUEST, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        JwtHelper.renewToken(httpBody, authUser);

        User user = userDAO.getUserFromId(id);

        if(user == null){
            return HttpBody.createResponse(httpBody, Response.Status.NOT_FOUND, MessageUtil.USER_NOT_FOUND, null);
        }
        httpBody.setStatus(Response.Status.OK);
        httpBody.setContent(user);
        return httpBody.build();
    }

    private boolean checkUserId(User authUser, int id){
        return authUser.getId() != id;
    }
}
