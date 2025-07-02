package mp.domain;

import java.util.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;

@Data
@ToString
public class AuditCompleted {

    @JsonDeserialize(using = UUIDDeserializer.class)
    private UUID id;
    
    @JsonDeserialize(using = UUIDDeserializer.class)
    @JsonProperty("userId")
    private UUID userId;
    
    private String info;
    private String status;
    private String portfolioUrl;
}
