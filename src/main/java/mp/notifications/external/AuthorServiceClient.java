package mp.notifications.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;

@FeignClient(name = "authors", url = "${authors.service.url:http://localhost:8081}")
public interface AuthorServiceClient {
    
    @GetMapping("/authors/{authorId}/userId")
    ApiResponse<UserIdResponse> getUserIdByAuthorId(@PathVariable("authorId") UUID authorId);
    
    class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;
        
        public ApiResponse() {}
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public T getData() { return data; }
        public void setData(T data) { this.data = data; }
    }
    
    class UserIdResponse {
        private UUID userId;
        
        public UserIdResponse() {}
        
        public UserIdResponse(UUID userId) {
            this.userId = userId;
        }
        
        public UUID getUserId() { return userId; }
        public void setUserId(UUID userId) { this.userId = userId; }
    }
} 