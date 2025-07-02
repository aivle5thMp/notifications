package mp.domain;

import java.time.LocalDateTime;
import java.util.*;
import lombok.*;

@Data
@ToString
public class BookPublished {

    private UUID bookId;
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
