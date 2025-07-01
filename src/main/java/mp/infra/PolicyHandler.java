// notification\src\main\java\mp\infra\PolicyHandler.java

package mp.notifications.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import mp.config.kafka.KafkaProcessor;
import mp.notifications.domain.Notification;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
public class PolicyHandler {

    @StreamListener(KafkaProcessor.INPUT)
    public void onMessage(@Payload NotificationRequested event) {
        if (event.getUserId() == null || event.getMessage() == null) return;

        Notification notification = new Notification();
        notification.setUserId(event.getUserId());
        notification.setMessage(event.getMessage());
        Notification.repository().save(notification);
    }

    @Data
    public static class NotificationRequested {
        private UUID userId;
        private String message;
    }
}
