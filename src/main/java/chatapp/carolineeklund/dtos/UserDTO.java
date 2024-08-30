package chatapp.carolineeklund.dtos;

import lombok.Getter;

@Getter
public class UserDTO {

    private String id;
    private String username;

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
