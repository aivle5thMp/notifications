// notification\src\main\java\mp\infra\NotificationController.java
package mp.notifications.infra;

import lombok.Data;
import mp.notifications.domain.Notification;
import mp.notifications.domain.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.*;

@RestController
@RequestMapping("/notifications")
@Transactional
public class NotificationController {

    @Autowired
    NotificationRepository notificationRepository;

    @GetMapping
    public List<NotificationResponse> getNotifications(@RequestBody NotificationUserRequest request) {
        List<Notification> list = notificationRepository.findByUserIdOrderByCreatedAtDesc(request.getUserId());

        List<NotificationResponse> result = new ArrayList<>();
        for (Notification notification : list) {
            NotificationResponse res = new NotificationResponse();
            res.setNotificationId(notification.getId());
            res.setMessage(notification.getMessage());
            res.setIsRead(notification.getIsRead());
            res.setCreatedAt(notification.getCreatedAt());
            result.add(res);
        }

        return result;
    }

    @PatchMapping("/read")
    public void markAsRead(@RequestBody NotificationIdRequest request) {
        notificationRepository.findById(request.getNotificationId())
            .ifPresent(notification -> {
                notification.setIsRead(true);
                notificationRepository.save(notification);
            });
    }

    @PatchMapping("/read-all")
    public void markAllAsRead(@RequestBody NotificationUserRequest request) {
        List<Notification> list = notificationRepository.findByUserIdOrderByCreatedAtDesc(request.getUserId());
        for (Notification notification : list) {
            notification.setIsRead(true);
        }
        notificationRepository.saveAll(list);
    }

    @Data
    public static class NotificationIdRequest {
        private UUID notificationId;
    }

    @Data
    public static class NotificationUserRequest {
        private UUID userId;
    }

    @Data
    public static class NotificationResponse {
        private UUID notificationId;
        private String message;
        private Boolean isRead;
        private Date createdAt;
    }
}
