package com.saas.support.notification.service;

import com.saas.support.common.exception.ResourceNotFoundException;
import com.saas.support.notification.dto.CreateNotificationRequest;
import com.saas.support.notification.dto.NotificationResponse;
import com.saas.support.notification.entity.Notification;
import com.saas.support.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<NotificationResponse> getUserNotifications(UUID userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<NotificationResponse> getUnreadNotifications(UUID userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public long getUnreadCount(UUID userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Transactional
    public NotificationResponse createNotification(CreateNotificationRequest request) {
        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .title(request.getTitle())
                .message(request.getMessage())
                .isRead(false)
                .build();

        Notification saved = notificationRepository.save(notification);
        log.info("Notification created for user: {}", saved.getUserId());
        return mapToResponse(saved);
    }

    @Transactional
    public void markAsRead(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", id.toString()));
        notification.setRead(true);
        notification.setReadAt(LocalDateTime.now());
        notificationRepository.save(notification);
        log.info("Notification marked as read: {}", id);
    }

    @Transactional
    public void markAllAsRead(UUID userId) {
        List<Notification> unread = notificationRepository
                .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
        unread.forEach(n -> {
            n.setRead(true);
            n.setReadAt(LocalDateTime.now());
        });
        notificationRepository.saveAll(unread);
        log.info("All notifications marked as read for user: {}", userId);
    }

    private NotificationResponse mapToResponse(Notification n) {
        NotificationResponse response = new NotificationResponse();
        response.setId(n.getId());
        response.setUserId(n.getUserId());
        response.setType(n.getType());
        response.setTitle(n.getTitle());
        response.setMessage(n.getMessage());
        response.setRead(n.isRead());
        response.setReadAt(n.getReadAt());
        response.setCreatedAt(n.getCreatedAt());
        return response;
    }
}