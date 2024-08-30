package chatapp.carolineeklund.services;

import chatapp.carolineeklund.dtos.ChatDTO;
import chatapp.carolineeklund.dtos.MessageDTO;
import chatapp.carolineeklund.models.Chat;
import chatapp.carolineeklund.models.Message;
import chatapp.carolineeklund.repositories.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    // Get the current user’s ID
    private String getCurrentUserId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername(); //User email extracted as username
    }

    //Get all chats sorted by last message first
    public List<ChatDTO> getAllChats() {
        String currentUserId = getCurrentUserId();

        return chatRepository.findAllByOrderByLastMessageTimeDesc().stream()
                .filter(chat -> chat.getParticipants().contains(currentUserId))
                .map(chat -> {
            ChatDTO chatDTO = new ChatDTO();
            chatDTO.setName(chat.getName());
            chatDTO.setParticipants(chat.getParticipants());
            return chatDTO;
        }).collect(Collectors.toList());
    }

    public String startChat(ChatDTO chatDTO) {
        String currentUserId = getCurrentUserId(); // Make sure this method retrieves the current user ID correctly

        List<String> participants = chatDTO.getParticipants();
        if (participants == null) {
            participants = new ArrayList<>();
        }
        if (!participants.contains(currentUserId)) {
            participants.add(currentUserId);
        }

        Chat chat = new Chat();
        chatDTO.setParticipants(participants); // Ensure that chatDTO has the correct participants list
        chat.setName(chatDTO.getName());
        chat.setParticipants(participants);
        chat.setCreatedAt(LocalDateTime.now());
        chat.setLastMessageTime(LocalDateTime.now());

        chatRepository.save(chat);
        return chat.getId();
    }




    // Add a participant to a chat
    public void addParticipant(String chatId, String userId) {
        Optional<Chat> chatOptional = chatRepository.findById(chatId);
        if (chatOptional.isPresent()) {
            Chat chat = chatOptional.get();

            // Check if user is part of chat
            List <String> participants = chat.getParticipants();
            if (participants.contains(userId)){
                throw new IllegalArgumentException("User ID is already a participant");
            } else {
                chat.getParticipants().add(userId);
                chatRepository.save(chat);
            }
        }
    }

    // Add message to chat
    public void addMessageToChat(String id, MessageDTO messageDTO) {
        Optional<Chat> chatOptional = chatRepository.findById(id);
        if (chatOptional.isPresent()) {
            Chat chat = chatOptional.get();
            Message message = new Message();

            //message.setSenderEmail(messageDTO.getSenderEmail());
            message.setContent(messageDTO.getContent());
            message.setCreatedAt(LocalDateTime.now());

            chat.getMessages().add(message);
            chat.setLastMessageTime(LocalDateTime.now());
            chatRepository.save(chat);
        }
    }

    // Get messages by chat id
    public List<MessageDTO> getMessagesByChatId(String id) {
        Optional<Chat> chatOptional = chatRepository.findById(id);
        List <Message> messages = new ArrayList<>();
        if (chatOptional.isPresent()) {
            Chat chat = chatOptional.get();
            if (chat.getMessages() == null || chat.getMessages().isEmpty()) {
                return new ArrayList<>();
            } else {
                return chat.getMessages().stream().map(message -> {
                    MessageDTO messageDTO = new MessageDTO();
                    messageDTO.setId(message.getId());
                    messageDTO.setSenderEmail(message.getSenderEmail());
                    messageDTO.setContent(message.getContent());
                    messageDTO.setTimestamp(message.getCreatedAt().toString());
                    return messageDTO;
                }).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }
}