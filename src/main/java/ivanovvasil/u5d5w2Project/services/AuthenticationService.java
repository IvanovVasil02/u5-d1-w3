package ivanovvasil.u5d5w2Project.services;

import ivanovvasil.u5d5w2Project.entities.User;
import ivanovvasil.u5d5w2Project.exceptions.AnauthorizedException;
import ivanovvasil.u5d5w2Project.payloads.UserLoginDTO;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthenticationService {
  @Autowired
  UsersService usersService;

  public String authenticateUser(UserLoginDTO body) {
    User userFound = usersService.findByEmail(body.email());
    if (userFound.getPassword().equals(body.password())) {
      return null;
    } else {
      throw new AnauthorizedException("Invalid credentials");
    }
  }
}
