package com.smartgrocery.dto.notification

import com.smartgrocery.entity.NotificationType
import java.time.Instant

data class NotificationResponse(
    val id: Long,
    val title: String,
    val message: String,
    val type: NotificationType,
    val referenceType: String?,
    val referenceId: Long?,
    val isRead: Boolean,
    val createdAt: Instant,
    val readAt: Instant?
)

data class NotificationCountResponse(
    val total: Long,
    val unread: Long
)

data class CreateNotificationRequest(
    val userId: Long,
    val title: String,
    val message: String,
    val type: NotificationType,
    val referenceType: String? = null,
    val referenceId: Long? = null
)

data class MarkNotificationsReadRequest(
    val ids: List<Long>
)
