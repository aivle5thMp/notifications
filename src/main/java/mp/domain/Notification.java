// notifications\src\main\java\mp\domain\Notification.java
package mp.notifications.domain;

import lombok.Data;
import mp.notifications.NotificationsApplication;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "Notification")
@Data
public class Notification {

    @Id
    private UUID id = UUID.randomUUID(); // UUID 자동 생성

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    private Boolean isRead = false;

    private Date createdAt = new Date();

    public static NotificationRepository repository() {
        return NotificationsApplication.applicationContext.getBean(NotificationRepository.class);
    }
}
