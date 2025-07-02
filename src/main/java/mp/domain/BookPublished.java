package mp.domain;

import java.time.LocalDateTime;
import java.util.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;

@Data
@ToString
public class BookPublished {

    @JsonDeserialize(using = UUIDDeserializer.class)
    @JsonProperty("bookId")
    private UUID bookId;
    
    @JsonDeserialize(using = UUIDDeserializer.class)
    @JsonProperty("authorId")
    private UUID authorId;
    
    private String authorName;
    private String title;
    private int price;
    private String category;
    private String summary;
    private String content;
    private String imageUrl;
    private String audioUrl;
    private Integer todayCount;
    private Integer totalCount;
    private LocalDateTime createdAt;
}
