package chatapp.carolineeklund.models;

import lombok.Getter;

import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "messages")
@Getter
@Setter
public class Message {
    @Id
    private String id;
    private String senderId;
    private String content;
    private LocalDateTime createdAt;
}