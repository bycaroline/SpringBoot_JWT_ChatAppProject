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

    // Skicka meddelande till specifik chatt
    public void sendMessage(String chatId, String content) {
        Optional<Chat> chatOptional = chatRepository.findById(chatId);
        if (chatOptional.isPresent()) {
            Chat chat = chatOptional.get();
            Message message = new Message();
            message.setContent(content);
            message.setSender("sender");  // Placeholder för avsändare, kan justeras
            message.setTimestamp(LocalDateTime.now().toString());
            messageRepository.save(message);
            chat.getMessages().add(message);
            chat.setLastMessageTime(LocalDateTime.now());
            chatRepository.save(chat);
        }
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

    // Läs meddelanden från en specifik chatt
//    public List<MessageDTO> getMessagesByChatId(String chatId) {
//        Optional<Chat> chatOptional = chatRepository.findById(chatId);
//        if (chatOptional.isPresent()) {
//            return chatOptional.get().getMessages().stream().map(message -> {
//                MessageDTO messageDTO = new MessageDTO();
//                messageDTO.setId(message.getId());
//                messageDTO.setSender(message.getSender());
//                messageDTO.setContent(message.getContent());
//                messageDTO.setTimestamp(message.getTimestamp());
//                return messageDTO;
//            }).collect(Collectors.toList());
//        }
//        return null;
//    }

//    public List<MessageDTO> getMessagesByChatId(String chatId) {
//        Optional<Chat> chatOptional = chatRepository.findById(chatId);
//        if (chatOptional.isPresent()) {
//            List<Message> messages = chatOptional.get().getMessages();
//            if (messages.isEmpty()) {
//                return new ArrayList<>();  // Return an empty list if there are no messages
//            }
//            return messages.stream().map(message -> {
//                MessageDTO messageDTO = new MessageDTO();
//                messageDTO.setId(message.getId());
//                messageDTO.setSender(message.getSender());
//                messageDTO.setContent(message.getContent());
//                messageDTO.setTimestamp(message.getTimestamp());
//                return messageDTO;
//            }).collect(Collectors.toList());
//        }
//        return new ArrayList<>();  // Return an empty list if the chat doesn't exist
//    }

    public List<MessageDTO> getMessagesByChatId(String chatId) {
        Optional<Chat> chatOptional = chatRepository.findById(chatId);
        if (chatOptional.isPresent()) {
            List<Message> messages = chatOptional.get().getMessages();
            if (messages == null || messages.isEmpty()) {
                return new ArrayList<>();  // Return an empty list if messages are null or empty
            }
            return messages.stream().map(message -> {
                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setId(message.getId());
                messageDTO.setSender(message.getSender());
                messageDTO.setContent(message.getContent());
                messageDTO.setTimestamp(message.getTimestamp());
                return messageDTO;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();  // Return an empty list if the chat doesn't exist
    }
}