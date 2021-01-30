package src.core;

import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import src.models.User;
import java.util.Optional;
import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;

public class JwtHelper {
    public static byte[] jwtSecret;

    public static void renewToken(HttpBody httpBody, Optional<User> user) {
        if(!user.isPresent()) {
            return;
        }

        renewToken(httpBody, user.get());
    }

    public static String createToken(int id){
        final JwtClaims claims = new JwtClaims();
        claims.setClaim("id", id);
        claims.setExpirationTimeMinutesInTheFuture(60);
        return createToken(claims);
    }

    public static void renewToken(HttpBody httpBody, User user) {
        httpBody.setToken(createToken(user.getId()));
    }

    private static String createToken(JwtClaims claims){
        final JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(HMAC_SHA256);
        jws.setKey(new HmacKey(jwtSecret));

        try {
            return jws.getCompactSerialization();
        } catch (JoseException e){
            return null;
        }
    }
}
