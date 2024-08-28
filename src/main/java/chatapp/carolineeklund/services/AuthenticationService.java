package chatapp.carolineeklund.services;

import chatapp.carolineeklund.dtos.LoginUserDto;
import chatapp.carolineeklund.dtos.RegisterUserDto;
import chatapp.carolineeklund.models.User;
import chatapp.carolineeklund.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUserDto input) {
        User user = User.builder()
                .fullName(input.getFullName())
                .email(input.getEmail())
                .password(passwordEncoder.encode(input.getPassword()))
                .enabled(true)  // Gör användaren aktiverad
                .accountNonLocked(true)  // Kontot är inte låst
                .accountNonExpired(true)  // Kontot har inte gått ut
                .credentialsNonExpired(true)  // Lösenordet har inte gått ut
                .build();
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        return (User) userRepository.findByEmail(input.getEmail())
                .orElseThrow();

    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean fullNameExists(String fullName) {
        return userRepository.findByFullName(fullName).isPresent();
    }
}



