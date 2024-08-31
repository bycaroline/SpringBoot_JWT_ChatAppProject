package chatapp.carolineeklund.controllers;

import chatapp.carolineeklund.dtos.ChatDTO;
import chatapp.carolineeklund.dtos.MessageDTO;
import chatapp.carolineeklund.models.Chat;
import chatapp.carolineeklund.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping
    public List<ChatDTO> getAllChats() {
        return chatService.getAllChats();
    }

    private String getCurrentUserId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername(); //User email extracted as username
    }

    @PostMapping
    public String startChat(@RequestBody ChatDTO chatDTO) {
        String currentUserId = getCurrentUserId(); // Retrieve the current user ID

        List<String> participants = chatDTO.getParticipants();
        if (participants == null) {
            participants = new ArrayList<>();
        }
        if (!participants.contains(currentUserId)) {
            participants.add(currentUserId);
        }
        chatDTO.setParticipants(participants);

        return chatService.startChat(chatDTO);
    }
    @PostMapping("/{id}")
    public void addMessageToChat(@PathVariable String id, @RequestBody MessageDTO messageDTO) {
        chatService.addMessageToChat(id, messageDTO);
    }

    @PutMapping("/{id}")
    public void addParticipantToChat(@PathVariable String id, @RequestParam String userEmail) {
        chatService.addParticipant(id, userEmail);
    }

    @GetMapping("/{id}")
    public List<MessageDTO> getMessagesByChatId(@PathVariable String id) {
        return chatService.getMessagesByChatId(id);
    }
}

