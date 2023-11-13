package ivanovvasil.u5d5w2Project.controllers;

import ivanovvasil.u5d5w2Project.payloads.UserLoggedTokenDTO;
import ivanovvasil.u5d5w2Project.payloads.UserLoginDTO;
import ivanovvasil.u5d5w2Project.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
public class UserLoginController {
  @Autowired
  private AuthenticationService authenticationService;

  @PostMapping("/login")
  public UserLoggedTokenDTO login(@RequestBody UserLoginDTO body) {
    return new UserLoggedTokenDTO(authenticationService.authenticateUser(body));

  }
}
