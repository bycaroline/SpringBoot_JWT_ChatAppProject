package chatapp.carolineeklund.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {
    private String id;
    private String senderEmail;
    private String content;
    private String timestamp;
}
