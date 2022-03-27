package jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class JwtUtil {

  private final Algorithm algorithm;
  private final JWTVerifier verifier;

  public JwtUtil(String secret) {
    this.algorithm = Algorithm.HMAC256(secret);
    this.verifier = JWT.require(algorithm).build();
  }

  public String create(String username) {
    var issuedAt = new Date();
    var expiresAt = new Date(issuedAt.getTime() + TimeUnit.SECONDS.toMillis(30));
    return JWT.create()
        .withSubject(username)
        .withIssuedAt(issuedAt)
        .withExpiresAt(expiresAt)
        .sign(algorithm);
  }

  public DecodedJWT decode(String token) {
    return verifier.verify(token);
  }

  public boolean validate(String token) {
    var decodedToken = verifier.verify(token);
    var now = new Date();
    return decodedToken.getExpiresAt().after(now);
  }
}
