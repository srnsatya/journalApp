package spring.practice.oct25.repos;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import spring.practice.oct25.entities.User;

import java.util.Optional;

public interface UserRepo extends MongoRepository<User, ObjectId> {

    public Optional<User> findByUserName(String userName);

    public void deleteByUserName(String userName);

}
