package src.core;

import io.dropwizard.auth.Authenticator;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.JwtContext;
import src.WebshopApplication;
import src.models.User;
import src.db.UserDAO;
import java.util.Optional;

public class JwtAuthenticator implements Authenticator<JwtContext, User> {
    @Override
    public Optional<User> authenticate(JwtContext jwtContext) {
        try {
            final JwtClaims claims = jwtContext.getJwtClaims();
            if(NumericDate.now().isAfter(claims.getExpirationTime())){
                return Optional.empty();
            }

            UserDAO userDAO = WebshopApplication.jdbiCon.onDemand(UserDAO.class);
            Long userId = (Long)claims.getClaimValue("id");
            User user = userDAO.getUserFromId(userId.intValue());

            return user == null ? Optional.empty() : Optional.of(user);
        } catch(MalformedClaimException e){
            return Optional.empty();
        }
    }
}
