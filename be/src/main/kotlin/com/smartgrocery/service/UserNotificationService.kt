package com.smartgrocery.service

import com.smartgrocery.dto.notification.*
import com.smartgrocery.entity.Notification
import com.smartgrocery.entity.NotificationType
import com.smartgrocery.exception.ErrorCode
import com.smartgrocery.exception.ResourceNotFoundException
import com.smartgrocery.repository.NotificationRepository
import com.smartgrocery.repository.UserRepository
import com.smartgrocery.security.CustomUserDetails
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class UserNotificationService(
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository
) {

    fun getNotifications(unreadOnly: Boolean, pageable: Pageable): Page<NotificationResponse> {
        val currentUser = getCurrentUser()
        
        val page = if (unreadOnly) {
            notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(currentUser.id, pageable)
        } else {
            notificationRepository.findByUserIdOrderByCreatedAtDesc(currentUser.id, pageable)
        }
        
        return page.map { toResponse(it) }
    }

    fun getNotificationCount(): NotificationCountResponse {
        val currentUser = getCurrentUser()
        val total = notificationRepository.countByUserId(currentUser.id)
        val unread = notificationRepository.countByUserIdAndIsReadFalse(currentUser.id)
        return NotificationCountResponse(total = total, unread = unread)
    }

    fun getNotificationById(id: Long): NotificationResponse {
        val currentUser = getCurrentUser()
        val notification = notificationRepository.findByIdAndUserId(id, currentUser.id)
            ?: throw ResourceNotFoundException(ErrorCode.NOTIFICATION_NOT_FOUND)
        return toResponse(notification)
    }

    @Transactional
    fun markAsRead(ids: List<Long>): Int {
        val currentUser = getCurrentUser()
        return notificationRepository.markAsRead(ids, currentUser.id)
    }

    @Transactional
    fun markAllAsRead(): Int {
        val currentUser = getCurrentUser()
        return notificationRepository.markAllAsRead(currentUser.id)
    }

    @Transactional
    fun deleteNotification(id: Long) {
        val currentUser = getCurrentUser()
        val notification = notificationRepository.findByIdAndUserId(id, currentUser.id)
            ?: throw ResourceNotFoundException(ErrorCode.NOTIFICATION_NOT_FOUND)
        notificationRepository.delete(notification)
    }

    @Transactional
    fun createNotification(request: CreateNotificationRequest): NotificationResponse {
        val user = userRepository.findById(request.userId)
            .orElseThrow { ResourceNotFoundException(ErrorCode.USER_NOT_FOUND) }

        val notification = Notification(
            user = user,
            title = request.title,
            message = request.message,
            type = request.type,
            referenceType = request.referenceType,
            referenceId = request.referenceId
        )

        return toResponse(notificationRepository.save(notification))
    }

    // Helper method for other services to create notifications
    @Transactional
    fun sendNotification(
        userId: Long,
        title: String,
        message: String,
        type: NotificationType,
        referenceType: String? = null,
        referenceId: Long? = null
    ): Notification {
        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException(ErrorCode.USER_NOT_FOUND) }

        val notification = Notification(
            user = user,
            title = title,
            message = message,
            type = type,
            referenceType = referenceType,
            referenceId = referenceId
        )

        return notificationRepository.save(notification)
    }

    private fun toResponse(notification: Notification): NotificationResponse {
        return NotificationResponse(
            id = notification.id!!,
            title = notification.title,
            message = notification.message,
            type = notification.type,
            referenceType = notification.referenceType,
            referenceId = notification.referenceId,
            isRead = notification.isRead,
            createdAt = notification.createdAt,
            readAt = notification.readAt
        )
    }

    private fun getCurrentUser(): CustomUserDetails {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication.principal as CustomUserDetails
    }
}
