package chatapp.carolineeklund.repositories;

import chatapp.carolineeklund.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    //kolla om användare med idt finns vid sökning
    boolean existsById(String id);

    Optional<Object> findByEmail(String username);

    Optional<Object> findByFullName(String fullName);

//    String findUsernameByUserId(String userId);
}