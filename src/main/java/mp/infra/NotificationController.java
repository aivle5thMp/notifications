package mp.infra;

import lombok.Data;
import mp.domain.Notification;
import mp.domain.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// spring security
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.transaction.Transactional;
import java.util.*;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@Transactional
public class NotificationController {

    @Autowired
    NotificationRepository notificationRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<NotificationResponse> getNotifications() {
        UUID userUUID = getCurrentUserId();
        List<Notification> list = notificationRepository.findByUserIdOrderByCreatedAtDesc(userUUID);

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

    @GetMapping("/unread-count")
    @PreAuthorize("isAuthenticated()")
    public UnreadCountResponse getUnreadCount() {
        UUID userUUID = getCurrentUserId();
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndIsReadFalse(userUUID);
        UnreadCountResponse response = new UnreadCountResponse();
        response.setCount(unreadNotifications.size());
        return response;
    }

    @PatchMapping("/read")
    @PreAuthorize("isAuthenticated()")
    public void markAsRead(@RequestBody NotificationIdRequest request) {
        UUID currentUserId = getCurrentUserId();
        notificationRepository.findById(request.getNotificationId())
            .ifPresent(notification -> {
                // 현재 사용자의 알림인지 확인
                if (notification.getUserId().equals(currentUserId)) {
                    notification.setIsRead(true);
                    notificationRepository.save(notification);
                } else {
                    throw new SecurityException("Access denied: Cannot modify other user's notification");
                }
            });
    }

    @PatchMapping("/read-all")
    @PreAuthorize("isAuthenticated()")
    public void markAllAsRead() {
        UUID userUUID = getCurrentUserId();
        List<Notification> list = notificationRepository.findByUserIdOrderByCreatedAtDesc(userUUID);
        for (Notification notification : list) {
            notification.setIsRead(true);
        }
        notificationRepository.saveAll(list);
    }

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UUID) authentication.getPrincipal();
    }

    @Data
    public static class NotificationIdRequest {
        private UUID notificationId;
    }

    @Data
    public static class NotificationResponse {
        private UUID notificationId;
        private String message;
        private Boolean isRead;
        private Date createdAt;
    }

    @Data
    public static class UnreadCountResponse {
        private int count;
    }
}
