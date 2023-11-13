package ivanovvasil.u5d5w2Project.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import ivanovvasil.u5d5w2Project.entities.User;
import ivanovvasil.u5d5w2Project.exceptions.AnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTools {

  @Value("${spring.jwt.secret}")
  private String secret;

  public String createToken(User user) {
    try {
      return Jwts.builder().setSubject(String.valueOf(user.getId()))
              .setIssuedAt(new Date(System.currentTimeMillis()))
              .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
              .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
              .compact();
    } catch (Exception e) {
      throw new AnauthorizedException("Invalid acces token");
    }
  }
}
