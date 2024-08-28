package chatapp.carolineeklund.services;

import chatapp.carolineeklund.dtos.UserDTO;
import chatapp.carolineeklund.models.User;
import chatapp.carolineeklund.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    //söka användare på userId
    public UserDTO searchUserById(String userId) {
        if (userRepository.existsById(userId)) {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                UserDTO userDTO = new UserDTO();
                userDTO.setId(user.getId());
                userDTO.setUsername(user.getUsername());
                return userDTO;
            } else {
                return null;
            }
        } else {
            return null;  // Användaren finns inte
        }
    }
}
