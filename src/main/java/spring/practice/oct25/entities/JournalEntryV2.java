package spring.practice.oct25.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection =  "journal_entries")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class JournalEntryV2 {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    @NonNull
    @Indexed(unique = true)
    private String title;
    private String content;
    private LocalDateTime date;

    public JournalEntryV2(ObjectId id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public JournalEntryV2(String title, String content) {
        this.title = title;
        this.content = content;
    }


}
