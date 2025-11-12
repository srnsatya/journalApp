package spring.practice.oct25.repos;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import spring.practice.oct25.entities.JournalEntryV2;

public interface JournalEntryRepo extends MongoRepository<JournalEntryV2, ObjectId> {

}
