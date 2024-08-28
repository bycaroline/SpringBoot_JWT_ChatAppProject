package chatapp.carolineeklund.controllers;

import chatapp.carolineeklund.dtos.UserDTO;
import chatapp.carolineeklund.models.User;
import chatapp.carolineeklund.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;


@RestController
@RequestMapping("/api/user")  //urlen för endpointen
public class UserController {

    @Autowired
    private UserService userService;

    //sök användare på id
    @PostMapping("/{userId}")
    public UserDTO searchUser(@PathVariable String userId) {
        return userService.searchUserById(userId);  //anropa serviceklass
    }
}

