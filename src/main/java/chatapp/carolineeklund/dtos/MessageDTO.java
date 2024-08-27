package chatapp.carolineeklund.dtos;

import lombok.Getter;

@Getter
public class MessageDTO {
    private String id;
    private String sender;
    private String content;
    private String timestamp;

    // Getter och Setter metoder

    public void setId(String id) {
        this.id = id;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
