package src.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import src.WebshopApplication;
import src.models.User;
import src.core.Body;
import src.core.JwtHelper;
import src.db.UserDAO;
import src.util.MessageUtil;
import src.util.PrivilegeUtil;

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
        Body body = new Body();
        String pass = userDAO.getPasswordFromUsername(username);

        if(pass == null){
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.EMAIL_PASS_INVALID_COMBI, null);
        }

        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), pass);
        if(!result.validFormat || !result.verified){
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.EMAIL_PASS_INVALID_COMBI, null);
        }

        User user = userDAO.getUserFromUsername(username);
        body.setContent(user);
        body.setMessage(MessageUtil.LOGIN_OK);
        user.setAuthToken(JwtHelper.createAuthToken(user.getId()));
        return body.build();
    }

    public Response register(String username, String email, String password, String streetAddress, String postalCode, String province){
        Body body = new Body();
        if(!checkIfEmailIsValid(email)){
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.EMAIL_NOT_VALID, null);
        }

        if(!checkPasswordLength(password)){
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.PASSWORD_LENGHT_TO_SHORT, null);
        }

        String hashedPass = BCrypt.withDefaults().hashToString(COST, password.toCharArray());

        return createUser(username, email, hashedPass, streetAddress, postalCode, province, body);
    }

    private Response createUser(String username, String email, String hashedPass,
                                String streetAddress, String postalCode, String province, Body body) {
        try {
            int id = userDAO.createUser(username, email, hashedPass, streetAddress, postalCode, province);
            User user = userDAO.getUserFromId(id);
            return Body.createResponse(body, Response.Status.OK, MessageUtil.ACCOUNT_CREATED, user);
        } catch (UnableToExecuteStatementException e) {
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.EMAIL_ALREADY_EXISTS, null);
        }
    }

    public Response editProfile(User authUser, int id, String email, String streetAddress, String postalCode, String province){
        Body body = new Body();
        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.UPDATE_PROFILE)){
            return Body.createResponse(body, BAD_REQUEST, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        JwtHelper.renewAuthToken(body, authUser);

        boolean updated = userDAO.editProfile(id, email, streetAddress, postalCode, province);
        return updated ? Body.createResponse(body, OK, MessageUtil.PROFILE_UPDATED, null)
                : Body.createResponse(body, BAD_REQUEST, MessageUtil.PROFILE_UPDATE_FAILED, null);

    }

    private boolean checkIfEmailIsValid(String email){
        return email.matches("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
    }

    private boolean checkPasswordLength(String password){
        return password.length() >= 6;
    }

    public Response getUser(User authUser, int id){
        Body body = new Body();

        if(checkUserId(authUser, id)){
            return Body.createResponse(body, Response.Status.NOT_FOUND, MessageUtil.USER_NOT_FOUND, null);
        }

        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.CHECK_USER_PROFILE)){
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        JwtHelper.renewAuthToken(body, authUser);

        User user = userDAO.getUserFromId(id);
        if(user == null){
            return Body.createResponse(body, Response.Status.NOT_FOUND, MessageUtil.USER_NOT_FOUND, null);
        }
        body.setStatus(Response.Status.OK);
        body.setContent(user);
        return body.build();
    }

    private boolean checkUserId(User authUser, int id){
        return authUser.getId() != id;
    }
}
