package chatapp.carolineeklund.services;

import chatapp.carolineeklund.dtos.ChatDTO;
import chatapp.carolineeklund.dtos.MessageDTO;
import chatapp.carolineeklund.models.Chat;
import chatapp.carolineeklund.models.Message;
import chatapp.carolineeklund.repositories.ChatRepository;
import chatapp.carolineeklund.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    // Hämta alla chattar sorterade efter senaste meddelande först
    public List<ChatDTO> getAllChats() {
        return chatRepository.findAllByOrderByLastMessageTimeDesc().stream().map(chat -> {
            ChatDTO chatDTO = new ChatDTO();
            chatDTO.setName(chat.getName());
            chatDTO.setParticipants(chat.getParticipants());
            return chatDTO;
        }).collect(Collectors.toList());
    }

    public String startChat(ChatDTO chatDTO) {
        Chat chat = new Chat();
        chat.setName(chatDTO.getName());
        chat.setParticipants(chatDTO.getParticipants());
        chat.setCreatedAt(LocalDateTime.now());
        chat.setLastMessageTime(LocalDateTime.now());
        chatRepository.save(chat);
        return chat.getId();
    }


    // Lägg till deltagare i en chatt
    public void addParticipant(String chatId, String userId) {
        Optional<Chat> chatOptional = chatRepository.findById(chatId);
        if (chatOptional.isPresent()) {
            Chat chat = chatOptional.get();
            chat.getParticipants().add(userId);
            chatRepository.save(chat);
        }
    }

    public Chat getChat(String chatId) {
        return chatRepository.findById(chatId).orElseThrow(() -> new RuntimeException("Chat not found"));
    }

    public void addMessageToChat(String id, MessageDTO messageDTO) {
        Optional<Chat> chatOptional = chatRepository.findById(id);
        if (chatOptional.isPresent()) {
            Chat chat = chatOptional.get();
            Message message = new Message();
            message.setSenderId(messageDTO.getSenderId());
            message.setContent(messageDTO.getContent());
            message.setCreatedAt(LocalDateTime.now());
            chat.getMessages().add(message);
            chat.setLastMessageTime(LocalDateTime.now());
            chatRepository.save(chat);
        }
    }

    public List<MessageDTO> getMessagesByChatId(String id) {
        Optional<Chat> chatOptional = chatRepository.findById(id);
        if (chatOptional.isPresent()) {
            Chat chat = chatOptional.get();
            return chat.getMessages().stream().map(message -> {
                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setSenderId(message.getSenderId());
                messageDTO.setContent(message.getContent());
                return messageDTO;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


}