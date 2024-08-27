package chatapp.carolineeklund.controllers;

import chatapp.carolineeklund.dtos.ChatDTO;
import chatapp.carolineeklund.dtos.MessageDTO;
import chatapp.carolineeklund.models.Chat;
import chatapp.carolineeklund.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public String startChat(@RequestBody ChatDTO chatDTO) {
        // Step 1: Get the current user's username or details
        String currentUsername = getCurrentUserUsername();

        // Step 2: Add the current user to the participants
        List<String> participants = chatDTO.getParticipants();
        if (participants == null) {
            participants = new ArrayList<>();
        }
        if (!participants.contains(currentUsername)) {
            participants.add(currentUsername);
        }
        chatDTO.setParticipants(participants);

        // Step 3: Create the chat
        Chat chat = new Chat();
        chat.setName(chatDTO.getName());
        chat.setParticipants(participants);  // now includes the current user
        chat.setCreatedAt(LocalDateTime.now());
        chat.setLastMessageTime(LocalDateTime.now());

        return chatService.startChat(chatDTO);
    }

    private String getCurrentUserUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString(); // For simplicity, assuming principal is username
        }
    }


//    private int getCurrentUserId() {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal instanceof UserDetails) {  // Assuming you're using a custom implementation
//            return ((UserDetails) principal).getId();
//        } else {
//            throw new IllegalStateException("Principal is not an instance of CustomUserDetails");
//        }
//    }



    @PostMapping("/{id}")
    public String sendMessage(@PathVariable String id, @RequestParam String message) {
        chatService.sendMessage(id, message);
        return "Message sent to chat ID: " + id;
    }

    @PutMapping("/{id}")
    public void addParticipantToChat(@PathVariable String id, @RequestParam String userId) {
        chatService.addParticipant(id, userId);
    }

    @GetMapping("/{id}")
    public List<MessageDTO> getMessagesByChatId(@PathVariable String id) {
        return chatService.getMessagesByChatId(id);
    }
}

