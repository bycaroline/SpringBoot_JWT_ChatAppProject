package chatapp.carolineeklund.services;

import chatapp.carolineeklund.dtos.LoginUserDto;
import chatapp.carolineeklund.dtos.RegisterUserDto;
import chatapp.carolineeklund.models.User;
import chatapp.carolineeklund.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for handling user authentication and registration.
 */
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructs the AuthenticationService with required dependencies.
     *
     * @param userRepository the repository to access user data.
     * @param authenticationManager the manager to authenticate user credentials.
     * @param passwordEncoder the encoder to securely hash user passwords.
     */
    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user with the provided details.
     *
     * @param input the registration details encapsulated in a RegisterUserDto object.
     * @return the newly created user.
     */
    public User signup(RegisterUserDto input) {
        User user = User.builder()
                .fullName(input.getFullName())
                .email(input.getEmail())
                .password(passwordEncoder.encode(input.getPassword()))
                .enabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .build();
        return userRepository.save(user);
    }

    /**
     * Authenticates a user using their login credentials.
     *
     * @param input the login credentials encapsulated in a LoginUserDto object.
     * @return the authenticated user.
     */
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

