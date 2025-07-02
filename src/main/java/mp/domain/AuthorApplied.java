package mp.domain;

import java.util.*;
import lombok.*;

@Data
@ToString
public class AuthorApplied {

    private UUID id;
    private UUID userId;
    private String info;
    private String status;
    private String portfolioUrl;
    private String name;
    private String bio;
}
