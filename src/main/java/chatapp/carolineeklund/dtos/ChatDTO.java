package chatapp.carolineeklund.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class ChatDTO {
    private String name;
    private List<String> participants;  // Lägg till detta fält
}

