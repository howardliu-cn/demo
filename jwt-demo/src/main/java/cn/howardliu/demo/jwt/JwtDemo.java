package cn.howardliu.demo.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <br>created at 17-11-9
 *
 * @author liuxh
 * @since 1.0.0
 */
public class JwtDemo {
    private static final Logger logger = LoggerFactory.getLogger(JwtDemo.class);

    public static void main(String[] args) throws Exception {
        Algorithm algorithm = Algorithm.HMAC256("secret");

        String token = JWT.create().withIssuer("auth0").sign(algorithm);
        System.out.println(token);

        JWTVerifier verifier = JWT.require(algorithm).withIssuer("auth0").build();
        try {
            DecodedJWT jwt = verifier.verify(token);
            System.out.println("header: " + new String(Base64.decodeBase64(jwt.getHeader()), UTF_8));
            System.out.println("payload: " + new String(Base64.decodeBase64(jwt.getPayload()), UTF_8));
            System.out.println("signature: " + jwt.getSignature());
            System.out.println("token: " + jwt.getToken());
        } catch (JWTVerificationException e) {
            System.err.println("Invalid signature/claims");
        }
    }
}
