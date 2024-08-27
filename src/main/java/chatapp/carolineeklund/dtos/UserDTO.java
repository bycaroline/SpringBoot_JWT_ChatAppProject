package chatapp.carolineeklund.dtos;

import lombok.Getter;

@Getter
public class UserDTO {

    // Getter och Setter för id
    private String id;
    // Getter och Setter för username
    private String username;

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
