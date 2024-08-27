package chatapp.carolineeklund.controllers;

import chatapp.carolineeklund.dtos.LoginUserDto;
import chatapp.carolineeklund.dtos.RegisterUserDto;
import chatapp.carolineeklund.models.LoginResponse;
import chatapp.carolineeklund.models.User;
import chatapp.carolineeklund.services.AuthenticationService;
import chatapp.carolineeklund.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/user")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        if (registerUserDto == null ||
                registerUserDto.getEmail() == null ||
                registerUserDto.getPassword() == null ||
                registerUserDto.getEmail().isEmpty() ||
                registerUserDto.getPassword().isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        if (authenticationService.emailExists(registerUserDto.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        if (authenticationService.fullNameExists(registerUserDto.getFullName())){
            return ResponseEntity.badRequest().build();
        }

        User registredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
