package chatapp.carolineeklund.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterUserDto {
    private String email;
    private String password;
    private String fullName;
}