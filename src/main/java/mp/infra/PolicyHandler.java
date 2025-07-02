// notification\src\main\java\mp\infra\PolicyHandler.java

package mp.notifications.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import mp.config.kafka.KafkaProcessor;
import mp.notifications.domain.Notification;
import mp.notifications.external.AuthorServiceClient;
import mp.domain.BookPublished;
import mp.domain.AuditCompleted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
public class PolicyHandler {

    @Autowired
    private AuthorServiceClient authorServiceClient;

    // 일반 알림 요청 처리 (필요시 별도 채널 추가)
    // @StreamListener("notification-input")
    // public void onMessage(@Payload NotificationRequested event) {
    //     if (event.getUserId() == null || event.getMessage() == null) return;
    //
    //     Notification notification = new Notification();
    //     notification.setUserId(event.getUserId());
    //     notification.setMessage(event.getMessage());
    //     Notification.repository().save(notification);
    // }

    @StreamListener(KafkaProcessor.BOOK_PUBLISHED_IN)
    public void onBookPublished(@Payload BookPublished event) {
        System.out.println("BookPublished event received: " + event);
        
        if (event.getAuthorId() == null || event.getTitle() == null) return;

        try {
            // authorId로 userId 조회
            UUID authorId = event.getAuthorId();
            AuthorServiceClient.ApiResponse<AuthorServiceClient.UserIdResponse> response = 
                authorServiceClient.getUserIdByAuthorId(authorId);
            
            if (!response.isSuccess() || response.getData() == null) {
                System.err.println("Failed to get userId for authorId: " + authorId);
                return;
            }
            
            UUID userId = response.getData().getUserId();
            
            // userId로 알림 생성
            Notification notification = new Notification();
            notification.setUserId(userId);
            notification.setMessage(String.format("🎉 축하합니다! '%s' 책이 출간되었습니다!", event.getTitle()));
            Notification.repository().save(notification);
            
            System.out.println("Book published notification sent to userId: " + userId + " (authorId: " + authorId + ") for book: " + event.getTitle());
            
        } catch (Exception e) {
            System.err.println("Failed to create book published notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.AUTHOR_REVIEW_IN)
    public void onAuditCompleted(@Payload AuditCompleted event) {
        System.out.println("AuditCompleted event received: " + event);
        
        if (event.getUserId() == null || event.getStatus() == null) return;

        try {
            UUID userId = event.getUserId();

            String message;
            if ("APPROVED".equals(event.getStatus())) {
                message = "🎉 축하합니다! 작가 심사가 승인되었습니다. 이제 원고를 등록하실 수 있습니다.";
            } else if ("REJECTED".equals(event.getStatus())) {
                message = "😔 죄송합니다. 작가 심사가 거절되었습니다. 포트폴리오를 보완한 후 다시 지원해주세요.";
            } else {
                message = "작가 심사 결과: " + event.getStatus();
            }

            // userId로 알림 생성
            Notification notification = new Notification();
            notification.setUserId(userId);
            notification.setMessage(message);
            Notification.repository().save(notification);
            
        } catch (Exception e) {
            System.err.println("Failed to create author review notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Data
    public static class NotificationRequested {
        private UUID userId;
        private String message;
    }
}
