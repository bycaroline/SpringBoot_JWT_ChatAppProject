package chatapp.carolineeklund.repositories;

import chatapp.carolineeklund.models.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {
    List<Chat> findAllByOrderByLastMessageTimeDesc(); // Ändra till List<Chat>
}