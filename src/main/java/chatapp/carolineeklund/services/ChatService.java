package chatapp.carolineeklund.services;

import chatapp.carolineeklund.dtos.ChatDTO;
import chatapp.carolineeklund.dtos.MessageDTO;
import chatapp.carolineeklund.models.Chat;
import chatapp.carolineeklund.models.Message;
import chatapp.carolineeklund.repositories.ChatRepository;
import chatapp.carolineeklund.repositories.UserRepository;
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
    private String getCurrentUserName() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername(); //User email extracted as username
    }


    //Get all chats sorted by last message first
    public List<ChatDTO> getAllChats() {
        String currentUserName = getCurrentUserName();

        return chatRepository.findAllByOrderByLastMessageTimeDesc().stream()
                .filter(chat -> chat.getParticipants().contains(currentUserName))
                .map(chat -> {
                    ChatDTO chatDTO = new ChatDTO();
                    chatDTO.setName(chat.getName());
                    chatDTO.setParticipants(chat.getParticipants());
                    return chatDTO;
                }).collect(Collectors.toList());
    }


    public String startChat(ChatDTO chatDTO) {
        String currentUserName = getCurrentUserName();

        List<String> participants = chatDTO.getParticipants();
        if (participants == null) {
            participants = new ArrayList<>();
        }
        if (!participants.contains(currentUserName)) {
            participants.add(currentUserName);
        }

        Chat chat = new Chat();

        chatDTO.setParticipants(participants);
        chat.setName(chatDTO.getName());
        chat.setParticipants(participants);
        chat.setCreatedAt(LocalDateTime.now());
        chat.setLastMessageTime(LocalDateTime.now());

        chatRepository.save(chat);
        return chat.getId();
    }

    // Add a participant to a chat
    public void addParticipant(String chatId, String userEmail) {
        Optional<Chat> chatOptional = chatRepository.findById(chatId);
        if (chatOptional.isPresent()) {
            Chat chat = chatOptional.get();

            List<String> participants = chat.getParticipants();
            String currentUserName = getCurrentUserName();

            // Check if current user is part of chat
            if (!participants.contains(currentUserName)) {
                throw new IllegalArgumentException("User is not a participant of the chat");
            }

            // Check if the user to be added is already a participant
            if (participants.contains(userEmail)) {
                throw new IllegalArgumentException("User is already a participant");
            }

            participants.add(userEmail);
            chatRepository.save(chat);
        }
    }

    // Add message to chat
    public void addMessageToChat(String id, MessageDTO messageDTO) {
        Optional<Chat> chatOptional = chatRepository.findById(id);

        if (chatOptional.isPresent()) {
            Chat chat = chatOptional.get();

            //Check if user is part of chat
            List<String> participants = chat.getParticipants();
            String userName = getCurrentUserName();
            if (!participants.contains(userName)) {
                throw new IllegalArgumentException("User ID is not a participant of the chat");
            } else {
                Message message = new Message();

                String senderEmail = getCurrentUserName();

                message.setSenderEmail(senderEmail);
                message.setContent(messageDTO.getContent());
                message.setCreatedAt(LocalDateTime.now());

                chat.getMessages().add(message);
                chat.setLastMessageTime(LocalDateTime.now());
                chatRepository.save(chat);
            }
        }
    }

    // Get messages by chat id
    public List<MessageDTO> getMessagesByChatId(String id) {
        Optional<Chat> chatOptional = chatRepository.findById(id);
        if (chatOptional.isPresent()) {
            Chat chat = chatOptional.get();

            List<String> participants = chat.getParticipants();
            String userId = getCurrentUserName();

            if (!participants.contains(userId)) {
                throw new IllegalArgumentException("User ID is not a participant of the chat");
            }

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